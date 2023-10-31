package app.linkbac.fmd.vm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linkbac.fmd.ProcessedQuestion
import app.linkbac.fmd.db.AppDatabase
import app.linkbac.fmd.db.Question
import app.linkbac.fmd.http.ApiResponse
import app.linkbac.fmd.http.QuestionApi
import app.linkbac.fmd.http.QuestionRepositoryImpl
import app.linkbac.fmd.http.ktorHttpClient
import app.linkbac.fmd.utils.dateString
import app.linkbac.fmd.utils.latexToAnnotatedString
import com.himamis.retex.renderer.android.FactoryProviderAndroid
import com.himamis.retex.renderer.android.graphics.ColorA
import com.himamis.retex.renderer.android.graphics.Graphics2DA
import com.himamis.retex.renderer.share.TeXFormula
import com.himamis.retex.renderer.share.TextStyle
import com.himamis.retex.renderer.share.platform.FactoryProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import java.util.Calendar


class HomeViewModel: ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state get() = _state.value

    enum class ContentType {
        Latex, Text
    }

    fun markQuestionResult(context: Context, question: ProcessedQuestion, result: Boolean) {
        _state.value = state.copy(
            questions = state.questions.map {
                if(it.question.uid == question.question.uid) {
                    it.copy(
                        question = it.question.copy(
                            attempted = true,
                            correct = result
                        )
                    )
                } else {
                    it
                }
            }
        )

        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(context).questionDao().update(
                state.questions.find {
                    it.question.uid == question.question.uid
                }!!.question
            )
        }
    }

    fun flagQuestion(context: Context, question: ProcessedQuestion, flagged: Boolean) {
        _state.value = state.copy(
            questions = state.questions.map {
                if(it.question.uid == question.question.uid) {
                    it.copy(
                        question = it.question.copy(
                            flagged = flagged
                        )
                    )
                } else {
                    it
                }
            }
        )

        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(context).questionDao().update(
                state.questions.find {
                    it.question.uid == question.question.uid
                }!!.question
            )
        }
    }

    enum class TextType {
        Latex, Text
    }

    private fun processQuestions(questions: List<Question>, context: Context, density: Density): List<ProcessedQuestion> {
        val processedQuestions: MutableList<ProcessedQuestion> = mutableListOf()
        for(i in questions) {
            processedQuestions.add(
                ProcessedQuestion(
                    question = i,
                    questionAnnotatedString = latexToAnnotatedString(context, i.question, density),
                    answerAnnotatedString = latexToAnnotatedString(context, i.answer, density)
                )
            )
        }
        return processedQuestions
    }

    fun getQuestions(context: Context, density: Density) {
        _state.value = state.copy(isLoading = true)
        // check question queue for questions. take 5 questions from queue and if internet is available, get more questions from server
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(context).questionDao().getAll().let { questions ->
                if(questions.filter{
                    it.forDay.toString() == dateString(Calendar.getInstance().time)
                }.size < 5) {
                    // clear queue and add 5 questions from server
                    val seenQuestions = questions.filter{
                        it.attempted
                    }.map { it.uid }
                    val questionEntities = QuestionRepositoryImpl(QuestionApi(ktorHttpClient)).getQuestions(seenQuestions)
                    when(questionEntities) {
                        is ApiResponse.Success -> {
                            val questionDao = AppDatabase.getInstance(context).questionDao()
                            questionDao.clearQueue()
                            val questionMap = questionEntities.body.map{
                                Question(
                                    uid = it.questionID,
                                    question = it.questionText,
                                    answer = it.answerText,
                                    topic = it.topic,
                                    difficulty = it.difficulty,
                                    examStyle = it.examStyle,
                                    marks = it.marks,
                                    createdAt = it.createdAt,
                                    attempted = false,
                                    forDay = dateString(Calendar.getInstance().time)
                                )
                            }
                            questionDao.insertAll(*questionMap.toTypedArray())
                            viewModelScope.launch(Dispatchers.Main) {
                                _state.value = state.copy(isLoading = false, questions =
                                processQuestions(questionMap.take(5).sortedBy { it.uid }, context, density)
                                )
                            }
                        }
                        else -> {
                            viewModelScope.launch(Dispatchers.Main) {
                                // TODO: read from disk cache
                                _state.value = state.copy(isLoading = false, error = "Error getting questions")
                            }
                        }
                    }

                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                    val filteredQuestions = questions.filter {
                        it.forDay.toString() == dateString(Calendar.getInstance().time)
                    }.take(5)
                        _state.value = state.copy(isLoading = false, questions =
                            processQuestions(filteredQuestions.sortedBy { it.uid }, context, density)
                            )
                    }
                }
            }
        }
    }
}
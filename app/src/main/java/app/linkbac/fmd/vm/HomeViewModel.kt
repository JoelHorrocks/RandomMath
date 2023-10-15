package app.linkbac.fmd.vm

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linkbac.fmd.db.AppDatabase
import app.linkbac.fmd.db.Question
import app.linkbac.fmd.http.QuestionApi
import app.linkbac.fmd.http.QuestionEntity
import app.linkbac.fmd.http.QuestionRepositoryImpl
import app.linkbac.fmd.http.ktorHttpClient
import app.linkbac.fmd.utils.dateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel: ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state get() = _state.value

    enum class ContentType {
        Latex, Text
    }

    fun getQuestions(context: Context) {
        _state.value = state.copy(isLoading = true)
        // check question queue for questions. take 5 questions from queue and if internet is available, get more questions from server
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(context).questionDao().getUnattempted().let { questions ->
                if(questions.filter{
                    it.forDay.toString() == dateString(Calendar.getInstance().time)
                }.size < 5) {
                    // clear queue and add 5 questions from server
                    val questionEntities = QuestionRepositoryImpl(QuestionApi(ktorHttpClient)).getQuestions()
                    val questionDao = AppDatabase.getInstance(context).questionDao()
                    questionDao.clearQueue()
                    questionDao.insertAll(*questionEntities.map{
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
                    }.toTypedArray())
                    viewModelScope.launch(Dispatchers.Main) {
                        _state.value = state.copy(isLoading = false, questions = questionEntities.take(5))
                    }

                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                    val questionEntities = questions.filter {
                        it.forDay.toString() == dateString(Calendar.getInstance().time)
                    }.take(5).map {
                            QuestionEntity(
                                questionID = it.uid,
                                questionText = it.question,
                                answerText = it.answer,
                                topic = it.topic,
                                difficulty = it.difficulty,
                                examStyle = it.examStyle,
                                marks = it.marks,
                                createdAt = it.createdAt
                            )
                        }
                        _state.value = state.copy(isLoading = false, questions = questionEntities)
                    }
                }
            }
        }
    }
}
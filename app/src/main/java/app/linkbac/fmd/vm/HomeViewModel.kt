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
import app.linkbac.fmd.db.AppDatabase
import app.linkbac.fmd.db.Question
import app.linkbac.fmd.http.QuestionApi
import app.linkbac.fmd.http.QuestionRepositoryImpl
import app.linkbac.fmd.http.ktorHttpClient
import app.linkbac.fmd.utils.dateString
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

    fun markQuestionResult(context: Context, question: Question, result: Boolean) {
        // TODO: save to DB
        _state.value = state.copy(
            questions = state.questions.map {
                if(it.uid == question.uid) {
                    it.copy(attempted = true, correct = result)
                } else {
                    it
                }
            }
        )
    }

    enum class TextType {
        Latex, Text
    }

    fun latexToAnnotatedString(context: Context, latex: String, localDensity: Density): Pair<AnnotatedString, MutableMap<String, InlineTextContent>> {
        // split by \( and \), add text as text, render latex and add as image
        var text: MutableList<Pair<TextType, String>> = mutableListOf()
        var latexString = latex
        val parts = latex.split("\\(")
        parts.forEachIndexed { index, s ->
            if(index == 0) {
                text.add(Pair(TextType.Text, s))
            } else {
                val latexParts = s.split("\\)")
                text.add(Pair(TextType.Latex, latexParts[0]))
                if(latexParts.size > 1) {
                    text.add(Pair(TextType.Text, latexParts[1]))
                }
            }
        }

        var annotatedString = AnnotatedString.Builder()
        var inlineContentMap = mutableMapOf<String, InlineTextContent>()
        text.forEach {
            if (it.first == TextType.Text) {
                    annotatedString.append(it.second)
                } else {
                    // render latex
                    val latexBitmap = latexToBitmap(context, it.second)
                    val emToPx = with(localDensity) { 30.sp.toPx() }
                    val sf = emToPx / latexBitmap.height.toFloat()
                    val scaledBitmap = Bitmap.createScaledBitmap(latexBitmap,
                        latexBitmap.width.coerceIn(1, (latexBitmap.width * sf).toInt()),
                        latexBitmap.height.coerceIn(1, (latexBitmap.height * sf).toInt()),
                        true
                    )
                    annotatedString.appendInlineContent(id = text.indexOf(it).toString())
                    val width = with(localDensity) { scaledBitmap.width.toDp().toSp() }
                    val height = with(localDensity) { scaledBitmap.height.toDp().toSp() }
                    inlineContentMap[text.indexOf(it).toString()] = InlineTextContent(
                        Placeholder(
                            width = width,
                            height = height,
                            PlaceholderVerticalAlign.TextCenter
                        )
                    ) {
                        Image(
                            bitmap = scaledBitmap.asImageBitmap(),
                            contentDescription = null
                        )
                    }
            }
        }
        return Pair(annotatedString.toAnnotatedString(), inlineContentMap)
    }

    fun latexToBitmap(context: Context, latex: String): Bitmap {
        if (FactoryProvider.getInstance() == null) {
            FactoryProvider.setInstance(FactoryProviderAndroid(context.assets))
        }

        val formula = TeXFormula(latex)
        //val icon = formula.createTeXIcon(TeXFormula.SERIF, 20.0)
        val builder = formula.TeXIconBuilder()
        val scaleFactor = context.resources.displayMetrics.scaledDensity
        val icon = builder.setSize(20.0 * scaleFactor).setStyle(0).setType(TeXFormula.SERIF).setFGColor(ColorA(android.graphics.Color.BLACK)).build()
        val bitmap = Bitmap.createBitmap(icon.iconWidth, icon.iconHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        var mGraphics = Graphics2DA()
        canvas.drawColor(android.graphics.Color.TRANSPARENT)

        // draw latex
        mGraphics.setCanvas(canvas)
        icon.setForeground(ColorA(android.graphics.Color.BLACK))
        icon.paintIcon(null, mGraphics, 0.0, 0.0)

        //val bitmap = Bitmap.createBitmap(icon.iconWidth, icon.iconHeight, Bitmap.Config.ARGB_8888)
        //canvas.setBitmap(bitmap)

        // save file for debugging
        val filename = "bitmap.png"
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        Log.d("HomeViewModel", "bitmap saved to $filename")

        return bitmap
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
                    val questionMap = questionEntities.map{
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
                        _state.value = state.copy(isLoading = false, questions = questionMap.take(5))
                    }

                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                    val questions = questions.filter {
                        it.forDay.toString() == dateString(Calendar.getInstance().time)
                    }.take(5)
                        _state.value = state.copy(isLoading = false, questions = questions)
                    }
                }
            }
        }
    }
}
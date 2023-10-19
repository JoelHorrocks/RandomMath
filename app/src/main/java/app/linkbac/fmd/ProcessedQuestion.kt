package app.linkbac.fmd

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import app.linkbac.fmd.db.Question

data class ProcessedQuestion(
    val question: Question,
    val questionAnnotatedString: Pair<AnnotatedString, MutableMap<String, InlineTextContent>>,
    val answerAnnotatedString: Pair<AnnotatedString, MutableMap<String, InlineTextContent>>,
)
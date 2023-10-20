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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import java.util.Calendar


class SettingsViewModel: ViewModel() {
    private val _notificationsEnabled = MutableStateFlow(false)
    var notificationsEnabled = _notificationsEnabled.asStateFlow()

}
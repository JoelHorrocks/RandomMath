package app.linkbac.fmd.screen

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import app.linkbac.fmd.R
import app.linkbac.fmd.db.AppDatabase
import app.linkbac.fmd.db.Question
import app.linkbac.fmd.draw.StylusView
import app.linkbac.fmd.utils.dateString
import app.linkbac.fmd.wv.LatexWebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

@Composable
fun Debug(navController: NavController) {
    var databaseData by rememberSaveable { mutableStateOf(listOf<Question>()) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val database = AppDatabase.getInstance(context)
            val questionDao = database.questionDao()
            val questions = questionDao.getAll()
            databaseData = questions
        }
    }
    // TODO: make sure formatdatetime keeps the same format
    Column {
        Text(text = "Today is ${dateString(Calendar.getInstance().time)}")
        Text(text = "Database Data")
        Spacer(modifier = Modifier.height(16.dp))
        for(i in databaseData) {
            Text(text = "Question ${i.uid}, for ${i.forDay!!}")
        }
    }
}
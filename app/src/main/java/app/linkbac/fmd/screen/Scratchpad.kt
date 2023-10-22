package app.linkbac.fmd.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.linkbac.fmd.R
import app.linkbac.fmd.draw.StylusView
import app.linkbac.fmd.utils.latexToAnnotatedString
import app.linkbac.fmd.vm.ScratchpadViewModel

@Composable
fun Scratchpad(navController: NavController, id: String?, scratchpadViewModel: ScratchpadViewModel = viewModel()) {
    val context = LocalContext.current
    var eraser by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scratchpadViewModel.loadQuestion(id?.toIntOrNull(), context)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
            Text(text = "Scratchpad", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            var showAnswer by rememberSaveable { mutableStateOf(false) }
            IconButton(
                onClick = {
                    showAnswer = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddTask,
                    contentDescription = "Answer",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                if(showAnswer) {
                    Popup(
                        onDismissRequest = {
                            showAnswer = false
                        },
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(0.7F)
                                .zIndex(1f),
                        ) {
                            Text(
                                text = "Answer:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                            val density = LocalDensity.current
                            val latexString = latexToAnnotatedString(context, scratchpadViewModel.question.value?.answer ?: "", density)
                            val scrollState = rememberScrollState()
                            Text(
                                latexString.first,
                                inlineContent = latexString.second,
                                lineHeight = 32.sp,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .horizontalScroll(scrollState)
                            )
                            Row {
                                IconButton(onClick = {
                                    scratchpadViewModel.markQuestion(context, true)
                                }) {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = if(scratchpadViewModel.question.value?.correct == true && scratchpadViewModel.question.value?.attempted == true) Color(0xFF388E3C) else Color.Gray)
                                }
                                IconButton(onClick = {
                                    scratchpadViewModel.markQuestion(context, false)
                                }) {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = if(scratchpadViewModel.question.value?.correct != true && scratchpadViewModel.question.value?.attempted == true) Color.Red else Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
            IconButton(
                onClick = {
                    eraser = !eraser
                },
                modifier = Modifier.drawBehind {
                    if(eraser) {
                        drawCircle(
                            color = Color(0xFFE0E0E0),
                            radius = 18.dp.toPx(),
                        )
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_eraser_24),
                    contentDescription = "Eraser",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            )) {
            if(scratchpadViewModel.question.value != null) {
                val density = LocalDensity.current
                val latexString = latexToAnnotatedString(context, scratchpadViewModel.question.value?.question ?: "", density)
                Text(
                    latexString.first,
                    inlineContent = latexString.second,
                    lineHeight = 32.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            StylusView(eraser)
        }
    }
}
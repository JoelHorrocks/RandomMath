package app.linkbac.fmd.screen

import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.webkit.WebViewAssetLoader
import app.linkbac.fmd.R
import app.linkbac.fmd.Screen
import app.linkbac.fmd.utils.latexToAnnotatedString
import app.linkbac.fmd.vm.HomeViewModel


@Composable
fun Home(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        homeViewModel.getQuestions(context, density)
    }

    when {
        homeViewModel.state.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        homeViewModel.state.error.isNotBlank() -> {
            Text(text = homeViewModel.state.error)
        }

        else -> {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Today's questions",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            // Gesture detector to open debug menu
                            modifier = Modifier.pointerInput(Unit) {
                                detectDragGesturesAfterLongPress { change, dragAmount ->
                                    if (dragAmount.y > 20) {
                                        navController.navigate(Screen.Debug.route)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(painter = painterResource(id = R.drawable.ic_fire_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp)
                                .drawBehind {
                                    drawCircle(Color(0xFFFFD978), radius = (size.minDimension / 2F))
                                }
                                .padding(2.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "215", fontSize = 18.sp)
                    }
                }
                items(homeViewModel.state.questions) { question ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            var answerRevealed by rememberSaveable { mutableStateOf(false) }
                            Row(
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                if(question.question.correct && question.question.attempted) {
                                    Icon(Icons.Filled.Check, modifier = Modifier.drawBehind {
                                        drawCircle(Color(0xFFABE294), radius = (size.minDimension / 2F))
                                    }, contentDescription = null, tint = Color(0xFF38B63E))
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else if(!question.question.correct && question.question.attempted) {
                                    Icon(Icons.Filled.Close, modifier = Modifier.drawBehind {
                                        drawCircle(Color(0xFFEB9C9C), radius = (size.minDimension / 2F))
                                    }, contentDescription = null, tint = Color(0xFFB63838))
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(text = question.question.topic, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = question.question.difficulty)
                            }
                            Text(
                                question.questionAnnotatedString.first,
                                inlineContent = question.questionAnnotatedString.second,
                                lineHeight = 32.sp,
                            )
                            Row(
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Button(onClick = {
                                    answerRevealed = !answerRevealed
                                }) {
                                    Text(text = if (answerRevealed) "Hide answer" else "Reveal answer")
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    navController.navigate("${Screen.Scratchpad.route}/${question.question.uid}")
                                }) {
                                    Icon(Icons.Filled.Draw, contentDescription = null)
                                }
                                IconButton(onClick = {
                                    homeViewModel.flagQuestion(context, question, !question.question.flagged)
                                }) {
                                    Icon(Icons.Filled.Flag, contentDescription = null, tint = if (question.question.flagged) Color(0xFFB63838) else LocalContentColor.current)
                                }
                            }
                            if (answerRevealed) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    question.answerAnnotatedString.first,
                                    inlineContent = question.answerAnnotatedString.second
                                )
                                Row {
                                    IconButton(onClick = {
                                        homeViewModel.markQuestionResult(context, question, true)
                                    }) {
                                        Icon(Icons.Filled.Check, contentDescription = null, tint = if(question.question.correct && question.question.attempted) Color(
                                            0xFF38B63E
                                        ) else Color.Gray)
                                    }
                                    IconButton(onClick = {
                                        homeViewModel.markQuestionResult(context, question, false)
                                    }) {
                                        Icon(Icons.Filled.Close, contentDescription = null, tint = if(!question.question.correct && question.question.attempted) Color(0xFFB63838) else Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
                item{
                    if(homeViewModel.state.questions.all { it.question.attempted }) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "\uD83C\uDFAF",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "You've completed all your questions for today!",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "\uD83E\uDD14",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Mark your answers to complete the questions for today",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

}
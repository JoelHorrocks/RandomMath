package app.linkbac.fmd.screen

import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.webkit.WebViewAssetLoader
import app.linkbac.fmd.vm.HomeViewModel
import app.linkbac.fmd.wv.LatexWebView


@Composable
fun Home(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        homeViewModel.getQuestions()
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
                        Text(text = "Today's questions", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "\uD83D\uDD25 215")
                    }
                }
                items(homeViewModel.state.questions) { question ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("question/${question.questionID}")
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            var answerRevealed by rememberSaveable { mutableStateOf(false) }
                            Row(
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text(text = question.topic, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = question.difficulty)
                            }
                            AndroidView(
                                factory = { context ->
                                    LatexWebView(context, question.questionText)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                IconButton(onClick = {}) {
                                    Icon(Icons.Filled.Flag, contentDescription = null)
                                }
                            }
                            if (answerRevealed) {
                                Spacer(modifier = Modifier.height(8.dp))
                                AndroidView(
                                    factory = { context ->
                                        LatexWebView(context, question.answerText)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
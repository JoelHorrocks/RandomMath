package app.linkbac.fmd.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.linkbac.fmd.ProcessedQuestion
import app.linkbac.fmd.R
import app.linkbac.fmd.Screen
import app.linkbac.fmd.vm.FlaggedQuestionsViewModel

@Composable
fun FlaggedQuestions(navController: NavController, flaggedQuestionsViewModel: FlaggedQuestionsViewModel = viewModel()) {
    val state = flaggedQuestionsViewModel.state
    val context =  LocalContext.current
    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        flaggedQuestionsViewModel.getFlaggedQuestions(context, density)
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(text = "Flagged questions",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        when{
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            state.error.isNotBlank() -> {
                Text("Error")
            }

            state.flaggedQuestions.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ) {
                    items(state.flaggedQuestions) {
                        FlaggedQuestionCard(it)
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("No flagged questions")
                }
            }
        }
    }
}

@Composable
fun FlaggedQuestionCard(question: ProcessedQuestion) {
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
                // TODO: allow unflagging questions
                /*IconButton(onClick = {
                    homeViewModel.flagQuestion(context, question, !question.question.flagged)
                }) {
                    Icon(Icons.Filled.Flag, contentDescription = null, tint = if (question.question.flagged) Color(0xFFB63838) else LocalContentColor.current)
                }*/
            }
            if (answerRevealed) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    question.answerAnnotatedString.first,
                    inlineContent = question.answerAnnotatedString.second
                )
            }
        }
    }
}
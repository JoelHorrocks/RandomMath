package app.linkbac.fmd.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.linkbac.fmd.Screen
import app.linkbac.fmd.vm.ProfileViewModel

@Composable
fun Profile(profileViewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        profileViewModel.getQuestions(context, density)
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(modifier = Modifier
            .padding(vertical = 16.dp)) {
            Text(
                "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if(profileViewModel.state.attemptedQuestions.isEmpty()) {
            Text(
                "\uD83D\uDD0E",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Start answering questions to see your stats here!",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn{
                item {
                    Text("Topics",
                            fontSize = 20.sp,
                        fontWeight = FontWeight.Medium)
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text("\uD83C\uDFAF Aim higher")
                }
                items(
                    profileViewModel
                        .state
                        .attemptedQuestions
                        .groupBy { it.question.topic }
                        .filter { it.value.filter {it.question.correct}.size / it.value.size.toFloat() < 0.5F }
                        .map { it.key }
                ) {
                    Text(it)
                }
                item {
                    Text("\uD83E\uDD14 On the right track")
                }
                items(
                    profileViewModel
                        .state
                        .attemptedQuestions
                        .groupBy { it.question.topic }
                        .filter { it.value.filter { it.question.correct}.size / it.value.size.toFloat() >= 0.5F &&
                                it.value.filter {it.question.correct}.size / it.value.size.toFloat() < 0.75F }
                        .map { it.key }
                ) {
                    Text(it)
                }
                item {
                    Text("\uD83C\uDF96️ Great job!")
                }
                items(
                    profileViewModel
                        .state
                        .attemptedQuestions
                        .groupBy { it.question.topic }
                        .filter { it.value.filter { it.question.correct}.size / it.value.size.toFloat() >= 0.75F }
                        .map { it.key }
                ) {
                    Text(it)
                }
                item {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("View all")
                    }
                }
            }
            if(profileViewModel.state.attemptedQuestions.any { it.question.flagged }) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Flagged questions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(profileViewModel.state.attemptedQuestions.filter { it.question.flagged }
                        .take(2)) { question ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        )  {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
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
                                if (question.question.forDay != null) {
                                    Text(
                                        question.question.forDay,
                                    )
                                }
                                var answerRevealed by rememberSaveable { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Button(onClick = {
                                        answerRevealed = !answerRevealed
                                    }) {
                                        Text(text = if (answerRevealed) "Hide answer" else "Reveal answer")
                                    }
                                }
                                if (answerRevealed) {
                                    Text(
                                        question.answerAnnotatedString.first,
                                        inlineContent = question.answerAnnotatedString.second,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text("View all")
                        }
                    }
                }
            }
        }
    }
}
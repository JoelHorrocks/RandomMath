package app.linkbac.fmd.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.linkbac.fmd.Screen
import app.linkbac.fmd.component.gridItems
import app.linkbac.fmd.utils.getTopicIcon
import app.linkbac.fmd.vm.ProfileViewModel
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun Profile(profileViewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        profileViewModel.getQuestions(context, density)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    "Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = 28.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        "Not signed in",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Sign in to save your progress",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        if (profileViewModel.state.attemptedQuestions.isEmpty()) {
            item {
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
            }
        } else {
            item {
                Text(
                    "Maths problem streak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "\uD83D\uDD25", modifier = Modifier
                        .drawBehind {
                            drawCircle(Color(0xFFFFD978), radius = (size.minDimension / 2F))
                        }
                        .padding(2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "4 days",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    ) {
                        val list = arrayOf(
                            Pair("M", "\uD83D\uDE34"),
                            Pair("T", "\uD83D\uDD25"),
                            Pair("W", "\uD83D\uDD25"),
                            Pair("T", "\uD83D\uDE34"),
                            Pair("F", "\uD83D\uDD25"),
                            Pair("S", "\uD83D\uDD25"),
                            Pair("S", "\uD83D\uDE34"),
                        )
                        for (i in list) {
                            Column(
                                modifier = Modifier.width(IntrinsicSize.Max)
                            ) {
                                Text(
                                    i.first,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    i.second, modifier = Modifier
                                        .border(
                                            1.dp,
                                            Color.Gray,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(4.dp),
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if(i.second == "\uD83D\uDD25") {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(end = 4.dp)
                                                .drawBehind {
                                                    drawCircle(
                                                        Color(0xFFABE294),
                                                        radius = (size.minDimension / 2F)
                                                    )
                                                },
                                            tint = Color(0xFF38B63E)
                                        )
                                        Text(Random(list.indexOf(i)).nextInt(0, 5).toString())
                                    } else {
                                        //Text("Rest")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Other",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            val extraItems = listOf(
                Pair(Icons.Default.Flag, "Flagged questions"),
                Pair(Icons.Default.History, "Past questions"),
                Pair(Icons.Default.StackedLineChart, "Answer stats"),
            )
            gridItems(extraItems.size, nColumns = 3) { index ->
                SubmenuCard(extraItems[index])
            }
        }
    }
}

@Composable
fun TopicsCard(item: Pair<String, Float>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    getTopicIcon(item.first),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    item.first,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "${(item.second * 100).roundToInt()}%",
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun SubmenuCard(item: Pair<ImageVector, String>) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    item.first,
                    contentDescription = null,
                    /*modifier = Modifier.drawBehind {
                        drawCircle(
                            Color(0xFFABE294),
                            radius = (size.minDimension / 2F)
                        )
                    }.padding(4.dp)*/
                )
            }
            Row {
                Text(
                    text = item.second,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
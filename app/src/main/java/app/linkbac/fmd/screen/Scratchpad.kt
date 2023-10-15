package app.linkbac.fmd.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import app.linkbac.fmd.draw.StylusView
import app.linkbac.fmd.wv.LatexWebView

@Composable
fun Scratchpad(navController: NavController) {
    val context = LocalContext.current
    var eraser by rememberSaveable { mutableStateOf(false) }
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
                    painter = painterResource(id = R.drawable.eraser),
                    contentDescription = "Save",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(
            color = Color.White
        )) {
            AndroidView(
                factory = { context ->
                    LatexWebView(context, "Expand and simplify \\(\\frac{2x}{x+1} - \\frac{3}{x+1}\\)")
                },
                modifier = Modifier
                    .padding(16.dp)
            )
            StylusView(eraser)
        }
    }
}
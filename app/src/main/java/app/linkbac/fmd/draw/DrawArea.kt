package app.linkbac.fmd.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toIcon
import app.linkbac.fmd.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DrawArea(modifier: Modifier = Modifier, viewModel: StylusViewModel) {
    val context = LocalContext.current
    val stylusState by viewModel.stylusState.collectAsState()
    val strokeStyle = Stroke(width = 8F, cap = StrokeCap.Round, join = StrokeJoin.Round)

    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Canvas(modifier = modifier
            .clipToBounds()
            .pointerInteropFilter {
                viewModel.processMotionEvent(it)
            }
        ) {
            with(stylusState) {
                drawPath(
                    path = this.path,
                    color = Color(this.color),
                    style = strokeStyle
                )
            }
        }
    }
}
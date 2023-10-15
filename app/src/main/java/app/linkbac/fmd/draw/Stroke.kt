package app.linkbac.fmd.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb

data class Stroke(
    var color: Int = Color.Black.toArgb(),
    var size: Float = 8F,
    var drawPoints: MutableList<DrawPoint> = mutableListOf(),
)

data class StrokePath(
    var color: Int = Color.Black.toArgb(),
    var size: Float = 8F,
    var path: Path = Path(),
)
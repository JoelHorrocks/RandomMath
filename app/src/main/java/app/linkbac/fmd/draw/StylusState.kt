package app.linkbac.fmd.draw

import androidx.compose.ui.graphics.Path

data class StylusState(
    var pressure: Float = 0F,
    var orientation: Float = 0F,
    var tilt: Float = 0F,
    var paths: List<StrokePath> = listOf(),
)
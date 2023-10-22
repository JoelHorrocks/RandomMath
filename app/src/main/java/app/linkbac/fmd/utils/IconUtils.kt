package app.linkbac.fmd.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MultilineChart
import androidx.compose.ui.graphics.vector.ImageVector

fun getTopicIcon(topic: String): ImageVector {
    return when(topic) {
        "Pure AS: Integration" -> Icons.Default.AreaChart
        "Pure AS: Differentiation" -> Icons.Default.AutoGraph
        "Pure AS: Quadratics" -> Icons.Default.MultilineChart
        "Pure A: Sequences and series" -> Icons.Default.FormatListNumbered
        else -> Icons.Default.Calculate
    }
}
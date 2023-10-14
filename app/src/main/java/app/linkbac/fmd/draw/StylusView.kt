package app.linkbac.fmd.draw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StylusView(debug: Boolean = false, stylusViewModel: StylusViewModel = viewModel()) {
    val stylusState by stylusViewModel.stylusState.collectAsState()

    Column {
        if(debug) {
            StylusVisualization(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
                viewModel = stylusViewModel
            )
            Divider(
                thickness = 1.dp,
                color = Color.Black,
            )
        }
        DrawArea(modifier = Modifier.fillMaxSize(), viewModel = stylusViewModel)
    }
}
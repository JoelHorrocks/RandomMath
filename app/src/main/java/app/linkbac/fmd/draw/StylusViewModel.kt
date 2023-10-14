package app.linkbac.fmd.draw

import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.TOOL_TYPE_ERASER
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StylusViewModel : ViewModel() {
    private var _stylusState = MutableStateFlow(StylusState())
    val stylusState: StateFlow<StylusState> = _stylusState

    private var currentPath = mutableListOf<DrawPoint>()

    fun createPath(): Path {
        val path: Path = Path()
        for(i in currentPath) {
            if(i.type == DrawPointType.START) {
                path.moveTo(i.x, i.y)
            } else {
                path.lineTo(i.x, i.y)
            }
        }
        return path
    }

    private fun cancelLastStroke() {
        // Find the last START event.
        val lastIndex = currentPath.findLastIndex {
            it.type == DrawPointType.START
        }

        // If found, keep the element from 0 until the very last event before the last MOVE event.
        if (lastIndex > 0) {
            currentPath = currentPath.subList(0, lastIndex - 1)
        }
    }

    fun processMotionEvent(motionEvent: MotionEvent): Boolean {
        val isEraser =
            TOOL_TYPE_ERASER == motionEvent.getToolType(motionEvent.actionIndex) ||
            motionEvent.buttonState == MotionEvent.BUTTON_STYLUS_PRIMARY

        Log.d("StylusViewModel", "isEraser: $isEraser")

        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.add(
                    DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.START)
                )
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
            }
            MotionEvent.ACTION_UP -> {
                val canceled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    (motionEvent.flags and MotionEvent.FLAG_CANCELED) == MotionEvent.FLAG_CANCELED

                if(canceled) {
                    cancelLastStroke()
                } else {
                    currentPath.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                // Unwanted touch detected.
                cancelLastStroke()
            }
            else -> return false
        }

        requestRendering(
            StylusState(
                tilt = motionEvent.getAxisValue(MotionEvent.AXIS_TILT),
                pressure = motionEvent.pressure,
                orientation = motionEvent.orientation,
                color = if (isEraser) Color.Red.toArgb() else Color.Green.toArgb(),
                path = createPath()
            )
        )

        return true
    }

    private fun requestRendering(stylusState: StylusState) {
        // Updates the stylusState, which triggers a flow.
        _stylusState.update {
            return@update stylusState
        }
    }
}

inline fun <T> List<T>.findLastIndex(predicate: (T) -> Boolean): Int {
    val iterator = this.listIterator(size)
    var count = 1
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        if (predicate(element)) {
            return size - count
        }
        count++
    }
    return -1
}
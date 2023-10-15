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

    //private var currentPath = mutableListOf<DrawPoint>()
    // last element in list is current stroke
    private var strokes = mutableListOf<Stroke>()

    fun createPaths(): List<StrokePath> {
        val paths: MutableList<StrokePath> = mutableListOf()
        for (stroke in strokes) {
            val path = Path()
            for (i in stroke.drawPoints) {
                if (i.type == DrawPointType.START) {
                    path.moveTo(i.x, i.y)
                } else {
                    path.lineTo(i.x, i.y)
                }
            }
            paths.add(StrokePath(stroke.color, stroke.size, path))
        }
        return paths
    }

    private fun cancelLastStroke() {
        // Find the last START event.
        val lastIndex = strokes.last().drawPoints.findLastIndex {
            it.type == DrawPointType.START
        }

        // If found, keep the element from 0 until the very last event before the last MOVE event.
        if (lastIndex > 0) {
            strokes.last().drawPoints = strokes.last().drawPoints.subList(0, lastIndex)
        }
    }

    fun processMotionEvent(motionEvent: MotionEvent, eraser: Boolean): Boolean {
        if(strokes.isEmpty()) {
            strokes.add(Stroke())
        }

        if(strokes.last().color == Color.Black.toArgb() && eraser) {
            strokes.add(
                Stroke(
                    size = 20F,
                    color = Color.White.toArgb()
                )
            )
        } else if(strokes.last().color == Color.White.toArgb() && !eraser) {
            strokes.add(
                Stroke(
                    size = 8F,
                    color = Color.Black.toArgb()
                )
            )
        }

        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                strokes.last().drawPoints.add(
                    DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.START)
                )
            }
            MotionEvent.ACTION_MOVE -> {
                strokes.last().drawPoints.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
            }
            MotionEvent.ACTION_UP -> {
                val canceled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    (motionEvent.flags and MotionEvent.FLAG_CANCELED) == MotionEvent.FLAG_CANCELED

                if(canceled) {
                    cancelLastStroke()
                } else {
                    strokes.last().drawPoints.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
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
                paths = createPaths()
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
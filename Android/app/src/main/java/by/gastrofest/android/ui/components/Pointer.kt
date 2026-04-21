package by.gastrofest.android.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged

suspend fun PointerInputScope.detectZoomAndPan(
    onPan: (Offset) -> Unit,
    onZoom: (Float) -> Unit
) {
    awaitPointerEventScope {
        var zoom = 1f
        var pan = Offset.Zero

        while (true) {
            val event = awaitPointerEvent()

            val changes = event.changes
            if (changes.isEmpty()) continue

            if (changes.size == 1) {
                // PAN
                val change = changes[0]
                if (change.positionChanged()) {
                    pan = change.positionChange()
                    onPan(pan)
                    change.consume()
                }
            } else if (changes.size == 2) {
                // PINCH ZOOM
                val first = changes[0]
                val second = changes[1]

                val prevDist = (first.previousPosition - second.previousPosition).getDistance()
                val currDist = (first.position - second.position).getDistance()

                if (prevDist != 0f) {
                    zoom = currDist / prevDist
                    onZoom(zoom)
                }

                first.consume()
                second.consume()
            }

            // завершение
            if (changes.any { it.changedToUp() }) break
        }
    }
}

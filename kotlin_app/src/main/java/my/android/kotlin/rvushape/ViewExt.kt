package my.android.kotlin.rvushape

import android.graphics.Point
import android.view.View

/** return center coordinate relative to parent */
fun View.positionInParent() = Point((left + right) / 2, (bottom + top) / 2)

fun View.moveTo(destinationPosition: Point) {
    val originPosition = positionInParent()
    val horizontalDelta = destinationPosition.x - originPosition.x
    val verticalDelta = destinationPosition.y - originPosition.y
    offsetLeftAndRight(horizontalDelta)
    offsetTopAndBottom(verticalDelta)
}

fun View.isOutOfParent(): Boolean {
    val parentView = parent as? View ?: return false
    return when {
        x < 0 -> true
        y < 0 -> true
        x > parentView.width -> true
        y > parentView.height -> true
        else -> false
    }
}

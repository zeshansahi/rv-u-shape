package my.android.kotlin.rvushape

import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import my.android.kotlin.rvushape.coordinator.TableCoordinator
import kotlinx.android.synthetic.main.item_circle_new.view.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class TableLayoutManager(private val itemMargin: Int) : CoordinatedLayoutManager(itemMargin) {

    override lateinit var coordinator: TableCoordinator

    private fun shouldProcessManually(): Boolean = itemCount <= 7

    override fun canScrollHorizontally(): Boolean {
        return if (shouldProcessManually()) false
        else super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return if (shouldProcessManually()) false
        else super.canScrollVertically()
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) return
        val view = recycler.getViewForPosition(0)
        val (itemWidth, itemHeight) = view.measure()
        val itemMaxDimension = max(itemWidth, itemHeight)
        coordinator = TableCoordinator(
            width = width - itemMaxDimension,
            height = height - itemMaxDimension,
            startPosition = Point(width - itemMaxDimension / 2, height - itemMaxDimension / 2)
        )
        if (shouldProcessManually()) layoutManually(recycler, itemMaxDimension)
        else super.onLayoutChildren(recycler, state)
    }

    override fun layoutView(view: View, currentPoint: Point) = with(coordinator) {
        super.layoutView(view, currentPoint)
        //TODO: HARDCODE WARNING!!!
        view.placeHolder?.rotation = calculateRotation(currentPoint).toFloat()
    }


    private fun layoutManually(recycler: RecyclerView.Recycler, itemSize: Int) {
        removeAllViews()
        val positions = coordinator.fetchManualPositions(itemCount, itemSize)
        positions.forEachIndexed { index, point ->
            val view = recycler.getViewForPosition(index)
            addView(view)
            layoutView(view, point)
        }
    }


    override fun calculateAvailableScroll(dy: Int): Int {
        val firstView = getChildAt(0) ?: return dy
        val lastView = getChildAt(childCount - 1) ?: return dy

        return if (dy < 0) {
            if (lastVisiblePosition == itemCount) {
                val offset = height - lastView.bottom
                max(offset, dy)
            } else {
                dy
            }

        } else {
            if (firstVisiblePosition == 0) {
                val offset = firstView.bottom - height
                min(offset, dy)
            } else {
                dy
            }
        }
    }

    override fun RecyclerView.Recycler.recycleItems(firstView: View, lastView: View, delta: Int) {
        if (delta < 0) {
            if (firstView.isOutOfParent()) {
                removeView(firstView)
                recycleView(firstView)
                ++firstVisiblePosition
            }
        } else {
            if (lastView.isOutOfParent()) {
                removeView(lastView)
                recycleView(lastView)
                --lastVisiblePosition
            }
        }
    }

    override fun RecyclerView.Recycler.restoreItems(firstView: View, lastView: View, delta: Int) =
        with(coordinator) {
            if (delta < 0) {
                if (lastVisiblePosition < itemCount && lastView.bottom < height) {
                    val newView: View = getViewForPosition(lastVisiblePosition)
                    val newCenter = lastView.positionInParent().shiftPosition(itemMargin)
                    addView(newView)
                    layoutView(newView, newCenter)
                    ++lastVisiblePosition
                }
            } else {
                if (firstVisiblePosition > 0 && firstView.bottom < height) {
                    val newView: View = getViewForPosition(firstVisiblePosition - 1)
                    val newCenter = firstView.positionInParent().shiftPosition(-itemMargin)
                    addView(newView, 0)
                    layoutView(newView, newCenter)
                    --firstVisiblePosition
                }
            }
        }


    /** Horizontal scrollbar */
    override fun computeHorizontalScrollExtent(state: RecyclerView.State): Int {
        if (state.itemCount == 0 || childCount == 0) return 0
        // ensure state?
        val firstVisibleChildClosestToStart = getChildAt(0) ?: return 0
        val firstVisibleChildClosestToEnd = getChildAt(childCount - 1) ?: return 0
        return abs(
            getDecoratedRight(firstVisibleChildClosestToStart)
                    - getDecoratedLeft(firstVisibleChildClosestToEnd) + 1
        )
    }

    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
        if (state.itemCount == 0 || childCount == 0) return 0
        // ensure state?
        val firstVisibleChildClosestToStart = getChildAt(0) ?: return 0
        val firstVisibleChildClosestToEnd = getChildAt(childCount - 1) ?: return 0
        return abs(
            getDecoratedRight(firstVisibleChildClosestToStart)
                    - getDecoratedLeft(firstVisibleChildClosestToEnd) + 1
        )
    }
    override fun computeHorizontalScrollOffset(state: RecyclerView.State): Int {
        if (state.itemCount == 0 || childCount == 0) return 0
        // ensure state?

        // first visible child closest to start
        val startChild = getChildAt(0) ?: return 0
        // first visible child closest to end
        val endChild = getChildAt(childCount - 1) ?: return 0

        val startViewPosition = getPosition(startChild)
        val endViewPosition = getPosition(endChild)

        val minPosition = min(startViewPosition, endViewPosition)
        val itemsBefore = max(0, minPosition)

        val laidOutArea = abs(getDecoratedLeft(endChild) - getDecoratedRight(startChild))
        val itemRange: Int = abs(startViewPosition - endViewPosition) + 1
        val avgSizePerRow = laidOutArea.toFloat() / itemRange

        return abs(itemsBefore * avgSizePerRow).roundToInt()
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        if (state.itemCount == 0 || childCount == 0) return 0
        // ensure state?

        // first visible child closest to start
        val startChild = getChildAt(0) ?: return 0
        // first visible child closest to end
        val endChild = getChildAt(childCount - 1) ?: return 0

        val startViewPosition = getPosition(startChild)
        val endViewPosition = getPosition(endChild)

        val minPosition = min(startViewPosition, endViewPosition)
        val itemsBefore = max(0, minPosition)

        val laidOutArea = abs(getDecoratedLeft(endChild) - getDecoratedRight(startChild))
        val itemRange: Int = abs(startViewPosition - endViewPosition) + 1
        val avgSizePerRow = laidOutArea.toFloat() / itemRange

        return abs(itemsBefore * avgSizePerRow).roundToInt()

    }
    override fun computeHorizontalScrollRange(state: RecyclerView.State): Int {
        if (state.itemCount == 0 || childCount == 0) return 0
        // first visible child closest to start
        val startChild = getChildAt(0) ?: return 0
        // first visible child closest to end
        val endChild = getChildAt(childCount - 1) ?: return 0

        val startViewPosition = getPosition(startChild)
        val endViewPosition = getPosition(endChild)

        val laidOutArea = abs(getDecoratedLeft(endChild) - getDecoratedRight(startChild))
        val laidOutRange = abs(startViewPosition - endViewPosition) + 1
        // estimate a size for full list.

        return ((laidOutArea.toFloat() / laidOutRange) * state.itemCount).toInt()
    }

    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {

        if (state.itemCount == 0 || childCount == 0) return 0
        // first visible child closest to start
        val startChild = getChildAt(0) ?: return 0
        // first visible child closest to end
        val endChild = getChildAt(childCount - 1) ?: return 0

        val startViewPosition = getPosition(startChild)
        val endViewPosition = getPosition(endChild)

        val laidOutArea = abs(getDecoratedLeft(endChild) - getDecoratedRight(startChild))
        val laidOutRange = abs(startViewPosition - endViewPosition) + 1
        // estimate a size for full list.

        return ((laidOutArea.toFloat() / laidOutRange) * state.itemCount).toInt()
    }
}
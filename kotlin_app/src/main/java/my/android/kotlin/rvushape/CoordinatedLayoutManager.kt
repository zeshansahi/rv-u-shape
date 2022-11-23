package my.android.kotlin.rvushape

import android.graphics.Point
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import my.android.kotlin.rvushape.coordinator.Coordinator
import kotlin.math.abs

abstract class CoordinatedLayoutManager(
    private val itemMargin: Int
) : RecyclerView.LayoutManager() {

    abstract val coordinator: Coordinator
    private  val TAG = "CoordinatedLayoutManage"
    protected var firstVisiblePosition: Int = 0
    protected var lastVisiblePosition: Int = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }


    /** Layout */
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        var anchorView = getChildAt(0)
        if (anchorView?.isOutOfParent() == true) anchorView = getChildAt(1)
        var anchorPosition = anchorView?.let(::getPosition)
        val anchorPositionInParent = anchorView?.positionInParent()
        if (anchorPosition == RecyclerView.NO_POSITION) anchorPosition = 0
        if (anchorPosition != null) {
            firstVisiblePosition = anchorPosition
            lastVisiblePosition = anchorPosition
        }

        if (itemCount == 0) return
        fill(recycler, anchorPositionInParent)
    }

    private fun fill(recycler: Recycler, anchorPositionInParent: Point?) = with(coordinator) {
        removeAllViews()
        var currentPoint: Point = anchorPositionInParent ?: startPosition
        do {
            val view = recycler.getViewForPosition(lastVisiblePosition)
            addView(view)
            layoutView(view, currentPoint)
            currentPoint = currentPoint.shiftPosition(itemMargin)
            lastVisiblePosition++
        } while (!view.isOutOfParent() && lastVisiblePosition < itemCount)
    }

    protected fun View.measure(): Pair<Int, Int> {
        measureChildWithMargins(this, 0, 0)

        val measuredWidth = getDecoratedMeasuredWidth(this)
        val measuredHeight = getDecoratedMeasuredHeight(this)
        return measuredWidth to measuredHeight
    }
    open fun layoutView(view: View, currentPoint: Point) {
        val (measuredWidth, measuredHeight) = view.measure()
        val left = currentPoint.x - measuredWidth / 2
        val right = currentPoint.x + measuredWidth / 2
        val top = currentPoint.y - measuredHeight / 2
        val bottom = currentPoint.y + measuredHeight / 2

        layoutDecorated(view, left, top, right, bottom)
    }


    /** Scrolling */
    override fun canScrollVertically() = true

    override fun canScrollHorizontally() = true


    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        if (childCount == 0) return 0
        val delta = calculateAvailableScroll(dx)
        if (abs(delta) > 1) scrollEachItem(delta, recycler)
        return delta
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
//        Log.e(TAG, "scrollVerticallyBy: dy:$dy state:$state recycler:$recycler", )
//        if (childCount == 0) return 0
//        val delta = calculateAvailableScroll(dy)
//        if (abs(delta) > 1) scrollEachItem(delta, recycler!!)
//        return delta
     /*   var half=FragTable.width/2
        if (FragTable.position<half){
            if (childCount == 0) return 0
            var d1=if (dy>0)-abs(dy) else abs(dy)
            val delta = calculateAvailableScroll(d1)
            if (abs(delta) > 1) scrollEachItem(delta, recycler!!)
            return delta
        }*/
        if (childCount == 0) return 0
        val delta = calculateAvailableScroll(dy)
        if (abs(delta) > 1) scrollEachItem(delta, recycler!!)
        return delta
    }

    abstract fun calculateAvailableScroll(dy: Int): Int
    abstract fun Recycler.recycleItems(firstView: View, lastView: View, delta: Int)
    abstract fun Recycler.restoreItems(firstView: View, lastView: View, delta: Int)


    private fun scrollEachItem(delta: Int, recycler: Recycler): Int = with(coordinator) {

        if (abs(delta) > 200) return 0

        val anchorView = getChildAt(0) ?: throw IllegalArgumentException("getChildAt(0) is null")
        val anchorPosition = anchorView.let(::getPosition)
        val anchorPositionInParent = anchorView.positionInParent().shiftPosition(delta)
        anchorPosition.let {
            firstVisiblePosition = it
            lastVisiblePosition = it
        }

        var currentPoint: Point = anchorPositionInParent
        for (i in 0 until childCount) {
            if (lastVisiblePosition < itemCount) // prevents endless scrolling
                getChildAt(i)?.let { view ->
                    layoutView(view, currentPoint)
                    currentPoint = currentPoint.shiftPosition(itemMargin)
                    lastVisiblePosition++
                }

        }

        with(recycler) {
            val firstView = getChildAt(0) ?: return delta
            val lastView = getChildAt(childCount - 1) ?: return delta

            recycleItems(firstView, lastView, delta)
            restoreItems(firstView, lastView, delta)
        }
        return delta
    }


    override fun onSaveInstanceState(): Parcelable {
        return ItemsPositionState(firstVisiblePosition, lastVisiblePosition)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        (state as ItemsPositionState).let {
            firstVisiblePosition = it.firstVisiblePosition
            lastVisiblePosition = it.firstVisiblePosition
        }
    }
}
package my.android.kotlin.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder>(
    var mList: MutableList<T>
) :  RecyclerView.Adapter<V>() {

    override fun getItemCount() = mList.size

    open fun setData(list: List<T>) {
        this.mList = list.toMutableList()
        notifyDataSetChanged()
    }

    open fun getItem(position: Int): T? {
        return mList.getOrNull(position)
    }

    open fun clearData() {
        setData(ArrayList())
    }

    open fun addItems(items: List<T>) {
        val initialPos = mList.size
        val finalPos = items.size + initialPos
        mList.addAll(items)
        notifyItemRangeChanged(initialPos, finalPos)
    }
}
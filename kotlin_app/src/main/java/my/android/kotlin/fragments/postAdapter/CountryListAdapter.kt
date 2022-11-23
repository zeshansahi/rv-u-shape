package my.android.kotlin.fragments.postAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.android.kotlin.R

import my.android.kotlin.base.BaseRecyclerViewAdapter
import my.android.kotlin.databinding.ItemCircleNewBinding

import my.android.kotlin.model.ItemObject

class CountryListAdapter(users: List<ItemObject>) :
    BaseRecyclerViewAdapter<ItemObject, CountryListAdapter.CountryItemViewHolder>(users.toMutableList()) {


    class CountryItemViewHolder(val binding: ItemCircleNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: ItemObject) {
            binding?.imgPlaceHolder?.setImageResource(R.drawable.ic_head)
            binding?.imgUser?.setImageResource(R.drawable.ic_account_circle)
            binding?.tvName?.text=movie.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        val binding: ItemCircleNewBinding = ItemCircleNewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryItemViewHolder(binding);
    }


    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun setData(list: List<ItemObject>) {
        mList.clear()
        mList.addAll(list)
    }
}
package mohit.android.assignment.kotlin.fragments.postAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mohit.android.assignment.kotlin.base.BaseRecyclerViewAdapter
import mohit.android.assignment.kotlin.databinding.ItemCounteryBinding
import mohit.android.assignment.kotlin.model.Country

class CountryListAdapter(users: List<Country>) :
    BaseRecyclerViewAdapter<Country, CountryListAdapter.CountryItemViewHolder>(users.toMutableList()) {


    class CountryItemViewHolder(val binding: ItemCounteryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: Country) {
            binding.items=movie

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        val binding: ItemCounteryBinding = ItemCounteryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryItemViewHolder(binding);
    }


    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun setData(list: List<Country>) {
        mList.clear()
        mList.addAll(list)
    }
}
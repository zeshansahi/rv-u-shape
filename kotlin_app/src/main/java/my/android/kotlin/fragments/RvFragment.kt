package my.android.kotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.android.kotlin.databinding.FragmentCountriesBinding
import my.android.kotlin.fragments.postAdapter.CountryListAdapter
import my.android.kotlin.model.ItemObject
import my.android.kotlin.rvushape.TableLayoutManager


class RvFragment : Fragment() {

    private val moviesAdapter = CountryListAdapter(emptyList())

    private lateinit var _binding: FragmentCountriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountriesBinding.inflate(inflater)

        _binding.rvPost.apply {
//            layoutManager = LinearLayoutManager(requireContext())
            layoutManager = TableLayoutManager(itemMargin = 255)
            adapter = moviesAdapter
            overScrollMode=View.OVER_SCROLL_NEVER
        }

        setDummyItems()
        return _binding.root
    }

    fun setDummyItems() {
        var list = arrayListOf<ItemObject>()
        for (i in 0..20) {
            list.add(ItemObject())
        }
        moviesAdapter.addItems(list)
    }

}
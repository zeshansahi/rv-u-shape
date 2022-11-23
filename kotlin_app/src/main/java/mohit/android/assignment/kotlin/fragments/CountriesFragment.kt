package mohit.android.assignment.kotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mohit.android.assignment.kotlin.data.Resource
import mohit.android.assignment.kotlin.data.RetrofitBuilder
import mohit.android.assignment.kotlin.databinding.FragmentCountriesBinding
import mohit.android.assignment.kotlin.fragments.postAdapter.CountryListAdapter
import mohit.android.assignment.kotlin.utils.ViewModelFactory


class CountriesFragment : Fragment() {
    private lateinit var viewModel: CountriesViewModel
    private val moviesAdapter = CountryListAdapter(emptyList())

    private lateinit var _binding: FragmentCountriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountriesBinding.inflate(inflater)
        setupViewModel()
        _binding.rvPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }
        getCountriesList()
        return _binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                CountriesRepository(RetrofitBuilder.provideApiService()),
                )
        ).get(CountriesViewModel::class.java)
    }

    private fun getCountriesList() {

        viewModel.countriesData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    moviesAdapter.addItems(it.data!!)
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
package mohit.android.assignment.kotlin.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mohit.android.assignment.kotlin.fragments.CountriesRepository
import mohit.android.assignment.kotlin.fragments.CountriesViewModel

class ViewModelFactory(
    private val repository: CountriesRepository ,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
            return CountriesViewModel(repository) as T
        }


        throw IllegalArgumentException("Unknown class name")
    }

}
package mohit.android.assignment.kotlin.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohit.android.assignment.kotlin.data.Resource
import mohit.android.assignment.kotlin.model.Country


class CountriesViewModel(
    private val repository: CountriesRepository
) : ViewModel() {
    private val TAG = "CountriesViewModel"
    val countriesData: MutableLiveData<Resource<ArrayList<Country>>> =
        MutableLiveData<Resource<ArrayList<Country>>>()

    init {
        getCountries()
    }

    fun getCountries() = viewModelScope.launch {
        try {
            repository.getCountries().let {
                if (it.isSuccessful) {
                    countriesData.postValue(Resource.success(it.body()))
                } else {
                    countriesData.postValue(
                        Resource.error(
                            "Error:${it.message()} ${it.code()}",
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            countriesData.postValue(Resource.error("Error:${e.localizedMessage}", null))
        }
    }
}
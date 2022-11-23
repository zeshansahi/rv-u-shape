package mohit.android.assignment.kotlin.fragments

import mohit.android.assignment.kotlin.data.ApiService

class CountriesRepository (val apiService: ApiService){
    suspend fun getCountries( ) = apiService.getCountries()

}
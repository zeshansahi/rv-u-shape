package mohit.android.assignment.kotlin.data

import mohit.android.assignment.kotlin.model.Country
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {
    @GET("countries.json")
    suspend fun getCountries(
    ): Response<ArrayList<Country>>
}


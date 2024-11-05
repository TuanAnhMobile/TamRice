package android.mobile.tamrice.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API_CLIENT {
    private const val BASE_URL = "http://10.0.2.2:3000"

    val apiService: API_USER = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API_USER::class.java);
}
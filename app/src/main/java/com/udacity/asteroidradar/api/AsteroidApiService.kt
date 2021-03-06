package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.utils.Constants.BASE_URL
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
  .add(KotlinJsonAdapterFactory())
  .build()

private val retrofit = Retrofit.Builder()
  .addConverterFactory(ScalarsConverterFactory.create())
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .baseUrl(BASE_URL)
  .build()

interface AsteroidApiService {
  @GET("neo/rest/v1/feed")
  suspend fun getAsteroids(
    @Query("start_date") start_date: String,
    @Query("end_date") end_date: String,
    @Query("api_key") api_key: String = API_KEY,
  ): String

  @GET("planetary/apod")
  suspend fun getPictureOfDay(@Query("api_key") api_key: String = API_KEY): PictureOfDay
}

object AsteroidApi {
  val retrofitService: AsteroidApiService by lazy {
    retrofit.create(AsteroidApiService::class.java)
  }
}
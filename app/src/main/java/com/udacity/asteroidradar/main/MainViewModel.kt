package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

  private val database = AsteroidDatabase.getInstance(application)

  private val videosRepository = AsteroidsRepository(database)

  private val _pictureOfDay = MutableLiveData<PictureOfDay>()
  val pictureOfDay: LiveData<PictureOfDay>
    get() = _pictureOfDay

  private var _asteroids = videosRepository.getAsteroids()
  val asteroids: LiveData<List<Asteroid>>
    get() = _asteroids

  private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

  val navigateToSelectedAsteroid: LiveData<Asteroid?>
    get() = _navigateToSelectedAsteroid

  init {
    viewModelScope.launch {
      _pictureOfDay.value = AsteroidApi.retrofitService.getPictureOfDay()
      videosRepository.refreshAsteroids()
    }
  }

  private fun getAsteroids(startDate: String, endDate: String) {
    viewModelScope.launch {
      _asteroids.value = videosRepository.getAsteroids(startDate, endDate)
    }
  }

  fun displayAsteroidDetails(asteroid: Asteroid) {
    _navigateToSelectedAsteroid.value = asteroid
  }

  fun displayAsteroidDetailsComplete() {
    _navigateToSelectedAsteroid.value = null
  }

  fun updateFilter(startDate: String, endDate: String) {
    getAsteroids(startDate, endDate)
  }

  class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(app) as T
      }
      throw IllegalArgumentException("Unable to construct viewmodel")
    }
  }

}
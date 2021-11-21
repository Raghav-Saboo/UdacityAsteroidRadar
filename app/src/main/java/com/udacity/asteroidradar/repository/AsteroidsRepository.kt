/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.getSeventhDay
import com.udacity.asteroidradar.utils.getToday
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {

  /**
   * Refresh the asteroids stored in the offline cache.
   *
   * This function uses the IO dispatcher to ensure the database insert database operation
   * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
   * function is now safe to call from any thread including the Main thread.
   *
   * To actually load the asteroids for use, observe [asteroids]
   */
  suspend fun refreshAsteroids() {
    withContext(Dispatchers.IO) {
      val response = AsteroidApi.retrofitService.getAsteroids(getToday(), getSeventhDay())
      val asteroids = parseAsteroidsJsonResult(JSONObject(response))
      database.asteroidDatabaseDao.insertAll(*asteroids.asDatabaseModel())
    }
  }

  fun getAsteroids(): MutableLiveData<List<Asteroid>> {
    return getMutableLiveDomainData(database.asteroidDatabaseDao.getAsteroids())
  }

  suspend fun getAsteroids(startDate: String, endDate: String): List<Asteroid> {
    return withContext(Dispatchers.IO) {
      database.asteroidDatabaseDao.getAsteroidsInDateRange(startDate, endDate)
        .asDomainModel()
    }
  }

  private fun getMutableLiveDomainData(asteroids: LiveData<List<DatabaseAsteroid>>): MutableLiveData<List<Asteroid>> {
    return Transformations.map(asteroids) {
      it.asDomainModel()
    } as MutableLiveData<List<Asteroid>>
  }
}

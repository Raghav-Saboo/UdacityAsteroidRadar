package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
  @Query("select * from asteroid_table order by closeApproachDate")
  fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg asteroids: DatabaseAsteroid)

  @Query("select * from asteroid_table where closeApproachDate >= :startDate  and closeApproachDate <= :endDate order by closeApproachDate")
  fun getAsteroidsInDateRange(
    startDate: String,
    endDate: String,
  ): List<DatabaseAsteroid>
}
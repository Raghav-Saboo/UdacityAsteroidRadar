package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.models.Asteroid


fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
  return map {
    Asteroid(
      id = it.id,
      codename = it.codename,
      closeApproachDate = it.closeApproachDate,
      absoluteMagnitude = it.absoluteMagnitude,
      estimatedDiameter = it.estimatedDiameter,
      relativeVelocity = it.relativeVelocity,
      distanceFromEarth = it.distanceFromEarth,
      isPotentiallyHazardous = it.isPotentiallyHazardous)
  }
}

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
  return this.map {
    DatabaseAsteroid(
      id = it.id,
      codename = it.codename,
      closeApproachDate = it.closeApproachDate,
      absoluteMagnitude = it.absoluteMagnitude,
      estimatedDiameter = it.estimatedDiameter,
      relativeVelocity = it.relativeVelocity,
      distanceFromEarth = it.distanceFromEarth,
      isPotentiallyHazardous = it.isPotentiallyHazardous)
  }.toTypedArray()
}

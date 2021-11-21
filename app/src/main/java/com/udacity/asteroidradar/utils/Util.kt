package com.udacity.asteroidradar.utils


import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): List<Asteroid> {
  val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

  val asteroidList = ArrayList<Asteroid>()

  val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
  for (formattedDate in nextSevenDaysFormattedDates) {
    val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

    for (i in 0 until dateAsteroidJsonArray.length()) {
      val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
      val id = asteroidJson.getLong("id")
      val codename = asteroidJson.getString("name")
      val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
      val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
        .getJSONObject("kilometers").getDouble("estimated_diameter_max")

      val closeApproachData = asteroidJson
        .getJSONArray("close_approach_data").getJSONObject(0)
      val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
        .getDouble("kilometers_per_second")
      val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
        .getDouble("astronomical")
      val isPotentiallyHazardous = asteroidJson
        .getBoolean("is_potentially_hazardous_asteroid")

      val asteroid = Asteroid(id,
                              codename,
                              formattedDate,
                              absoluteMagnitude,
                              estimatedDiameter,
                              relativeVelocity,
                              distanceFromEarth,
                              isPotentiallyHazardous)
      asteroidList.add(asteroid)
    }
  }

  return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
  val formattedDateList = ArrayList<String>()

  val calendar = Calendar.getInstance()
  for (i in 0..DEFAULT_END_DATE_DAYS) {
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
    formattedDateList.add(dateFormat.format(currentTime))
    calendar.add(Calendar.DAY_OF_YEAR, 1)
  }

  return formattedDateList
}

fun getToday(): String {
  return getFormattedDate(Calendar.getInstance().time)
}

fun getSeventhDay(): String {
  return getFormattedDate(addDays(Calendar.getInstance().time, 7))
}

fun getFormattedDate(date: Date): String {
  val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
  return dateFormat.format(date)
}

fun addDays(date: Date, amount: Int): Date {
  val c = Calendar.getInstance()
  c.time = date
  c.add(Calendar.DAY_OF_YEAR, amount)
  return c.time
}


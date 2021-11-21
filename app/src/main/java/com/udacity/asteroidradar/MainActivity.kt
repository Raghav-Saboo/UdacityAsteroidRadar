package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.RefreshDataWorker
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  private val applicationScope = CoroutineScope(Dispatchers.Default)

  private fun delayedInit() {
    applicationScope.launch {
      setupRecurringWork()
    }
  }

  private fun setupRecurringWork() {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.UNMETERED)
      .setRequiresCharging(true)
      .apply {
        setRequiresDeviceIdle(true)
      }.build()

    val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
      .setConstraints(constraints)
      .build()

    WorkManager.getInstance().enqueueUniquePeriodicWork(
      RefreshDataWorker.WORK_NAME,
      ExistingPeriodicWorkPolicy.KEEP,
      repeatingRequest)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    delayedInit()
  }

}

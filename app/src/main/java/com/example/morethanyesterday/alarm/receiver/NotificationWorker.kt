package com.example.morethanyesterday.alarm.receiver

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.morethanyesterday.usecase.GetWeatherUseCase
import javax.inject.Inject

class NotificationWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
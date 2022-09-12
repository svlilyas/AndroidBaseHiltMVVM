package com.pi.androidbasehiltmvvm.core.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.features.NavHostActivity

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "channelId"
        const val NOTIFICATION = 1
    }

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        //uploadImages()

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val intent = Intent(applicationContext, NavHostActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Content Title")
            .setContentText("Subscribe Channel")
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }
}
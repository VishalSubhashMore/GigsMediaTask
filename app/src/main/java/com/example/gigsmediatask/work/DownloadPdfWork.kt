package com.example.gigsmediatask.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.gigsmediatask.R
import com.example.gigsmediatask.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class DownloadPdfWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    val notificationTitle: String = "File Download"
    val downloadSuccess: String = "File Downloaded"
    val downloadFailed: String = "File Download Failed"

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val progress = "Starting Download"
        showNotification(notificationTitle, progress)
        downloadFile()
        Result.success()
    }

    private fun downloadFile() {
        val call =
            (applicationContext as MyApplication).getDownloadRetroComponent()
                .getRetroServiceInterface()
                .downloadPDF()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
//                    response.body()?.byteStream()?.saveToFile("DCIM/${System.currentTimeMillis()}.pdf")
                    response.body()?.let {
                        saveFileToStorage(
                            it,
                            "/${System.currentTimeMillis()}.pdf",
                            this@DownloadPdfWork.applicationContext
                        )
                    }
                } else {

                    showNotification(notificationTitle, downloadFailed)
//                    println(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                println(t.message)
                showNotification(notificationTitle, downloadFailed)
            }

        })
    }

    private fun saveFileToStorage(
        body: ResponseBody,
        filename: String,
        context: Context
    ) {

        val contentResolver = context.contentResolver
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        var fileZizeDownloaded: Long = 0


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
            }

            val uri =
                contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                )
            try {
                var pfd: ParcelFileDescriptor? = null
                try {
                    pfd =
                        uri?.let { contentResolver.openFileDescriptor(it, "w") }!!
                    val buf = ByteArray(4 * 1024)
                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(pfd.fileDescriptor)
                    var len: Int

                    while (true) {
                        val read = inputStream.read(buf)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(buf, 0, read)
                        fileZizeDownloaded += read.toLong()
                    }

                    outputStream.flush()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showNotification(notificationTitle, downloadFailed)
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                    pfd?.close()
                }
                values.clear()
                values.put(MediaStore.Video.Media.IS_PENDING, 0)
                if (uri != null) {
                    context.contentResolver.update(uri, values, null, null)
                    outputStream = context.contentResolver.openOutputStream(uri)
                }
                if (outputStream == null) {
                    throw IOException("Failed to get output stream.")
                }
                showNotification(notificationTitle, downloadSuccess)

            } catch (e: IOException) {
                if (uri != null) {
                    context.contentResolver.delete(uri, null, null)
                }
                showNotification(notificationTitle, downloadFailed)
            } finally {
                outputStream?.close()
            }
        } else {
            val directory_path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            val file = File(directory_path)
            if (!file.exists()) {
                file.mkdirs()
            }
            val targetPdf = directory_path + filename
            val filePath = File(targetPdf)

            try {
                val fileReader = ByteArray(4 * 1024)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(filePath)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileZizeDownloaded += read.toLong()

                }
                outputStream.flush()

            } catch (e: IOException) {
                e.printStackTrace()
                showNotification(notificationTitle, downloadFailed)
            } finally {
                inputStream?.close()
                outputStream?.close()
                showNotification(notificationTitle, downloadSuccess)
            }
        }
    }

    private fun showNotification(notificationTitle: String, notificationText: String) {
        val channelId = applicationContext.getString(R.string.notification_channel_id)
        val description = applicationContext.getString(R.string.notification_title)
        notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(applicationContext, channelId)
                .setContentText(notificationText)
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.drawable.ic_launcher_background)
        } else {

            builder = Notification.Builder(applicationContext)
                .setContentText(notificationText)
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.drawable.ic_launcher_background)
        }
        notificationManager.notify(channelId.toInt(), builder.build())
    }

}
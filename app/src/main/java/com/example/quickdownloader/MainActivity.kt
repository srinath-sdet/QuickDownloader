package com.example.quickdownloader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var etUrl: EditText
    private lateinit var tvStatus: TextView
    private lateinit var btnDownload: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUrl = findViewById(R.id.etUrl)
        tvStatus = findViewById(R.id.tvStatus)
        btnDownload = findViewById(R.id.btnDownload)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)

        requestNotificationPermission()

        btnDownload.setOnClickListener {
            val url = etUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                confirmAndStartDownload(url)
            } else {
                Toast.makeText(this, "Please enter or paste a URL", Toast.LENGTH_SHORT).show()
            }
        }

        handleIncomingIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrEmpty()) {
                val extractedUrl = extractUrl(sharedText)
                if (extractedUrl != null) {
                    etUrl.setText(extractedUrl)
                    tvStatus.text = "Received shared link:"
                    confirmAndStartDownload(extractedUrl)
                } else {
                    Toast.makeText(this, "No valid link found in shared text", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun extractUrl(text: String): String? {
        val parts = text.split("\\s+".toRegex())
        return parts.firstOrNull { it.startsWith("http://") || it.startsWith("https://") }
    }

    private fun confirmAndStartDownload(url: String) {
        AlertDialog.Builder(this)
            .setTitle("Download Video")
            .setMessage("Do you want to download this video?\n\n$url")
            .setPositiveButton("Download") { _, _ ->
                startFastDownload(url)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun startFastDownload(url: String) {
        btnDownload.isEnabled = false
        progressBar.visibility = View.VISIBLE
        progressBar.progress = 0
        tvProgress.visibility = View.VISIBLE
        tvProgress.text = "Fetching video details..."

        executor.execute {
            try {
                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs()
                }

                val request = YoutubeDLRequest(url).apply {
                    addOption("--no-mtime")
                    addOption("-o", "${downloadDir.absolutePath}/%(title)s.%(ext)s")
                    // Format selection: best mp4 format or best video+audio
                    addOption("-f", "best[ext=mp4]/bestvideo[ext=mp4]+bestaudio[ext=m4a]/best")
                }

                YoutubeDL.getInstance().execute(request) { progress, etaInSeconds, line ->
                    mainHandler.post {
                        if (progress > 0) {
                            progressBar.progress = progress.toInt()
                            tvProgress.text = "Downloading: ${progress.toInt()}% (ETA: ${etaInSeconds}s)"
                        } else {
                            tvProgress.text = line ?: "Processing..."
                        }
                    }
                }

                mainHandler.post {
                    btnDownload.isEnabled = true
                    progressBar.visibility = View.GONE
                    tvProgress.text = "Download completed!"
                    Toast.makeText(this@MainActivity, "Video downloaded to Downloads folder!", Toast.LENGTH_LONG).show()

                    // Scan file to appear in Gallery
                    MediaScannerConnection.scanFile(
                        this@MainActivity,
                        arrayOf(downloadDir.absolutePath),
                        null,
                        null
                    )
                }
            } catch (e: Exception) {
                mainHandler.post {
                    btnDownload.isEnabled = true
                    progressBar.visibility = View.GONE
                    tvProgress.text = "Failed: ${e.message}"
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }
}

package com.nativeaudioapi

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.room.Room
import com.database.AppDatabase
import com.database.DbAudio
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableArray
import com.facebook.react.modules.core.DeviceEventManagerModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NativeAudioApiModule(reactContext: ReactApplicationContext): NativeAudioApiSpec(reactContext) {
    private var recorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private val reactContxt: ReactApplicationContext = reactContext
    private lateinit var audioFile: File
    private lateinit var audioPath: String
    private lateinit var filename: String
    private var runnable: Runnable? = null
    private val backgroundHandlerThread = HandlerThread("AmplitudePollerThread").apply { start() }
    private val backgroundHandler = Handler(backgroundHandlerThread.looper)
    val delay = 100L
    var totalDuration = 0L
    private var ampVal = 0.0f
    private var currentPosition = 0
    private var seekVal = 1000L


    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            reactContext.applicationContext,
            AppDatabase::class.java,
            "audio_database"
        ).build()
    }

    private fun getFileName(): String?{
        try {
            // External cache directory (cleared by the system when needed)
            val cacheDir = reactContxt.externalCacheDir
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            filename = "audio_recording$timeStamp.m4a"
            audioFile = File(cacheDir,filename )
        } catch (e: Exception) {
            Log.e("ERROR IN FILE FORMAT", "Error file format")
            return null
        }
        audioPath = audioFile.absolutePath
        return audioPath
    }


    override fun getName() = NAME

    override fun startRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = MediaRecorder(reactContxt).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                val output = getFileName()
                if (output == null) {
                    Log.e(NAME, "Failed to create file for recording.")
                    return
                }
                setOutputFile(audioFile.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e("ERROR PREPARE", "prepare() failed", e)
                }

                start()
                startAmplitudeUpdates()
            }
        } else {
            @Suppress("DEPRECATION")
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                val output = getFileName()
                if (output == null) {
                    Log.e(NAME, "Failed to create file for recording.")
                    return
                }
                setOutputFile(audioFile.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e("ERROR PREPARE", "prepare() failed", e)
                }

                start()
                startAmplitudeUpdates()
            }
        }

    }

    override fun stopRecording(): String {
        stopAmplitudeUpdates()
        try {
            recorder?.stop()
        } catch (e: Exception) {
            Log.e(NAME, "Failed to stop recorder", e)
        }
        recorder?.release()
        recorder = null
        return filename
    }

    override fun resumeRecording() {
        recorder?.resume()
        startAmplitudeUpdates()
    }

    override fun pauseRecording() {
        recorder?.pause()
        stopAmplitudeUpdates()
    }


    private fun startAmplitudeUpdates() {
        stopAmplitudeUpdates()

        runnable = Runnable {
            ampVal = ((recorder?.maxAmplitude ?: 0) / 7).coerceAtMost(200).toFloat()
            //ampVal = ((recorder?.maxAmplitude ?: 0).coerceAtMost(32767)) * (400.0 / 32767.0)

            val params = Arguments.createMap().apply {
                putDouble("amplitude", ampVal.toDouble())
            }

            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onAmplitude", params)

            runnable?.let { backgroundHandler.postDelayed(it, delay) }

        }
        backgroundHandler.postDelayed(runnable!!, delay)


    }

    private fun stopAmplitudeUpdates() {
        runnable?.let {
            backgroundHandler.removeCallbacks(it)
            runnable = null
        }
    }

    private fun emitEvents(eventName: String, message: String){
        val params = Arguments.createMap().apply {
            putString("message", message)
        }

        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    override fun deleteRecording() {
        stopAmplitudeUpdates()
        recorder?.apply {
            stop()
            release()

        }

        val file = File(audioPath)

        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                emitEvents("onDeleteRecord", "Record deleted")
            } else {
                emitEvents("onDeleteRecord", "Recording not deleted")
            }
        } else {
            emitEvents("onDeleteRecord", "File does not exist")
        }
    }

    override fun saveRecording(path: String?) {
        val cacheDir = reactContxt.externalCacheDir

        val oldFile = File(audioPath)
        val newFile = File(cacheDir, path!!)

        if (filename != path) {
            oldFile.renameTo(newFile)
            emitEvents("onSaveRecord", "Recording Saved")
        } else {
            emitEvents("onSaveRecord", "Recording Saved")
        }

    }

    override fun addListener(eventName: String?) {

    }

    override fun removeListeners(count: Double) {

    }

    override fun insertAudio(
        filename: String?,
        duration: Double,
        createdAt: Double,
        promise: Promise?
    ) {

        val dbObj = DbAudio(filename!!, audioPath, duration.toLong(), createdAt.toLong())
        val audioDb = db.dbAudioDao()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                audioDb.insertAudio(dbObj)
                promise?.resolve(null)
            } catch (e: Exception) {
                promise?.reject("INSERT_ERROR", e)
            }
        }

    }

    override fun deleteAudio(
        filename: String?,
        filepath: Double,
        duration: Double,
        createdAt: Double,
        promise: Promise?
    ) {
        val dbObj = DbAudio(filename!!, audioPath, duration.toLong(), createdAt.toLong())
        val audioDb = db.dbAudioDao()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                audioDb.deleteAudio(dbObj)
                promise?.resolve(null)
            } catch (e: Exception) {
                promise?.reject("DELETE_ERROR", e)
            }
        }
    }

    override fun getAllAudio(promise: Promise) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allAudio = db.dbAudioDao().getAllAudio()

                val result: WritableArray = Arguments.createArray()

                for (audio in allAudio) {
                    val audioMap = Arguments.createMap().apply {
                        putInt("id", audio.id)
                        putString("fileName", audio.fileName)
                        putString("filePath", audio.filePath)
                        putLong("duration", audio.duration)
                        putLong("createdAt", audio.createdAt)
                    }
                    result.pushMap(audioMap)
                }

                promise.resolve(result)
            } catch (e: Exception) {
                promise.reject("GET_ALL_ERROR", e)
            }
        }
    }

    override fun deleteAllAudio(promise: Promise?) {
        val audioDb = db.dbAudioDao()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                audioDb.deleteAllAudio()
                promise?.resolve(null)
            } catch (e: Exception) {
                promise?.reject("DELETE_ALL_ERROR", e)
            }
        }
    }

    private fun startMediaPositionPolling(){
        stopMediaPositionPolling()
        runnable = Runnable {
            currentPosition = mediaPlayer?.currentPosition ?: 0
            val params = Arguments.createMap().apply {
                putLong("currentPosition", currentPosition.toLong())
            }

            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onGetPosition", params)

            runnable?.let { backgroundHandler.postDelayed(it, seekVal) }

        }
        backgroundHandler.postDelayed(runnable!!, seekVal)
    }

    private fun stopMediaPositionPolling(){
        runnable?.let {
            backgroundHandler.removeCallbacks(it)
            runnable = null
        }
    }

    override fun playAudio(path: String?, promise: Promise?) {
        try {
             if(mediaPlayer == null){
                 mediaPlayer = MediaPlayer()
                 mediaPlayer?.setDataSource(path)
                 mediaPlayer?.prepare()
             }

            mediaPlayer?.start()
            promise?.resolve(null)
            startMediaPositionPolling()
            mediaPlayer?.setOnCompletionListener {
                  stopMediaPositionPolling()
                val params = Arguments.createMap().apply {
                    putBoolean("completePlay", true)
                }

                reactApplicationContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit("onCompleteListener", params)
            }
        } catch (e: IOException) {
            promise?.reject("AUDIO_PLAY_ERROR", "Failed to play audio: ${e.message}")
        }
    }






    override fun pauseAudio(promise: Promise?) {
        try{
            mediaPlayer?.pause()
            promise?.resolve(null)
            runnable?.let { backgroundHandler.removeCallbacks(it) }
        }catch (e: IOException){
            promise?.reject("AUDIO_PAUSE_ERROR", "Failed to pause audio: ${e.message}")
        }
    }

    override fun stopAudio(promise: Promise?) {
        try{
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            promise?.resolve(null)

        }catch (e: IOException){
            promise?.reject("AUDIO_STOP_ERROR", "Failed to stop audio: ${e.message}")
        }
    }

    override fun seekTo(position: Double, type: String, promise: Promise?) {
        try{
            var newPosition = 0.0
            if(type == "increase"){
                  newPosition = position + seekVal
                mediaPlayer?.seekTo(newPosition.toInt())
                promise?.resolve(null)
            }else if (type == "decrease"){
                 newPosition = position - seekVal
                mediaPlayer?.seekTo(newPosition.toInt())
                promise?.resolve(null)
            }else{
                newPosition = position
                mediaPlayer?.seekTo(newPosition.toInt())
                promise?.resolve(null)
            }


            val params = Arguments.createMap().apply {
                putDouble("seek", newPosition)
            }

            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onSeek", params)

        }catch (e: IOException){
            promise?.reject("AUDIO_SEEK_ERROR", "Failed to seek audio: ${e.message}")
        }
    }

    override fun playbackSpeed(speed: String?) {
        when(speed){
            "high" -> {
                val params = mediaPlayer?.playbackParams
                params!!.speed = 2.0f
                mediaPlayer?.playbackParams = params
            }
            "mid" -> {
                val params = mediaPlayer?.playbackParams
                params!!.speed = 1.5f
                mediaPlayer?.playbackParams = params
            }
            "normal" -> {
                val params = mediaPlayer?.playbackParams
                params!!.speed = 1.0f
                mediaPlayer?.playbackParams = params
            }

        }
    }

    companion object {
        const val NAME = "NativeAudioApi"
    }
}
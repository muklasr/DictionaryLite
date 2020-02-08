package com.muklas.dictionarylite.service

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.database.DatabaseContract.IdToEnColumns.Companion.TRANSLATE
import com.muklas.dictionarylite.database.DatabaseContract.IdToEnColumns.Companion.WORD
import com.muklas.dictionarylite.database.IdToEnHelper
import com.muklas.dictionarylite.model.Word
import com.muklas.dictionarylite.preference.AppPreference
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.CoroutineContext

class DataManagerService : Service(), CoroutineScope {

    private var activityMessenger: Messenger? = null

    companion object {
        const val PREPARATION_MESSAGE = 0
        const val UPDATE_MESSAGE = 1
        const val SUCCESS_MESSAGE = 2
        const val FAILED_MESSAGE = 3
        const val CANCEL_MESSAGE = 4
        const val ACTIVITY_HANDLER = "activity_handler"
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate() {
        super.onCreate()
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        activityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER)
        //proses ambil data
        loadDataAsync()
        return activityMessenger.let { it!!.binder }
    }

    override fun onUnbind(intent: Intent): Boolean {
        job.cancel()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }

    private fun loadDataAsync() {
        sendMessage(PREPARATION_MESSAGE)
        job = launch {
            val isInsertSuccess = async(Dispatchers.IO) {
                getData()
            }
            if (isInsertSuccess.await()) {
                sendMessage(SUCCESS_MESSAGE)
            } else {
                sendMessage(FAILED_MESSAGE)
            }
        }
        job.start()
    }

    private fun sendMessage(messageStatus: Int) {
        val message = Message.obtain(null, messageStatus)
        try {
            activityMessenger?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun getData(): Boolean {
        val idToEnHelper = IdToEnHelper.getInstance(applicationContext)
        val appPreference = AppPreference(applicationContext)

        val firstRun = appPreference.firstRun as Boolean
        Log.d("Checkpoint", "firstrun: $firstRun")

        if (firstRun) {
            val idToEnModels = preLoadRaw()
            idToEnHelper.open()

            var isInsertSuccess: Boolean

            try {
                for (model in idToEnModels) {
                    val values = ContentValues()
                    values.put(WORD, model.word)
                    values.put(TRANSLATE, model.translate)
                    idToEnHelper.insert(values)
                }
                isInsertSuccess = true
                appPreference.firstRun = false

                Log.d("Checkpoint", "INSERT SUCCESS")
            } catch (e: Exception) {
                isInsertSuccess = false
                Log.d("Checkpoint", "INSERT FAILED")
            }

            return isInsertSuccess
        } else {
            try {
                synchronized(this) {
                    return true
                }
            } catch (e: Exception) {
                return false
            }
        }
    }

    private fun preLoadRaw(): ArrayList<Word> {
        val words = ArrayList<Word>()
        var line: String?
        val reader: BufferedReader
        try {
            val rawText = resources.openRawResource(R.raw.data_iden)

            reader = BufferedReader(InputStreamReader(rawText))
            do {
                line = reader.readLine()
                val splitstr =
                    line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val word: Word

                word = Word(0, splitstr[0], splitstr[1])
                words.add(word)
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return words
    }
}

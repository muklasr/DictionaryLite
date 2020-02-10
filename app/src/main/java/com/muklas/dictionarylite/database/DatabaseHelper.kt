package com.muklas.dictionarylite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.sql.SQLException


internal class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dictionarydb.db"
        private const val DATABASE_VERSION = 1
        private const val DB_PATH = "data/data/com.muklas,dictionarylite/databases/"
    }

    private var myDB: SQLiteDatabase? = null

    private fun checkDataBase(): Boolean {
        var tempDB: SQLiteDatabase? = null
        try {
            val myPath: String = DB_PATH + DATABASE_NAME
            tempDB =  SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        tempDB?.close()
        return tempDB != null
    }

    @Throws(IOException::class)
    fun copyDataBase() {
        try {
            val myInput: InputStream = context.assets.open(DATABASE_NAME)
            val outputFileName: String = DB_PATH + DATABASE_NAME
            val myOutput: OutputStream = FileOutputStream(outputFileName)
            val buffer = ByteArray(1024)
            var length: Int
            while (myInput.read(buffer).also { length = it } > 0) {
                myOutput.write(buffer, 0, length)
            }
            myOutput.flush()
            myOutput.close()
            myInput.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    fun openDataBase() {
        val myPath: String = DB_PATH + DATABASE_NAME
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist = checkDataBase()
        if (!dbExist) {
            this.readableDatabase
            try {
                copyDataBase()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        onCreate(db)
    }
}
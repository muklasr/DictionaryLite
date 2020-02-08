package com.muklas.dictionarylite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.TABLE_NAME
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.WORD
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion._ID

class EnToIdHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: EnToIdHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): EnToIdHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = EnToIdHelper(context)
                    }
                }
            }
            return INSTANCE as EnToIdHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$WORD ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = $id",
            null,
            null,
            null,
            null
        )
    }

    fun queryByWord(word: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$WORD LIKE '%$word%'",
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
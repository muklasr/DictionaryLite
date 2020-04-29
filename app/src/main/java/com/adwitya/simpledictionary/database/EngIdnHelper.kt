package com.adwitya.simpledictionary.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.adwitya.simpledictionary.database.DatabaseContract.EnToIdColumns.Companion.TABLE_NAME
import com.adwitya.simpledictionary.database.DatabaseContract.EnToIdColumns.Companion.WORD
import com.adwitya.simpledictionary.database.DatabaseContract.EnToIdColumns.Companion.ID

class EngIdnHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
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

    fun queryByWord(word: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$WORD LIKE '$word%'",
            null,
            null,
            null,
            null
        )
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
    }
}
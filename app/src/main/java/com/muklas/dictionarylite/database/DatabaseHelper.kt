package com.muklas.dictionarylite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dictionarydb"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_EN_TO_ID =
            "CREATE TABLE ${DatabaseContract.EnToIdColumns.TABLE_NAME}" +
                    " (${DatabaseContract.EnToIdColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " ${DatabaseContract.EnToIdColumns.WORD} TEXT NOT NULL," +
                    " ${DatabaseContract.EnToIdColumns.TRANSLATE} TEXT NOT NULL)"
        private const val SQL_CREATE_TABLE_ID_TO_EN =
            "CREATE TABLE ${DatabaseContract.IdToEnColumns.TABLE_NAME}" +
                    " (${DatabaseContract.EnToIdColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " ${DatabaseContract.EnToIdColumns.WORD} TEXT NOT NULL," +
                    " ${DatabaseContract.EnToIdColumns.TRANSLATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_EN_TO_ID)
        db?.execSQL(SQL_CREATE_TABLE_ID_TO_EN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.EnToIdColumns.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.IdToEnColumns.TABLE_NAME}")
        onCreate(db)
    }
}
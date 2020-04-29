package com.adwitya.simpledictionary.helper

import android.database.Cursor
import com.adwitya.simpledictionary.model.Word
import com.adwitya.simpledictionary.database.DatabaseContract.IdToEnColumns.Companion.TRANSLATE
import com.adwitya.simpledictionary.database.DatabaseContract.IdToEnColumns.Companion.WORD
import com.adwitya.simpledictionary.database.DatabaseContract.IdToEnColumns.Companion.ID

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor): ArrayList<Word> { //function for convert from cursor to array list
        val list = ArrayList<Word>()
        while (cursor.moveToNext()) {
            val id =
                cursor.getInt(cursor.getColumnIndexOrThrow(ID))
            val word =
                cursor.getString(cursor.getColumnIndexOrThrow(WORD))
            val translate =
                cursor.getString(cursor.getColumnIndexOrThrow(TRANSLATE))
            list.add(
                Word(
                    id,
                    word,
                    translate
                )
            )
        }
        return list
    }

}
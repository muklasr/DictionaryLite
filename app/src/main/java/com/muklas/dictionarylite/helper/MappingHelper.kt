package com.muklas.dictionarylite.helper

import android.database.Cursor
import com.muklas.dictionarylite.model.Word
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.TRANSLATE
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion.WORD
import com.muklas.dictionarylite.database.DatabaseContract.EnToIdColumns.Companion._ID

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor): ArrayList<Word> {
        val list = ArrayList<Word>()
        while (cursor.moveToNext()) {
            val id =
                cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
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
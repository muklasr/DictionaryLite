package com.muklas.dictionarylite.database

import android.provider.BaseColumns


internal class DatabaseContract{
    internal class IdToEnColumns: BaseColumns {
        companion object{
            const val TABLE_NAME = "id_to_en"
            const val _ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }
    internal class EnToIdColumns: BaseColumns {
        companion object{
            const val TABLE_NAME = "en_to_id"
            const val _ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }
}
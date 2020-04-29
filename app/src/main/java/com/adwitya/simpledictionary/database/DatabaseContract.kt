package com.adwitya.simpledictionary.database

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class IdToEnColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "id_to_en" //table for store the default word list (Indonesian to English)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }

    internal class EnToIdColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "en_to_id" //table for store the default word list (English to Indonesian)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }

    internal class FavEnIdColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "fav_en_id" //table for store the favorite word list (Indonesian to English)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }

    internal class FavIdEnColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "fav_id_en" //table for store the favorite word list (English to Indonesian)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }
    internal class MyEnIdColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "my_en_id" //table for store the custom word list (Indonesian to English)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }

    internal class MyIdEnColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "my_id_en" //table for store the custom word list (English to Indonesian)
            const val ID = "id"
            const val WORD = "word"
            const val TRANSLATE = "translate"
        }
    }
}
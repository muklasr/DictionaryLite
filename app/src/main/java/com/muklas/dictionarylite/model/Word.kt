package com.muklas.dictionarylite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word (
    val id: Int,
    val word: String,
    val translate: String
):Parcelable
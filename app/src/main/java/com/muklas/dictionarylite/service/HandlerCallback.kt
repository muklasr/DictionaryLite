package com.muklas.dictionarylite.service

interface HandlerCallback {
    fun onPreparation()
    fun updateProgress(progress: Long)
    fun loadSuccess()
    fun loadFailed()
    fun loadCancel()
}
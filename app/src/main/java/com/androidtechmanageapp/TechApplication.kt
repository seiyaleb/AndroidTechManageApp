package com.androidtechmanageapp

import android.app.Application
import com.androidtechmanageapp.model.TechRepository
import com.androidtechmanageapp.model.TechRoomDatabase

class TechApplication : Application(){

    //リポジトリとRoomデータベースをインスタンス化
    private val dataBase by lazy { TechRoomDatabase.getDatabase(this) }
    val repository by lazy { TechRepository(dataBase.techDao()) }
}
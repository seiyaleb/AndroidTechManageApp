package com.androidtechmanageapp.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tech::class, URL::class], version = 1, exportSchema = false)
abstract class TechRoomDatabase: RoomDatabase() {
    abstract fun techDao():TechDao
}
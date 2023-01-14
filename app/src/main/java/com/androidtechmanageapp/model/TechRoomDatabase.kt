package com.androidtechmanageapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tech::class, URL::class], version = 1, exportSchema = false)
abstract class TechRoomDatabase: RoomDatabase() {

    abstract fun techDao():TechDao

    //シングルトンで定義
    companion object {
        @Volatile
        private var INSTANCE: TechRoomDatabase? = null

        //INSTANCEがnullでない場合はそれを返し、もしnullであればデータベースを作成
        fun getDatabase(context: Context): TechRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TechRoomDatabase::class.java,
                    "tech_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
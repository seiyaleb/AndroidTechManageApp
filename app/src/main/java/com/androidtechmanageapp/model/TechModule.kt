package com.androidtechmanageapp.model

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//RoomDatabaseの構築とDaoの実装
@Module
@InstallIn(SingletonComponent::class)
object TechModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, TechRoomDatabase::class.java, "tech_database").build()

    @Singleton
    @Provides
    fun provideDao(db: TechRoomDatabase) = db.techDao()
}
package com.androidtechmanageapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["title"], unique = true)])
data class Tech(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title:String,
    var detail:String?,
    var category:String
)

@Entity(foreignKeys = [ForeignKey(
    entity = Tech::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("tech_id"),
    onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["tech_id"])]
)
data class URL(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val tech_id:Int,
    var url:String?
)



package com.androidtechmanageapp.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

//TechクラスとURLクラスの中間クラス
@Parcelize
class TechAndURL : Parcelable {
    @IgnoredOnParcel
    @Embedded
    lateinit var tech: Tech

    @IgnoredOnParcel
    @Relation(parentColumn = "id", entityColumn = "tech_id")
    lateinit var urls: List<URL>
}
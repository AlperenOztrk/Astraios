package com.wiuma.astraios.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var Id: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "birthday")
    val birthday: String?,

    @ColumnInfo(name = "horoscope")
    val horoscope: String?,

    @ColumnInfo(name = "age")
    val age: String?,

    @ColumnInfo(name = "email")
    val email: String?,
) {
}
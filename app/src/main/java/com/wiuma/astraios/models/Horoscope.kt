package com.wiuma.astraios.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Horoscope(
    @PrimaryKey
    @SerializedName("id")
    var Id: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val nameHoroscope: String?,

    @ColumnInfo(name = "signURL")
    @SerializedName("signURL")
    val signHoroscope: String?,

    @ColumnInfo(name = "shadowSignURL")
    @SerializedName("shadowSignURL")
    val shadowSignHoroscope: String?,

    @ColumnInfo(name = "dailyComment")
    @SerializedName("dailyComment")
    val dailyComment: String?,

    @ColumnInfo(name = "starts")
    @SerializedName("starts")
    val startDate: String?,

    @ColumnInfo(name = "ends")
    @SerializedName("ends")
    val endDate: String?,

    @ColumnInfo(name = "planet")
    @SerializedName("planet")
    val namePlanet: String?,

    @ColumnInfo(name = "planetSign")
    @SerializedName("planetSign")
    val signPlanet: String?,

    @ColumnInfo(name = "element")
    @SerializedName("element")
    val nameElement: String?,

    @ColumnInfo(name = "elementSign")
    @SerializedName("elementSign")
    val signElement: String?,

    @ColumnInfo(name = "highlights")
    @SerializedName("highlights")
    val highlightOfHoroscope: String?,

    @ColumnInfo(name = "characteristics")
    @SerializedName("characteristics")
    val characterOfHoroscope: String?,

    @ColumnInfo(name = "inkSign")
    @SerializedName("inkSign")
    val InkSign: String?
) {
}
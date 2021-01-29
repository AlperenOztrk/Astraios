package com.wiuma.astraios.models

import com.google.gson.annotations.SerializedName

data class Horoscope(
    @SerializedName("id")
    val idHoroscope: Int?,
    @SerializedName("name")
    val nameHoroscope: String?,
    @SerializedName("signURL")
    val signHoroscope: String?,
    @SerializedName("shadowSignURL")
    val shadowSignHoroscope: String?,
    @SerializedName("dailyComment")
    val dailyComment: String?,
    @SerializedName("starts")
    val startDate: String?,
    @SerializedName("ends")
    val endDate: String?,
    @SerializedName("planet")
    val namePlanet: String?,
    @SerializedName("planetSign")
    val signPlanet: String?,
    @SerializedName("element")
    val nameElement: String?,
    @SerializedName("elementSign")
    val signElement: String?,
    @SerializedName("characteristics")
    val characterOfHoroscope: String?,
    @SerializedName("inkSign")
    val inkSignHoroscope: String?
) {
}
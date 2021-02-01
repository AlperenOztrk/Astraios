package com.wiuma.astraios.utilities

import java.util.*

fun findAge(year: Int, month: Int, day: Int): String {
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.set(year, month, day)
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    val finalAge = age
    return finalAge.toString()
}

fun findHoroscope(month: Int, day: Int): Int {
    var horoscopeGlobal = 0
    when (month) {
        0 -> {
            horoscopeGlobal = if (day <= 20) {
                0
            } else {
                1
            }
        }
        1 -> {
            horoscopeGlobal = if (day <= 19) {
                1
            } else {
                2
            }
        }
        2 -> {
            horoscopeGlobal = if (day <= 20) {
                2
            } else {
                3
            }
        }
        3 -> {
            horoscopeGlobal = if (day <= 20) {
                3
            } else {
                4
            }
        }
        4 -> {
            horoscopeGlobal = if (day <= 20) {
                4
            } else {
                5
            }
        }
        5 -> {
            horoscopeGlobal = if (day <= 21) {
                5
            } else {
                6
            }
        }
        6 -> {
            horoscopeGlobal = if (day <= 22) {
                6
            } else {
                7
            }
        }
        7 -> {
            horoscopeGlobal = if (day <= 23) {
                7
            } else {
                8
            }
        }
        8 -> {
            horoscopeGlobal = if (day <= 23) {
                8
            } else {
                9
            }
        }
        9 -> {
            horoscopeGlobal = if (day <= 23) {
                9
            } else {
                10
            }
        }
        10 -> {
            horoscopeGlobal = if (day <= 22) {
                10
            } else {
                11
            }
        }
        11 -> {
            horoscopeGlobal = if (day <= 21) {
                11
            } else {
                0
            }
        }
    }
    return horoscopeGlobal
}
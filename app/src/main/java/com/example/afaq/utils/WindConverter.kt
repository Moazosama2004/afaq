package com.example.afaq.utils

object WindConverter {

    fun convert(meterPerSec: Double, unit: String): String {
        val converted = when (unit) {
            "meter/sec" -> "${meterPerSec.toInt()} m/s"
            "mile/hour" -> "${meterToMile(meterPerSec).toInt()} mph"
            else -> "${meterPerSec.toInt()} m/s"
        }
        return converted.localizeDigits()
    }

    private fun meterToMile(meterPerSec: Double): Double = meterPerSec * 2.237
}
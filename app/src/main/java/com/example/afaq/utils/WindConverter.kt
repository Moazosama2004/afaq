package com.example.afaq.utils

object WindConverter {

    fun convert(meterPerSec: Double, unit: String): String {
        return when (unit) {
            "meter/sec" -> "${meterPerSec.toInt()} m/s"
            "mile/hour" -> "${meterToMile(meterPerSec).toInt()} mph"
            else -> "${meterPerSec.toInt()} m/s"
        }
    }

    private fun meterToMile(meterPerSec: Double): Double = meterPerSec * 2.237
}
package com.example.afaq.services.notification

interface NotificationService {
    fun showNotification(message: String = "Weather Alert! 🌤️")
    fun showAlarm(message: String)
}
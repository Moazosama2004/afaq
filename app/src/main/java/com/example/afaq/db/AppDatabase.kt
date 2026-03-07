package com.example.afaq.db

import android.content.Context
import androidx.annotation.DisplayContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// TODO : ADD DATABASE ANNOTATION
//@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase  : RoomDatabase(){
    // TODO : ADD DAOS

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance (context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance  = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "afaq_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
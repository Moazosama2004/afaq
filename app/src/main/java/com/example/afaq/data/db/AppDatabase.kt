package com.example.afaq.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.afaq.data.alarm.datasource.local.AlertDao
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.data.local.db.FavouriteDao
import com.example.afaq.data.local.db.FavouriteEntity

@Database(
    entities = [FavouriteEntity::class, AlertEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance (context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance  = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "afaq_db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
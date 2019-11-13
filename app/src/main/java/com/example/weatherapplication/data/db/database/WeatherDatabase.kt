package com.example.weatherapplication.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapplication.data.db.database.current.Current
import com.example.weatherapplication.data.db.database.current.CurrentDao
import com.example.weatherapplication.data.db.database.location.WeatherLocation
import com.example.weatherapplication.data.db.database.location.WeatherLocationDao

@Database(entities = [Current::class, WeatherLocation::class],version = 1)
@TypeConverters(Converter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentDao(): CurrentDao
    abstract fun locationDao(): WeatherLocationDao

    companion object{
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, "forecasts.db").build()
    }
}
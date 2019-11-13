package com.example.weatherapplication.data.db.database.current


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_ID = 0

@Entity(tableName = "current_weather")
data class Current(

    val feelslike: Double,

    @SerializedName("observation_time")
    val observationTime: String,

    val pressure: Double,

    @SerializedName("temperature")
    val temparature: Int,

    @SerializedName("weather_descriptions")
    val weatherDescription: List<String>,

    @SerializedName("uv_index")
    val uvIndex: Double,

    @SerializedName("weather_icons")
    val weatherIcons: List<String>,

    @SerializedName("wind_speed")
    val windSpeed: Double
)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_ID
}
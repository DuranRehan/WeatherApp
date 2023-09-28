package g56055.mobg.meteoapp.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import g56055.mobg.meteoapp.fragment.home.CurrentConditionsDto
import g56055.mobg.meteoapp.fragment.home.DayDto
import g56055.mobg.meteoapp.fragment.home.WeatherDto

@Entity(tableName = "weather_response_table")
data class WeatherResponseDatabase(
    @PrimaryKey(autoGenerate = false)
    var weather_response_id: Long,
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val description: String,
)

@Entity(tableName = "day_table", foreignKeys = [
    ForeignKey(
        entity = WeatherResponseDatabase::class,
        parentColumns = ["weather_response_id"],
        childColumns = ["weather_response_id_days"],
        onDelete = ForeignKey.CASCADE
    )
])
data class DayEntity(
    @PrimaryKey
    val datetime: String,
    val datetimeEpoch: Int,
    val description: String,
    val sunset: String,
    val sunrise: String,
    val tempmin: Double,
    val tempmax: Double,
    val weather_response_id_days: Long
)

@Entity(tableName = "current_conditions_table", foreignKeys = [
    ForeignKey(
        entity = WeatherResponseDatabase::class,
        parentColumns = ["weather_response_id"],
        childColumns = ["weather_response_id_current_conditions"],
        onDelete = ForeignKey.CASCADE
    )
])
data class CurrentConditionsEntity(
    @PrimaryKey(autoGenerate = false)
    var current_cond_id: Long,
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val precipprob: Double,
    val windspeed: Double,
    val pressure: Double,
    val visibility: Double,
    val uvindex: Int,
    val sunrise: String,
    val sunset: String,
    val weather_response_id_current_conditions: Long
)


fun WeatherResponseDatabase.asDto(): WeatherDto {
    return WeatherDto(
        latitude = latitude,
        longitude = longitude,
        resolvedAddress = resolvedAddress,
        address = address,
        timezone = timezone,
        description = description,
    )
}

fun DayEntity.asDto(): DayDto {
    return DayDto(
        datetime = datetime,
        datetimeEpoch = datetimeEpoch,
        description = description,
        sunset = sunset,
        sunrise = sunrise,
        tempmax = tempmax,
        tempmin = tempmin,
    )
}

fun CurrentConditionsEntity.asDto(): CurrentConditionsDto {
    return CurrentConditionsDto(
        temp = temp,
        feelslike = feelslike,
        humidity = humidity,
        precipprob = precipprob,
        windspeed = windspeed,
        pressure = pressure,
        visibility = visibility,
        uvindex = uvindex,
        sunrise = sunrise,
        sunset = sunset,
    )
}


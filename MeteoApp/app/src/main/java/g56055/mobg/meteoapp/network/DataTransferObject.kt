package g56055.mobg.meteoapp.network

import g56055.mobg.meteoapp.database.CurrentConditionsEntity
import g56055.mobg.meteoapp.database.DayEntity
import g56055.mobg.meteoapp.database.WeatherResponseDatabase

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val description: String,
    val days: List<Day>,
    val currentConditions: CurrentConditions
)

data class Day(
    val datetime: String,
    val datetimeEpoch: Int,
    val description: String,
    val sunset: String,
    val sunrise: String,
    val tempmin: Double,
    val tempmax: Double,
)

data class CurrentConditions(
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

)

fun WeatherResponse.asDatabaseEntity(): WeatherResponseDatabase {
    return WeatherResponseDatabase(
        weather_response_id = 1L,
        latitude = latitude,
        longitude = longitude,
        resolvedAddress = resolvedAddress,
        address = address,
        timezone = timezone,
        description = description,
    )
}

fun Day.asDatabase(): DayEntity {
    return DayEntity(
        datetime = datetime,
        datetimeEpoch = datetimeEpoch,
        description = description,
        sunset = sunset,
        sunrise = sunrise,
        tempmax = tempmax,
        tempmin = tempmin,
        weather_response_id_days = 1L
    )
}


fun WeatherResponse.asDatabaseEntityWithCurrentConditions(): CurrentConditionsEntity {
    return CurrentConditionsEntity(
        current_cond_id = 1L,
        temp = currentConditions.temp,
        feelslike = currentConditions.feelslike,
        humidity = currentConditions.humidity,
        precipprob = currentConditions.precipprob,
        windspeed = currentConditions.windspeed,
        pressure = currentConditions.pressure,
        visibility = currentConditions.visibility,
        uvindex = currentConditions.uvindex,
        sunrise = currentConditions.sunrise,
        sunset = currentConditions.sunset,
        weather_response_id_current_conditions = 1L
    )
}
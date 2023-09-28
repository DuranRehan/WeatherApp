package g56055.mobg.meteoapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import g56055.mobg.meteoapp.database.WeatherDatabase
import g56055.mobg.meteoapp.database.relations.asDto
import g56055.mobg.meteoapp.fragment.home.WeatherWithDaysAndCurrentCondDto
import g56055.mobg.meteoapp.network.WeatherNetwork
import g56055.mobg.meteoapp.network.WeatherResponse
import g56055.mobg.meteoapp.network.asDatabase
import g56055.mobg.meteoapp.network.asDatabaseEntity
import g56055.mobg.meteoapp.network.asDatabaseEntityWithCurrentConditions
import kotlinx.coroutines.runBlocking

class WeatherRepository(private val database: WeatherDatabase) {

    val weather: LiveData<WeatherWithDaysAndCurrentCondDto?> = run {
        database.dao.getWeatherWithDaysAndCurrentCond().map {
            if(it == null){
                return@map null
            }
            it.asDto()
        }
    }

    fun refreshWeatherByCity(city: String, key: String) {
        runBlocking {
            val weatherResponse = WeatherNetwork.weather.getWeatherByCity(city, key)
            deleteWeather()
            insertWeatherWithDaysAndCurrentConditions(weatherResponse)
        }


    }

    fun refreshWeatherByCoordinates(lat: Double, long: Double, key: String) {
        runBlocking {
            val weatherResponse = WeatherNetwork.weather.getWeatherByCoordinates(lat, long, key)
            deleteWeather()
            insertWeatherWithDaysAndCurrentConditions(weatherResponse)
        }
    }

    private fun deleteWeather() {

        database.dao.deleteDay()
        database.dao.deleteCurrentConditions()
        database.dao.deleteWeatherResponse()


    }

    private fun insertWeatherWithDaysAndCurrentConditions(weatherResponse: WeatherResponse) {
        database.dao.insertWeatherResponse(weatherResponse.asDatabaseEntity())
        database.dao.insertCurrentConditions(weatherResponse.asDatabaseEntityWithCurrentConditions())
        weatherResponse.days.map { day ->
            database.dao.insertDay(day.asDatabase())
        }


    }


}
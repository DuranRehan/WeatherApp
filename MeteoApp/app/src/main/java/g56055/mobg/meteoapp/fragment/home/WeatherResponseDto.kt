package g56055.mobg.meteoapp.fragment.home


data class WeatherWithDaysAndCurrentCondDto(
    val weather : WeatherDto,
    val currentConditions: CurrentConditionsDto,
    val days: List<DayDto>
)
data class WeatherDto(
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val description: String,
)
data class DayDto(
    val datetime: String,
    val datetimeEpoch: Int,
    val description: String,
    val sunset: String,
    val sunrise: String,
    val tempmin: Double,
    val tempmax: Double,
)

data class CurrentConditionsDto(
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val precipprob: Double,
    val windspeed: Double,
    val pressure: Double,
    val visibility: Double,
    val uvindex: Int,
    val sunset: String,
    val sunrise: String,
)


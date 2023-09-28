package g56055.mobg.meteoapp.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import g56055.mobg.meteoapp.database.CurrentConditionsEntity
import g56055.mobg.meteoapp.database.DayEntity
import g56055.mobg.meteoapp.database.WeatherResponseDatabase
import g56055.mobg.meteoapp.database.asDto
import g56055.mobg.meteoapp.fragment.home.WeatherWithDaysAndCurrentCondDto

data class WeatherWithDaysAndCurrentCond(
    @Embedded val weatherResponseDatabase: WeatherResponseDatabase,
    @Relation(
        parentColumn = "weather_response_id",
        entityColumn = "weather_response_id_days"
    )
    val days: List<DayEntity>,
    @Relation(
        parentColumn = "weather_response_id",
        entityColumn = "weather_response_id_current_conditions"
    )
    val currentConditions: CurrentConditionsEntity,
)

fun WeatherWithDaysAndCurrentCond.asDto() : WeatherWithDaysAndCurrentCondDto{
    return WeatherWithDaysAndCurrentCondDto(
        weather = weatherResponseDatabase.asDto(),
        days = days.map { it.asDto() },
        currentConditions = currentConditions.asDto()
    )
}

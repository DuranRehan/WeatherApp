package g56055.mobg.meteoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import g56055.mobg.meteoapp.database.relations.WeatherWithDaysAndCurrentCond

@Dao
interface Dao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM User WHERE email= :email")
    fun getEmail(email: String): User?

    @Query("SELECT email FROM User")
    fun getAllEmails(): List<String>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherResponse(weatherResponse: WeatherResponseDatabase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentConditions(currentConditions: CurrentConditionsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDay(day: DayEntity)

    @Query("DELETE FROM weather_response_table")
    fun deleteWeatherResponse()

    @Query("DELETE FROM current_conditions_table")
    fun deleteCurrentConditions()

    @Query("DELETE FROM day_table")
    fun deleteDay()

    @Transaction
    @Query("SELECT * FROM weather_response_table")
    fun getWeatherWithDaysAndCurrentCond(): LiveData<WeatherWithDaysAndCurrentCond?>

    @Query("SELECT * FROM history_table ORDER BY DATE desc")
    fun getHistory(): LiveData<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: History)
}
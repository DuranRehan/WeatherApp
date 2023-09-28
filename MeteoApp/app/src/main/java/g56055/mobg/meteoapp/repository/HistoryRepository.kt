package g56055.mobg.meteoapp.repository

import androidx.lifecycle.LiveData
import g56055.mobg.meteoapp.database.History
import g56055.mobg.meteoapp.database.WeatherDatabase

class HistoryRepository(private val database: WeatherDatabase) {

        fun insertHistory(history: History) {
            database.dao.insertHistory(history)
        }

        fun getHistory(): LiveData<List<History>> {
            return database.dao.getHistory()
        }
}
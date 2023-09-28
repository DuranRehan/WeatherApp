package g56055.mobg.meteoapp.fragment.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import g56055.mobg.meteoapp.database.History
import g56055.mobg.meteoapp.database.WeatherDatabase.Companion.getInstance
import g56055.mobg.meteoapp.repository.HistoryRepository
import java.text.DateFormat

class SearchViewModel(application: Application) : ViewModel() {
    private val histoRepo = HistoryRepository(getInstance(application))

    private val _eventSearch = MutableLiveData<Boolean>()
    val eventSearch: LiveData<Boolean>
        get() = _eventSearch

    init {
        _eventSearch.value = false
    }

    fun onSearch() {

        _eventSearch.value = true
    }

    fun onSearchComplete() {
        _eventSearch.value = false
    }

    fun insertHistory(city: String) {
        val date = DateFormat.getDateInstance().format(System.currentTimeMillis())
        val history= History(0, city, date)
        histoRepo.insertHistory(history)
    }
    fun getHistory() = histoRepo.getHistory()
}

class SearchViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
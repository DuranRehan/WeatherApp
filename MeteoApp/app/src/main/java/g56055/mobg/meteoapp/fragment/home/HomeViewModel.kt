package g56055.mobg.meteoapp.fragment.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import g56055.mobg.meteoapp.BuildConfig
import g56055.mobg.meteoapp.database.WeatherDatabase.Companion.getInstance
import g56055.mobg.meteoapp.repository.WeatherRepository

class HomeViewModel(
    application: Application,
    private var city: String,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {
    private val weatherRepo = WeatherRepository(getInstance(application))
    private val key = BuildConfig.API_KEY
    val weatherResponse = weatherRepo.weather
    private var _eventShare = MutableLiveData<Boolean>()
    val eventShare: LiveData<Boolean>
        get() = _eventShare

    init {
        if (city != "") {
            getWeatherByCity()
        } else {
            getWeatherByCoordinates()
        }
        Log.i("HomeViewModel", "HomeViewModel: Init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeViewModel", "HomeViewModel: Destroyed")
        city = ""
    }

    private fun getWeatherByCity() {
        try {
            weatherRepo.refreshWeatherByCity(city, key)
        } catch (e: Exception) {
            Log.i("HomeViewModel", "RefreshResult: $e.s")
        }

    }

    @Suppress("MissingPermission")
    private fun getWeatherByCoordinates() {
        val currentLocationRequest: CurrentLocationRequest = CurrentLocationRequest.Builder()
            .setGranularity(Granularity.GRANULARITY_FINE)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setDurationMillis(5000)
            .setMaxUpdateAgeMillis(0)
            .build()
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            currentLocationRequest,
            cancellationTokenSource.token
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                val location = it.result
                if (location != null) {
                    try {
                        weatherRepo.refreshWeatherByCoordinates(
                            location.latitude,
                            location.longitude,
                            key
                        )
                        Log.i(
                            "HomeViewModel",
                            "RefreshResult: ${weatherResponse.value?.currentConditions}"
                        )
                    } catch (e: Exception) {
                        Log.i("HomeViewModel", "RefreshResult: $e.s")
                    }

                    Log.d("HomeViewModel", "lat: ${location.latitude}, lon: ${location.longitude}")
                }
            } else {
                it.exception?.printStackTrace()
            }
        }
    }

    fun onShare() {
        _eventShare.value = true
    }

    fun onShareComplete() {
        _eventShare.value = false
    }

}


class HomeViewModelFactory(
    private val application: Application,
    private val city: String,
    private val fusedLocationClient: FusedLocationProviderClient
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application, city, fusedLocationClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
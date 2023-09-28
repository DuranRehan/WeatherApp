package g56055.mobg.meteoapp.fragment.home

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import g56055.mobg.meteoapp.R
import g56055.mobg.meteoapp.databinding.FragmentHomeBinding
import java.util.Locale


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val args = HomeFragmentArgs.fromBundle(requireArguments())
        val application = requireNotNull(this.activity).application
        viewModelFactory = HomeViewModelFactory(application, args.city, fusedLocationClient)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = HomeAdapter()
        binding.previsionDay.adapter = adapter


        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            updateWeather()
            it?.let {
                adapter.data = it.days
            }
        }
        viewModel.eventShare.observe(viewLifecycleOwner){hasShare ->
            if(hasShare){
                shareWeather()
            }

        }
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        binding.previsionDay.layoutManager = manager
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun updateWeather() {
        val weatherResponse = viewModel.weatherResponse
        if (weatherResponse.value != null) {
            showAllAndHideProgressBar()
            val current = weatherResponse.value!!.currentConditions
            val weather = weatherResponse.value!!.weather
            val days = weatherResponse.value!!.days

            binding.cityName.text =
                getCityNameFromLocation(weather.latitude, weather.longitude).uppercase()
            binding.description.text = weather.description
            binding.description.text = weather.description
            binding.sunrise.text = current.sunrise
            binding.sunset.text = current.sunset
            binding.tempMinMax.text = getString(
                R.string.temp_min_max_format, days[0].tempmin.toString(), days[0].tempmax.toString()
            )
            binding.actualTemp.text = getString(R.string.temp_format, current.temp.toString())
            binding.humidity.text = getString(R.string.percent_format, current.humidity.toString())
            binding.uvIndex.text = current.uvindex.toString()
            binding.precipProb.text =
                getString(R.string.percent_format, current.precipprob.toString())
            binding.wind.text = getString(R.string.wind_format, current.windspeed.toString())
            binding.pressure.text = getString(R.string.pressure_format, current.pressure.toString())
            binding.visibility.text =
                getString(R.string.visibility_format, current.visibility.toString())
            binding.feels.text = getString(R.string.feels_format, current.feelslike.toString())
        }
    }


    private fun showAllAndHideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.forecastLabel.visibility = View.VISIBLE
        binding.cityName.visibility = View.VISIBLE
        binding.description.visibility = View.VISIBLE
        binding.sunrise.visibility = View.VISIBLE
        binding.sunset.visibility = View.VISIBLE
        binding.tempMinMax.visibility = View.VISIBLE
        binding.actualTemp.visibility = View.VISIBLE
        binding.humidity.visibility = View.VISIBLE
        binding.uvIndex.visibility = View.VISIBLE
        binding.precipProb.visibility = View.VISIBLE
        binding.wind.visibility = View.VISIBLE
        binding.pressure.visibility = View.VISIBLE
        binding.visibility.visibility = View.VISIBLE
        binding.feels.visibility = View.VISIBLE
        binding.previsionDay.visibility = View.VISIBLE
        binding.shareBtn.visibility = View.VISIBLE
    }
    private fun getCityNameFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        var cityName = ""

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            cityName = addresses?.get(0)?.locality ?: ""

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cityName
    }

    private fun getShareIntent() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_btn_text,binding.cityName.text,binding.actualTemp.text))
        return shareIntent
    }

    private fun shareWeather() {
        startActivity(getShareIntent())
        viewModel.onShareComplete()
    }
}
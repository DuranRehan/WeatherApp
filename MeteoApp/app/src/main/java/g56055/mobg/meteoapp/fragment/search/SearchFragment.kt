package g56055.mobg.meteoapp.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import g56055.mobg.meteoapp.R
import g56055.mobg.meteoapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        viewModelFactory = SearchViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        binding.searchViewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = SearchAdapter()
        binding.seachHistory.adapter = adapter

        viewModel.eventSearch.observe(viewLifecycleOwner) { hasSearch ->
            if (hasSearch) search()
        }
        val history = viewModel.getHistory()
        history.observe(viewLifecycleOwner) {
            it?.let {
                adapter.data = it
            }
        }

        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding.seachHistory.layoutManager = manager
        return binding.root
    }

    private fun search() {
        val city = binding.searchField.text.toString()
        if (city.isNotEmpty()) {
            viewModel.insertHistory(city)
            val action = SearchFragmentDirections.actionSearchFragmentToHome(city)
            NavHostFragment.findNavController(this).navigate(action)
            viewModel.onSearchComplete()
        } else if (city.isEmpty()) {
            binding.searchField.error = "Please enter a city"
        } else {
            binding.searchField.error = "Please enter a valid city"
        }
    }
}
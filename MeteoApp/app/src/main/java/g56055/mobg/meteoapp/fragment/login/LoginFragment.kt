package g56055.mobg.meteoapp.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import g56055.mobg.meteoapp.R
import g56055.mobg.meteoapp.database.WeatherDatabase
import g56055.mobg.meteoapp.databinding.FragmentLoginBinding
import g56055.mobg.meteoapp.repository.UserRepository


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val dao = WeatherDatabase.getInstance(application).dao
        val repository = UserRepository(dao)
        viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventUserLogin.observe(viewLifecycleOwner) { hasLogin ->
            if (hasLogin) {
                loginUser()
            }

        }
        viewModel.emails.observe(viewLifecycleOwner) { emails ->
            val adapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                emails
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.emailsSearch.setAdapter(adapter)
        }

        return binding.root
    }

    private fun loginUser() {
        val email = binding.emailsSearch.text.toString()
        if (viewModel.isValidEmail(email)) {
            viewModel.registerEmail(email)
            val action = LoginFragmentDirections.actionLoginFragmentToHome("")
            NavHostFragment.findNavController(this).navigate(action)
            viewModel.onLoginFinishComplete()
        } else {
            binding.emailsSearch.error = getString(R.string.invalid_email)
        }
    }

}
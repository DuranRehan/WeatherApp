package g56055.mobg.meteoapp.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import g56055.mobg.meteoapp.database.User
import g56055.mobg.meteoapp.repository.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginViewModel(userRepository: UserRepository) : ViewModel() {
    private val repository = userRepository

    // LiveData
    private val _eventUserLogin = MutableLiveData<Boolean>()
    val eventUserLogin: LiveData<Boolean>
        get() = _eventUserLogin

    private val _emails = MutableLiveData<List<String>>()
    val emails: LiveData<List<String>>
        get() = _emails

    init {

        _eventUserLogin.value = false
        _emails.value = repository.getAllEmails()
    }

    fun onLoginFinish() {
        _eventUserLogin.value = true
    }

    fun onLoginFinishComplete() {
        _eventUserLogin.value = false
    }


    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun registerEmail(email: String) {
        val date = formatDate(LocalDateTime.now())
        if (repository.getEmail(email) == null) {
            val user = User(email = email, date = date)
            repository.insert(user)
        } else {
            val user = repository.getEmail(email)
            if (user != null) {
                user.date = date
                repository.update(user)
            }
        }
        _emails.value = repository.getAllEmails()
    }

    private fun formatDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return date.format(formatter)
    }
}
package g56055.mobg.meteoapp.repository

import g56055.mobg.meteoapp.database.Dao
import g56055.mobg.meteoapp.database.User

class UserRepository(userDao: Dao) {
    private val dao = userDao
    fun insert(user: User) {
        try {
            dao.insert(user)

        } catch (e: Exception) {
            println(e)
        }
    }

    fun update(user: User) {
        try {
            dao.update(user)

        } catch (e: Exception) {
            println(e)
        }
    }

    fun getEmail(email: String): User? {
        return try {
            dao.getEmail(email)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    fun getAllEmails(): List<String> {
        return try {
            dao.getAllEmails()

        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

}

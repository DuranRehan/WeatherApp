package g56055.mobg.meteoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "email", index = true)
    val email: String,
    @ColumnInfo(name = "date")
    var date: String
)

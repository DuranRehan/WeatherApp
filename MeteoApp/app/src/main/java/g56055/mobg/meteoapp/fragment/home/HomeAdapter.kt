package g56055.mobg.meteoapp.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import g56055.mobg.meteoapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var data = listOf<DayDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class HomeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val day: TextView = itemView.findViewById(R.id.dayTime)
        private val temp: TextView = itemView.findViewById(R.id.dayTemp)
        private val dayTempMinMax: TextView = itemView.findViewById(R.id.dayTemp_min_max)

        fun bind(item: DayDto) {
            day.text = transformDate(item.datetime)
            temp.text = temp.context.getString(R.string.temp_format, item.tempmin.toString())
            dayTempMinMax.text = dayTempMinMax.context.getString(
                R.string.temp_min_max_format,
                item.tempmin.toString(),
                item.tempmax.toString()
            )
        }

        private fun transformDate(date:String): String {
            val currentLocale = Locale.getDefault()
            if (date == SimpleDateFormat("yyyy-MM-dd",currentLocale).format(System.currentTimeMillis())) {
                return "Today"
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd",currentLocale)
            val parsedDate = dateFormat.parse(date)
            val dayFormat = SimpleDateFormat("EEEE",currentLocale)
            return dayFormat.format(parsedDate as Date)
        }

        companion object {
            fun from(parent: ViewGroup): HomeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.item_day, parent, false)
                return HomeViewHolder(view)
            }
        }
    }
}
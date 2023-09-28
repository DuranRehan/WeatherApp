package g56055.mobg.meteoapp.fragment.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import g56055.mobg.meteoapp.R
import g56055.mobg.meteoapp.database.History

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    var data = listOf<History>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class SearchViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val city: TextView = itemView.findViewById(R.id.city_history)
        private val date: TextView = itemView.findViewById(R.id.date_history)
        fun bind(item: History) {
            city.text = item.city
            date.text = item.date
        }

        companion object {
            fun from(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.item_history, parent, false)
                return SearchViewHolder(view)
            }
        }
    }
}

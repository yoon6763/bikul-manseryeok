package com.bikulwon.manseryeok.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.databinding.ItemLocationBinding
import com.bikulwon.manseryeok.models.TimeZone
import kotlin.math.abs


class LocationAdapter(
    private val context: Context,
    private val items: Array<TimeZone>,
) :
    RecyclerView.Adapter<LocationAdapter.Holder>() {
    private val TAG = "LocationAdapter"

    var onLocationClickListener: OnLocationClickListener? = null

    interface OnLocationClickListener {
        fun onLocationClick(location: String, timeDiff: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_location, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvItemLocation.text = items[position].location
            val time = items[position].timeDiff

            val hour = abs(time) / 60
            val min = abs(time) % 60

            val sign = when {
                time == 0 -> "0분"
                time > 0 -> "+"
                time < 0 -> "-"
                else -> ""
            }

            val hourText = if(hour == 0) "" else "${hour}시간"
            val minText = if(min == 0) "" else " ${min}분"

            Log.d(TAG, "onBindViewHolder: $hourText   $minText")

            tvItemTimeDiff.text = sign + hourText + minText
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemLocationBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                onLocationClickListener?.onLocationClick(
                    items[adapterPosition].location,
                    items[adapterPosition].timeDiff
                )
            }
        }
    }
}

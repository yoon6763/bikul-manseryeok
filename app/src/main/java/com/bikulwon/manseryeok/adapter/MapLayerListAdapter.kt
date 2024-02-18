package com.bikulwon.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.databinding.ItemIdxLabelBinding
import com.bikulwon.manseryeok.utils.CompassUtils

class MapLayerListAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
) :
    RecyclerView.Adapter<MapLayerListAdapter.Holder>() {

    var degree = 0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_idx_label, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            val item = items[position]

            tvIdx.text = (position + 1).toString()
            tvLabel.text = CompassUtils.getValue(degree, position + 1)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemIdxLabelBinding.bind(itemView)
    }
}
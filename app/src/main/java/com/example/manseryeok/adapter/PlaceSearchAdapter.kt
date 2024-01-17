package com.example.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemSearchPlaceBinding
import com.example.manseryeok.models.address.Juso
import com.example.manseryeok.models.naversearch.NaverSearchItem

class PlaceSearchAdapter(
    private val context: Context,
    private val naverSearchItems: ArrayList<Juso>,
) :
    RecyclerView.Adapter<PlaceSearchAdapter.Holder>() {


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_search_place, parent, false)

        return Holder(view)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            val item = naverSearchItems[position]

            tvAddress.text = item.jibunAddr
            tvZipcode.text = item.zipNo
            tvAddressRoad.text = item.roadAddr
        }
    }

    override fun getItemCount(): Int {
        return naverSearchItems.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSearchPlaceBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(
                    it, bindingAdapterPosition
                )
            }
        }
    }
}
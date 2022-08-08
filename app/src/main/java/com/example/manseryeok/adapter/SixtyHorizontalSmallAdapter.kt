package com.example.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemSixtyHorizonSmallBinding
import com.example.manseryeok.models.SixtyHorizontalItem
import java.util.*

class SixtyHorizontalSmallAdapter(
    private val context: Context,
    private val items: ArrayList<SixtyHorizontalItem>,
) :
    RecyclerView.Adapter<SixtyHorizontalSmallAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_sixty_horizon_small, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {

            val item = items[position]
            tvItemSixtyLabel.text = item.label
            tvItemSixtyTop.text = item.top
            tvItemSixtyBottom.text = item.bottom

            val rd = Random()
            val temp = rd.nextInt(5)
            val tmep2 = rd.nextInt(5)
            when (temp) {
                0 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_light_gray)
                1 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_mint)
                2 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_red)
                3 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_dark_gray)
                4 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_yellow)
            }
            when (tmep2) {
                0 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_light_gray)
                1 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_mint)
                2 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_red)
                3 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_dark_gray)
                4 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_yellow)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSixtyHorizonSmallBinding.bind(itemView)
    }
}
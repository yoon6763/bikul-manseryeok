package com.bikulwon.manseryeok.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.databinding.ItemNameBinding
import com.bikulwon.manseryeok.models.name.NameScoreItem
import java.util.*

class NameScoreAdapter(
    private val context: Context,
    private val items: ArrayList<NameScoreItem>,
) :
    RecyclerView.Adapter<NameScoreAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_name, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            val item = items[position]

            tvItemName.text = item.name.toString()
            llItemNameContainer.removeAllViews()

            item.nameScoreChildItems.forEach { nameScoreChildItem ->
                val llContainer = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                for (element in listOf(
                    nameScoreChildItem.luckTop,
                    nameScoreChildItem.ganjiTop,
                    nameScoreChildItem.nameHan,
                    nameScoreChildItem.ganjiBottom,
                    nameScoreChildItem.luckBottom,
                )) {
                    val tvItem = TextView(context).apply {
                        text = element
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        ).apply {
                            weight = 1f
                            gravity = Gravity.CENTER
                            textSize = 25f
                            typeface = Typeface.DEFAULT_BOLD
                        }
                        gravity = Gravity.CENTER
                    }
                    llContainer.addView(tvItem)
                }
                llItemNameContainer.addView(llContainer)
            }
            llItemNameContainer.invalidate()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemNameBinding.bind(itemView)
    }
}
package com.example.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemNameBinding
import com.example.manseryeok.models.NameScoreItem
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
            tvItemGanjiTop.text = item.ganjiTop
            tvItemGanjiBottom.text = item.ganjiBottom

            tvItemNameKor.text = item.name
            tvItemNameHan.text = item.nameHan

//            "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"
//            "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
//            "목", "목", "화", "화", "토", "토", "금", "금", "수", "수"

//            "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
//            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
//            "수", "토", "목", "목", "토", "화", "화", "토", "금", "금", "토", "수"

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemNameBinding.bind(itemView)
    }
}
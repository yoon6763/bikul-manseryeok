package com.bikulwon.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.databinding.ItemSixtyHorizonBinding
import com.bikulwon.manseryeok.utils.Utils
import com.bikulwon.manseryeok.models.SixtyHorizontalItem
import java.util.*

class SixtyHorizontalSmallAdapter(
    private val context: Context,
    private val items: ArrayList<SixtyHorizontalItem>,
) :
    RecyclerView.Adapter<SixtyHorizontalSmallAdapter.Holder>() {

    interface OnItemClickListener {
        fun onItemClick(year: Int)
    }

    var useItemClickEvent = false

    var selectedItemPos = -1
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_sixty_horizon, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {

            val item = items[position]
            tvItemSixtyLabel.text = item.label.toString()
            tvItemSixtyTop.text = item.top
            tvItemSixtyBottom.text = item.bottom

            if(selectedItemPos == position) {
                tvItemSixtyLabel.setTextColor(context.getColor(R.color.white))
                llItemSixty.setBackgroundResource(R.drawable.box_dark_gray)
            } else {
                tvItemSixtyLabel.setTextColor(context.getColor(R.color.black))
                llItemSixty.background = null
            }

//            "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"
//            "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
//            "목", "목", "화", "화", "토", "토", "금", "금", "수", "수"

//            "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
//            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
//            "수", "토", "목", "목", "토", "화", "화", "토", "금", "금", "토", "수"


            when (Utils.sibgan[0].indexOf(item.top)) {
                0, 1 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_mint) // 목
                2, 3 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_red) // 화
                4, 5 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_yellow) // 토
                6, 7 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_light_gray) // 금
                8, 9 -> tvItemSixtyTop.setBackgroundResource(R.drawable.box_sky) // 수
            }

            when (Utils.sibiji[0].indexOf(item.bottom)) {
                0, 11 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_sky) // 수
                1, 4, 7, 10 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_yellow) // 토
                2, 3 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_mint) // 목
                5, 6 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_red) // 화
                8, 9 -> tvItemSixtyBottom.setBackgroundResource(R.drawable.box_light_gray) // 금
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun itemClicked(position: Int) {
        if(useItemClickEvent) {
            selectedItemPos = position

            // 이전 선택한 라벨의 배경 제거
//            tvOldLabel?.background = null
//            // OldLabel 최신화
//            tvOldLabel = binding.tvItemSixtyLabel
//            // 클릭한 라벨 배경 처리
//            binding.tvItemSixtyLabel.setBackgroundResource(R.drawable.box_light_gray)
            // 리스너 발동
            onItemClickListener?.onItemClick(items[position].label)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSixtyHorizonBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                itemClicked(adapterPosition)
            }
        }
    }
}
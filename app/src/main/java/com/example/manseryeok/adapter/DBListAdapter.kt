package com.example.manseryeok.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.databinding.ItemDbListBinding
import com.example.manseryeok.models.DBListItem


class DBListAdapter(
    private val context: Context,
    private val items: ArrayList<DBListItem>,
) :
    RecyclerView.Adapter<DBListAdapter.Holder>() {
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    private val TAG = "DBListAdapter"
    private var prePosition = -1

    var onMenuClickListener: OnMenuClickListener? = null

    interface OnMenuClickListener{
        fun onSearchClick(ID: String, position: Int)
        fun onDeleteClick(ID: String, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_db_list, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            val item = items[position]

            val birth = item.birth
            val sunBirth = "${birth.substring(0,4)}.${birth.substring(4,6)}.${birth.substring(6,8)}"
            tvItemDbName.text = "${item.firstName}${item.lastName}"
            tvItemDbBirthSum.text = "(양) $sunBirth"
            tvItemDbBirthMoon.text = "(음) ${Utils.dateDotFormat.format(Utils.convertSolarToLunar(birth.substring(0, 8)))}"
            tvItemDbGanji.text = item.ganji

            if(item.gender == 0)
                ivItemDbGender.setImageResource(R.drawable.ic_male)
            else
                ivItemDbGender.setImageResource(R.drawable.ic_female)

            changeVisibility(holder.binding,selectedItems.get(position))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDbListBinding.bind(itemView)

        init {
            binding.btnItemDbSearch.setOnClickListener {
                onMenuClickListener?.onSearchClick(items[adapterPosition].id, adapterPosition)
            }

            binding.btnItemDbDelete.setOnClickListener {
                onMenuClickListener?.onDeleteClick(items[adapterPosition].id, adapterPosition)
            }


            itemView.setOnClickListener {
                if(selectedItems.get(adapterPosition)) {
                    selectedItems.delete(adapterPosition)
                } else {
                    selectedItems.delete(prePosition)
                    selectedItems.put(adapterPosition,true)
                }

                if(prePosition != -1) {
                    notifyItemChanged(prePosition)
                }
                notifyItemChanged(adapterPosition)
                prePosition = adapterPosition

//                if (selectedItems.get(adapterPosition)) {
//                    // VISIBLE -> INVISIBLE
//                    selectedItems.delete(adapterPosition)
//
//                    binding.llItemDbBottomPanel.visibility = View.GONE
//                } else {
//                    // INVISIBLE -> VISIBLE
//                    selectedItems.put(adapterPosition, true)
//                    binding.llItemDbBottomPanel.visibility = View.VISIBLE
//                }
            }
        }
    }

    private fun changeVisibility(binding: ItemDbListBinding, isExpanded: Boolean) {
        // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
        val dpValue = 50
        val d = context.resources.displayMetrics.density
        val height = (dpValue * d).toInt()

        // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
        val va = if (isExpanded) ValueAnimator.ofInt(0, height) else ValueAnimator.ofInt(height, 0)
        // Animation이 실행되는 시간, n/1000초
        va.duration = 200
        va.addUpdateListener { animation -> // value는 height 값
            val value = animation.animatedValue as Int
            // imageView의 높이 변경
            binding.llItemDbBottomPanel.layoutParams.height = value
            binding.llItemDbBottomPanel.requestLayout()
            // imageView가 실제로 사라지게하는 부분
            binding.llItemDbBottomPanel.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
        // Animation start
        va.start()
    }
}

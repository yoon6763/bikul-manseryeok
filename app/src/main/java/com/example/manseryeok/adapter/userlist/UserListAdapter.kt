package com.example.manseryeok.adapter.userlist

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.adapter.userlist.item.UserRVItem
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.databinding.ItemDbListBinding
import java.lang.StringBuilder
import java.util.Calendar


class UserListAdapter(
    private val context: Context,
    private val items: List<UserRVItem>,
) :
    RecyclerView.Adapter<UserListAdapter.Holder>() {
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    private val TAG = "DBListAdapter"
    private var prePosition = -1
    var useKeywordHighlight = false
    var highlightKeyword = ""

    var onUserMenuClickListener: OnUserMenuClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_db_list, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            val user = items[position].user
            val birth = user.getBirthCalculated()
            val manseryeok = items[position].manseryeok

            var hourGanji = ""
            val yearGanji = manseryeok.cd_hyganjee!!
            val monthGanji = manseryeok.cd_hmganjee!!
            val dayGanji = manseryeok.cd_hdganjee!!

            val ganji = StringBuilder()

            if (user.includeTime) {
                hourGanji = Utils.getTimeGanji(dayGanji[0], birth[Calendar.HOUR_OF_DAY])
                ganji.append(hourGanji[0])
            }

            ganji.append(dayGanji[0])
            ganji.append(monthGanji[0])
            ganji.append(yearGanji[0])

            ganji.append("\n")

            if (user.includeTime) ganji.append(hourGanji[1])
            ganji.append(dayGanji[1])
            ganji.append(monthGanji[1])
            ganji.append(yearGanji[1])


            val username = user.firstName + user.lastName

            if (useKeywordHighlight) {
                tvItemDbName.text = highlightKeywords(username, highlightKeyword)
            } else {
                tvItemDbName.text = username
            }

            tvItemDbBirthSum.text = "(양) ${Utils.dateDotFormat.format(user.getBirthOrigin().timeInMillis)}"
            tvItemDbBirthMoon.text = "(음) ${Utils.dateDotFormat.format(Utils.convertSolarToLunar(birth))}"
            tvItemDbGanji.text = ganji.toString()


            val tags = items[position].tags.map { "#${it.name}\n" }

            tvItemDbTag.text = if(useKeywordHighlight) {
                highlightKeywords(tags.joinToString(""), highlightKeyword)
            } else {
                tags.joinToString("")
            }

            if (user.gender == 0) {
                ivItemDbGender.setImageResource(R.drawable.ic_male)
            } else {
                ivItemDbGender.setImageResource(R.drawable.ic_female)
            }

            changeVisibility(holder.binding, selectedItems.get(position))
        }
    }


    private fun highlightKeywords(originalText: String?, keyword: String?): SpannableString {
        val spannableString = SpannableString(originalText)

        if (!originalText.isNullOrBlank() && !keyword.isNullOrBlank()) {
            var startIndex = originalText.toLowerCase().indexOf(keyword.toLowerCase())
            var endIndex: Int

            var currentStartIndex = startIndex
            while (startIndex != -1) {
                endIndex = startIndex + keyword.length
                val colorSpan = ForegroundColorSpan(Color.RED)
                spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                currentStartIndex = originalText.toLowerCase().indexOf(keyword.toLowerCase(), endIndex)
                if (currentStartIndex != -1) {
                    startIndex = currentStartIndex
                } else {
                    break
                }
            }
        }

        return spannableString
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDbListBinding.bind(itemView)

        init {
            binding.btnItemDbSearchCalendar.setOnClickListener {
                onUserMenuClickListener?.onManseryeokView(
                    items[adapterPosition].user.userId,
                    adapterPosition
                )
            }

            binding.btnItemDbSearchName.setOnClickListener {
                onUserMenuClickListener?.onNameView(
                    items[adapterPosition].user.userId,
                    adapterPosition
                )
            }

            binding.btnItemDbDelete.setOnClickListener {
                onUserMenuClickListener?.onDeleteClick(
                    items[adapterPosition].user.userId,
                    adapterPosition
                )
            }

            binding.btnItemDbGroup.setOnClickListener {
                onUserMenuClickListener?.onGroupClick(
                    items[adapterPosition].user.userId,
                    adapterPosition
                )
            }

            binding.btnItemDbEdit.setOnClickListener {
                onUserMenuClickListener?.onEditClick(
                    items[adapterPosition].user.userId,
                    adapterPosition
                )
            }


            itemView.setOnClickListener {
                if (selectedItems.get(adapterPosition)) {
                    selectedItems.delete(adapterPosition)
                } else {
                    selectedItems.delete(prePosition)
                    selectedItems.put(adapterPosition, true)
                }

                if (prePosition != -1) {
                    notifyItemChanged(prePosition)
                }
                notifyItemChanged(adapterPosition)
                prePosition = adapterPosition
            }
        }
    }

    private fun changeVisibility(binding: ItemDbListBinding, isExpanded: Boolean) {
        // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
        val dpValue = 85
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

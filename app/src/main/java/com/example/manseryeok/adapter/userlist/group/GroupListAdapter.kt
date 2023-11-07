package com.example.manseryeok.adapter.userlist.group

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.adapter.decorator.RecyclerViewDecorator
import com.example.manseryeok.adapter.userlist.OnUserMenuClickListener
import com.example.manseryeok.adapter.userlist.UserListAdapter
import com.example.manseryeok.databinding.ItemDbListBinding
import com.example.manseryeok.databinding.ItemGroupBinding

class GroupListAdapter(
    private val context: Context,
    private val groupList: ArrayList<UserRecyclerViewItem>
) :
    RecyclerView.Adapter<GroupListAdapter.Holder>() {
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    private var prePosition = -1
    private val userListAdapters = ArrayList<UserListAdapter>()

    init {
        for (group in groupList) {
            userListAdapters.add(UserListAdapter(context, group.users, group.manseryeokList))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_group, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val group = groupList[position]

        holder.binding.run {
            tvGroupName.text = "${group.groupName} (${group.users.size}명)"
            rvGroupMemberList.adapter = userListAdapters[position]
            rvGroupMemberList.addItemDecoration(RecyclerViewDecorator(1, Color.parseColor("#EDEDED")))

            //changeVisibility(holder.binding, selectedItems.get(position))
        }
    }

    fun setUserMenuClickListener(menuClickListener: OnUserMenuClickListener) {
        for (adapter in userListAdapters) {
            adapter.onUserMenuClickListener = menuClickListener
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGroupBinding.bind(itemView)

        init {
//            itemView.setOnClickListener {
//                if (selectedItems.get(adapterPosition)) {
//                    selectedItems.delete(adapterPosition)
//                } else {
//                    selectedItems.delete(prePosition)
//                    selectedItems.put(adapterPosition, true)
//                }
//
//                if (prePosition != -1) {
//                    notifyItemChanged(prePosition)
//                }
//                notifyItemChanged(adapterPosition)
//                prePosition = adapterPosition
//            }
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

package com.example.manseryeok.adapter.userlist.group

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
import com.example.manseryeok.adapter.userlist.item.GroupRVItem
import com.example.manseryeok.databinding.ItemGroupBinding
import com.example.manseryeok.service.calendar.CalendarService

class GroupListAdapter(
    private val context: Context,
    private val groupList: List<GroupRVItem>,
    private val calenderService: CalendarService
) :
    RecyclerView.Adapter<GroupListAdapter.Holder>() {
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    private var prePosition = -1
    private val userListAdapters = ArrayList<UserListAdapter>()

    init {
        for (group in groupList) {
            userListAdapters.add(UserListAdapter(context, group.userRVItemList, calenderService))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_group, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = groupList[position]

        holder.binding.run {
            tvGroupName.text = "${item.groupLabel} (${item.userRVItemList.size}명)"
            rvGroupMemberList.adapter = userListAdapters[position]
            rvGroupMemberList.addItemDecoration(RecyclerViewDecorator(1, Color.parseColor("#EDEDED")))
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
    }
}

package com.example.manseryeok.adapter.userlist.group

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemGroupChecklistBinding
import com.example.manseryeok.models.user.groups.Group

class UserGroupCheckBoxAdapter(
    private val context: Context,
    private val groups: List<Group>,
    private val selectedGroups: HashSet<Long>
) : RecyclerView.Adapter<UserGroupCheckBoxAdapter.Holder>() {

    var onGroupCheckListener: OnGroupCheckListener? = null

    interface OnGroupCheckListener {
        fun onGroupCheck(groupId: Long, check: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_group_checklist, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            cbGroup.text = groups[position].name
            cbGroup.isChecked = groups[position].groupId in selectedGroups
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGroupChecklistBinding.bind(itemView)

        init {
            binding.cbGroup.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    onGroupCheckListener?.onGroupCheck(groups[adapterPosition].groupId, isChecked)
                }
            }
        }
    }

    fun uncheckAll() {
        for (element in groups) {
            selectedGroups.remove(element.groupId)
        }
        notifyDataSetChanged()
    }
}

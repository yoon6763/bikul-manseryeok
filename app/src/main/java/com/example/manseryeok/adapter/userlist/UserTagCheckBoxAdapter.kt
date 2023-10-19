package com.example.manseryeok.adapter.userlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemGroupBinding
import com.example.manseryeok.databinding.ItemGroupChecklistBinding
import com.example.manseryeok.models.user.GroupTag

class UserTagCheckBoxAdapter(
    private val context: Context,
    private val groupTags: List<GroupTag>,
    private val selectedTags: HashSet<Long>
) : RecyclerView.Adapter<UserTagCheckBoxAdapter.Holder>() {

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
            cbGroup.text = groupTags[position].name
            cbGroup.isChecked = groupTags[position].id in selectedTags
        }
    }

    override fun getItemCount(): Int {
        return groupTags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGroupChecklistBinding.bind(itemView)

        init {
            binding.cbGroup.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    onGroupCheckListener?.onGroupCheck(groupTags[adapterPosition].id, isChecked)
                }
            }
        }
    }
}

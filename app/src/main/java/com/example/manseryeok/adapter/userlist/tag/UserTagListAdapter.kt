package com.example.manseryeok.adapter.userlist.tag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ItemRemovableLabelBinding
import com.example.manseryeok.models.user.tags.Tag

class UserTagListAdapter(
    private val context: Context,
    private val tags: List<Tag>,
) : RecyclerView.Adapter<UserTagListAdapter.Holder>() {

    interface OnRemoveClickListener {
        fun onRemoveClick(position: Int, tagId: Long)
    }

    var onRemoveClickListener: OnRemoveClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_removable_label, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvItemName.text = "#${tags[position].name}"
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRemovableLabelBinding.bind(itemView)

        init {
            binding.ivRemove.setOnClickListener {
                onRemoveClickListener?.onRemoveClick(adapterPosition, tags[adapterPosition].id)
            }
        }
    }
}

package com.example.manseryeok.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.manseryeok.R
import com.example.manseryeok.models.notion.AdvertiseSliderModel


class ImageSliderAdapter(
    private val context: Context,
    private val sliderModels: ArrayList<AdvertiseSliderModel>
) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindSliderImage(sliderModels[position].imageUrl)
    }

    override fun getItemCount(): Int {
        return sliderModels.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAd: ImageView

        init {
            ivAd = itemView.findViewById(R.id.iv_ad)
            ivAd.setOnClickListener {
                // open browser
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(sliderModels[bindingAdapterPosition].siteUrl)
                )
                context.startActivity(intent)
            }
        }

        fun bindSliderImage(imageURL: String?) {
            Glide.with(context)
                .load(imageURL)
                .into(ivAd)
        }
    }
}
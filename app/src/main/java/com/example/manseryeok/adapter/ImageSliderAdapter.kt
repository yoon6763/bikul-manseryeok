package com.example.manseryeok.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.manseryeok.R


class ImageSliderAdapter(private val context: Context, private val sliderImageUrls: ArrayList<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindSliderImage(sliderImageUrls[position])
    }

    override fun getItemCount(): Int {
        return sliderImageUrls.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAd: ImageView

        init {
            ivAd = itemView.findViewById(R.id.iv_ad)
        }

        fun bindSliderImage(imageURL: String?) {
            Glide.with(context)
                .load(imageURL)
                .into(ivAd)
        }
    }
}
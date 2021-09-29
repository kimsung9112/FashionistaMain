package com.study.poly.fashionista.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.poly.fashionista.databinding.ViewpagerItemBinding
import com.study.poly.fashionista.utility.loadImage

class BannerAdapter(private val bannerUrlList: ArrayList<String>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = ViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bindWithView(bannerUrlList[position])
    }

    override fun getItemCount(): Int = bannerUrlList.size

    inner class BannerViewHolder(private val binding: ViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindWithView(bannerUrl: String) {

            binding.bannerImg.loadImage(bannerUrl)
        }
    }
}
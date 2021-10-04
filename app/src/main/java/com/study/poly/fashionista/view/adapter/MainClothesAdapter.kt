package com.study.poly.fashionista.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.poly.fashionista.databinding.ListviewItemClothesBinding
import com.study.poly.fashionista.utility.loadCircleImage
import com.study.poly.fashionista.utility.onThrottleFirstClick

class MainClothesAdapter(
    private val clothesUrlList: ArrayList<String>,
    val viewHandler: (itemName: String) -> Unit
) :
    RecyclerView.Adapter<MainClothesAdapter.MainClothesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainClothesViewHolder {
        val view =
            ListviewItemClothesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainClothesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainClothesViewHolder, position: Int) {
        holder.bindWithView(clothesUrlList[position])
    }

    override fun getItemCount(): Int = clothesUrlList.size

    inner class MainClothesViewHolder(private val binding: ListviewItemClothesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindWithView(clothesUrl: String) = with(binding) {

            clothesItem.loadCircleImage(clothesUrl)
            clothesItem.onThrottleFirstClick {
                viewHandler.invoke(clothesUrl)
            }
        }
    }

}
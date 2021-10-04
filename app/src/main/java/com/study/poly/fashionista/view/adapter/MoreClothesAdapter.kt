package com.study.poly.fashionista.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.poly.fashionista.data.entity.ClothesEntity
import com.study.poly.fashionista.databinding.ListviewItemMoreClothesBinding
import com.study.poly.fashionista.utility.loadImage
import com.study.poly.fashionista.utility.onThrottleFirstClick

class MoreClothesAdapter(
    private val clothesList: ArrayList<ClothesEntity>,
    val viewHandler: (itemName: String) -> Unit
) : RecyclerView.Adapter<MoreClothesAdapter.MoreClothesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreClothesViewHolder {
        val view = ListviewItemMoreClothesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MoreClothesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoreClothesViewHolder, position: Int) {
        holder.bindWithView(clothesList[position])
    }

    override fun getItemCount(): Int = clothesList.size

    inner class MoreClothesViewHolder(private val binding: ListviewItemMoreClothesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindWithView(clothesInfo: ClothesEntity) = with(binding) {

            clothesImg.loadImage(clothesInfo.TitlePath)
            clothesName.text = clothesInfo.Name
            clothesSize.text = "사이즈: M"  // 추후 DB 가져올 것
            clothesDetailInfo.text = "상세설명: ${clothesInfo.Info}"
            clothesShop.text = "쇼핑몰: ${clothesInfo.Shop}"

            binding.root.onThrottleFirstClick {
                viewHandler.invoke(clothesInfo.TitlePath)
            }
        }
    }

}
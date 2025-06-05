package com.example.freemates_android.ui.adapter.category

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCategoryLargeBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem

class CategoryLargeAdapter (val context: Context, val items: List<CategoryItem>, val externalClick: (String) -> (Unit)) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCategoryLargeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategoryLargeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CategoryLargeViewHolder) {
            holder.bind(items[position]) { clickedItem ->
                updateSelection(clickedItem)         // ① 내부 상태 갱신
                val category: String =
                    when (clickedItem.title) {
                        "카페" -> "CAFE"
                        "먹거리" -> "FOOD"
                        "쇼핑" -> "SHOPPING"
                        "산책" -> "WALK"
                        "놀거리" -> "PLAY"
                        "병원" -> "HOSPITAL"
                        else -> ""
                    }

                Log.d("CategoryLarge", "category is $category")
                externalClick(category)     // ② 화면 밖(뷰모델 등)으로 알림
            }
//            var backgroundColor = ContextCompat.getColor(context, R.color.primary400)
//            var textColor = ContextCompat.getColor(context, R.color.secondary200)
//            if (categoryLargeItemList[position].imageUrl == R.drawable.ic_cafe_large_off ||
//                categoryLargeItemList[position].imageUrl == R.drawable.ic_walk_large_off ||
//                categoryLargeItemList[position].imageUrl == R.drawable.ic_leisure_large_off ||
//                categoryLargeItemList[position].imageUrl == R.drawable.ic_shopping_large_off ||
//                categoryLargeItemList[position].imageUrl == R.drawable.ic_foods_large_off ||
//                categoryLargeItemList[position].imageUrl == R.drawable.ic_hospital_large_off
//            ) {
//                backgroundColor = ContextCompat.getColor(context, R.color.natural100)
//                textColor = ContextCompat.getColor(context, R.color.natural200)
//            }
//
//            val tintList = ColorStateList.valueOf(backgroundColor)
//            ViewCompat.setBackgroundTintList(holder.container, tintList)
//            Glide.with(context)
//                .load(categoryLargeItemList[position].imageUrl) // 불러올 이미지 url
//                .into(holder.categoryImage) // 이미지를 넣을 뷰
//            holder.categoryText.text = categoryLargeItemList[position].title
//            holder.categoryText.setTextColor(textColor)
        }
    }

    private fun updateSelection(clicked: CategoryItem) {
        val old = items.indexOfFirst { it.isSelected }
        val new = items.indexOf(clicked)

        if (new == old) return               // 같은 아이템이면 스킵

        if (old != -1) {
            items[old].isSelected = false
            notifyItemChanged(old)
        }
        items[new].isSelected = true
        notifyItemChanged(new)
    }
}
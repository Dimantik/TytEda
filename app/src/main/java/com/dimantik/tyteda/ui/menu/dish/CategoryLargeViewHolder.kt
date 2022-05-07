package com.dimantik.tyteda.ui.menu.dish

import androidx.recyclerview.widget.RecyclerView
import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.databinding.ViewHolderCategoryLargeBinding

class CategoryLargeViewHolder(
    private val binding: ViewHolderCategoryLargeBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var category: Category

    fun onBind(category: Category) {
        this.category = category

        binding.name.text = category.name
    }

}
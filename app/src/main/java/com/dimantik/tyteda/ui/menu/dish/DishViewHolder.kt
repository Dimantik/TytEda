package com.dimantik.tyteda.ui.menu.dish

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimantik.tyteda.R
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.databinding.ViewHolderDishBinding
import com.dimantik.tyteda.utils.LogSettings


@SuppressLint("ClickableViewAccessibility")
class DishViewHolder(
    private val binding: ViewHolderDishBinding,
    private val context: Context,
    private val dishClickListener: OnDishClickListener,
    private val favoriteClickListener: OnDishFavoriteClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            dishClickListener.onDishClick(dish)
        }

        binding.root.setOnTouchListener{ _, event ->
            val anim = when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isZoomOut = true
                    R.anim.animation_zoom_out
                }
                MotionEvent.ACTION_UP -> {
                    isZoomOut = false
                    R.anim.animation_zoom_in
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isZoomOut)
                        R.anim.animation_zoom_in
                    else null
                }
                else -> null
            }

            anim?.let {
                AnimationUtils.loadAnimation(context, it).apply {
                    fillAfter = true
                    binding.root.startAnimation(this)
                }
            }

            false
        }

        binding.favorite.setOnClickListener {
            favoriteClickListener.onDishFavoriteClick(dish)
        }
    }

    private var isZoomOut = false
    private var isFavorite = false
    private lateinit var dish: Dish

    fun onBind(
        dish: Dish,
        isFavorite: Boolean,
        hideDivider: Boolean,
        cartItem: CartItem?
    ) {
        this.dish = dish
        this.isFavorite = isFavorite

        binding.name.text = dish.name
        binding.price.text = StringBuilder()
            .append(dish.price)
            .append(" Р")
            .toString()


        when(cartItem) {
            null -> binding.amount.visibility = View.INVISIBLE
            else -> binding.amount.apply {
                text = cartItem.amount.toString()
                visibility = View.VISIBLE
                Log.i(LogSettings.LOG_TYT_EDA_LOG, "${cartItem.dishId} : ${cartItem.amount}")
            }
        }

        val imageResourceId =
            if (isFavorite) R.drawable.image_favorite
            else R.drawable.image_favorite_border

        binding.favorite.setImageResource(imageResourceId)

        if (hideDivider) binding.divider.visibility = View.INVISIBLE
        else binding.divider.visibility = View.VISIBLE

        Glide
            .with(context)
            .load(dish.image)
            .into(binding.image)
    }

    fun interface OnDishClickListener {
        fun onDishClick(dish: Dish)
    }

    fun interface OnDishFavoriteClickListener {
        fun onDishFavoriteClick(dish: Dish)
    }

}
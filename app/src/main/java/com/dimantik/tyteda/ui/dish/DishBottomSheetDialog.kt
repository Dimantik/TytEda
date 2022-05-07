package com.dimantik.tyteda.ui.dish

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dimantik.tyteda.TytedaApplication
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.databinding.DialogBottomSheetDishBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder

class DishBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {

        private const val DISH_ID = "dish_id"

        fun newInstance(
            dishId: Int,
            iIsChangeCart: IIsChangeCart
        ) = DishBottomSheetDialog().apply {
            this.iIsChangeCart = iIsChangeCart
            arguments = Bundle().also {
                it.putSerializable(DISH_ID, dishId)

            }
        }
    }

    private lateinit var binding: DialogBottomSheetDishBinding
    private lateinit var viewModel: DishViewModel

    private lateinit var iIsChangeCart: IIsChangeCart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogBottomSheetDishBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
        initViewModel()
        initView()
        getArgs()
        initView()
        initAmountController()
    }.root

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            (requireActivity().application as TytedaApplication).viewModelFactory
        )[DishViewModel::class.java]
    }

    private fun getArgs() {
        arguments?.let { noNullArgs ->
            viewModel.getDish(noNullArgs.getInt(DISH_ID))
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        iIsChangeCart.isChangeCart(false)
    }
    private fun initView() {
        dialog?.let {
            val behavior = (dialog as BottomSheetDialog).behavior
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        viewModel.dishLD.observe(viewLifecycleOwner) { dish ->
            binding.name.text = dish.name
            binding.summary.text = dish.summary

            Glide
                .with(requireContext())
                .load(dish.image)
                .into(binding.image)
        }

        binding.close.setOnClickListener {
            dismiss()
        }
    }

    private fun initAmountController() {
        viewModel.amountLD.observe(viewLifecycleOwner) { amount ->
            binding.amount.text = amount.toString()
        }

        viewModel.addCompleteLD.observe(viewLifecycleOwner) { result ->
            when(result) {
                false -> Toast.makeText(requireContext(), "Ошибка!", Toast.LENGTH_SHORT).show()
                true -> {
                    iIsChangeCart.isChangeCart(true)
                    dismiss()
                }
            }
        }

        viewModel.priceLD.observe(viewLifecycleOwner) { price ->
            binding.addToCart.text = StringBuilder()
                .append("Добавить  ")
                .append(price)
                .append(" Р")
                .toString()
        }

        binding.addToCart.setOnClickListener {
            viewModel.addToCart()
        }

        binding.reduce.setOnClickListener {
            viewModel.reduceAmount()
        }

        binding.increase.setOnClickListener {
            viewModel.increaseAmount()
        }
    }

    fun interface IIsChangeCart {
        fun isChangeCart(isChange: Boolean)
    }

}
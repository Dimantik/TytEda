package com.dimantik.tyteda.ui.payMethod

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimantik.tyteda.TytedaApplication
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.databinding.DialogBottomSheetCartBinding
import com.dimantik.tyteda.ui.cart.cartItem.CartItemAdapter
import com.dimantik.tyteda.ui.cart.cartItem.CartItemViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder

class PayMethodBottomSheetDialog : BottomSheetDialogFragment(), CartItemViewHolder.CartItemAmountController {


    private lateinit var binding: DialogBottomSheetCartBinding

    private val cartItemAdapter = CartItemAdapter(this)

    private lateinit var iIsChangeCart: IIsChangeCart

    private var isChangeCart = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogBottomSheetCartBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
        initBottomSheetBehavior()
        initCloseButton()
        initViewModel()
        observeViewModel()
        initRegOrderButton()
        initCartRecycler()
    }.root

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        iIsChangeCart.isChangeCart(isChangeCart)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        iIsChangeCart.isChangeCart(isChangeCart)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            (requireActivity().application as TytedaApplication).viewModelFactory
        )[CartViewModel::class.java]
    }

    private fun observeViewModel() {
        viewModel.totalLD.observe(viewLifecycleOwner) { total ->
            binding.total.text = StringBuilder()
                .append(total)
                .append(" Р")
                .toString()
        }
    }

    private fun initRegOrderButton() {
        binding.regOrder.setOnClickListener {
            viewModel.regOrder(
                name = binding.name.text.toString(),
                phone = binding.phone.text.toString(),
                email = binding.email.text.toString()
            )
        }
    }

    private fun initBottomSheetBehavior() {
        dialog?.let {
            val behavior = (dialog as BottomSheetDialog).behavior

            behavior.saveFlags = BottomSheetBehavior.SAVE_ALL
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initCloseButton() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    private fun initCartRecycler() {
        binding.cartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecycler.adapter = cartItemAdapter

        viewModel.cartItemListLD.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                dismiss()
            } else {
                updateCartRecycler(
                    newCartItemList = it,
                    newDishList = cartItemAdapter.dishList
                )
            }
        }

        viewModel.dishListLD.observe(viewLifecycleOwner) { updateCartRecycler(
            newCartItemList = cartItemAdapter.cartItemList,
            newDishList = it
        ) }
    }

    private fun updateCartRecycler(
        newCartItemList: List<CartItem>,
        newDishList: List<Dish>
    ) {
        cartItemAdapter.cartItemList = newCartItemList
        cartItemAdapter.dishList = newDishList
        cartItemAdapter.notifyDataSetChanged()
    }

    override fun reduceAmount(cartItem: CartItem) {
        viewModel.reduceCartItemAmount(cartItem)
        isChangeCart = true
    }

    override fun increaseAmount(cartItem: CartItem) {
        viewModel.increaseCartItemAmount(cartItem)
        isChangeCart = true
    }

    fun interface IIsChangeCart {
        fun isChangeCart(isChange: Boolean)
    }

}
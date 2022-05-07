package com.dimantik.tyteda.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimantik.tyteda.TytedaApplication
import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.data.model.custom.Menu
import com.dimantik.tyteda.data.remote.ServiceResult
import com.dimantik.tyteda.databinding.FragmentMenuBinding
import com.dimantik.tyteda.ui.cart.CartBottomSheetDialog
import com.dimantik.tyteda.ui.dish.DishBottomSheetDialog
import com.dimantik.tyteda.ui.menu.dish.DishAdapter
import com.dimantik.tyteda.ui.menu.dish.DishViewHolder
import com.google.android.material.tabs.TabLayout
import java.lang.StringBuilder

class MenuFragment : Fragment(),
    DishViewHolder.OnDishClickListener,
    DishViewHolder.OnDishFavoriteClickListener
{

    private lateinit var binding: FragmentMenuBinding
    private lateinit var viewModel: MenuViewModel

    private val dishAdapter = DishAdapter(this, this)
    private var tabList = mutableListOf<TabLayout.Tab>()

    private lateinit var dishLinearLayoutManager: LinearLayoutManager

    private var isScrolled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMenuBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
        initViewModel()
        initView()
        observeViewModel()
        initShowCartButton()
    }.root

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            (requireActivity().application as TytedaApplication).viewModelFactory
        )[MenuViewModel::class.java]
    }

    private fun initView() {
        binding.update.setOnClickListener {
            viewModel.updateMenu()
        }

        binding.dishRecycler.adapter = dishAdapter
        binding.dishRecycler.layoutManager = LinearLayoutManager(requireContext()).apply {
            dishLinearLayoutManager = this
        }

        binding.categoryTabs
            .addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (!isScrolled) {
                        dishLinearLayoutManager
                            .scrollToPositionWithOffset(dishAdapter.getCategoryPosition(tab!!.id), 0)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}

            }
        )

        binding.dishRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    changeCurrentCategory(dy > 0)
                }
            }
        )
    }

    private fun changeCurrentCategory(isScrollUp: Boolean) {
        (binding.dishRecycler.layoutManager as LinearLayoutManager).also { dishLayoutManager ->
            val position = (
                    dishLayoutManager.findFirstVisibleItemPosition() +
                            dishLayoutManager.findLastVisibleItemPosition()) / 2

            dishAdapter.getCategoryByPosition(position)?.let { noNullCategory ->
                var tabIndex = tabList.indexOfFirst { it.id == noNullCategory.id }

                if (!isScrollUp) tabIndex--

                isScrolled = true
                binding.categoryTabs.selectTab(tabList[tabIndex])
                isScrolled = false
            }
        }
    }

    //ViewModel
    private fun observeViewModel() {
        viewModel.serviceResultLD.observe(viewLifecycleOwner) { updateResult ->
            when(updateResult) {
                is ServiceResult.Success -> onUpdateSuccess(updateResult)
                is ServiceResult.Load -> onUpdateLoad(updateResult)
                is ServiceResult.Failure -> onUpdateFailure(updateResult)
            }
        }

        viewModel.favoriteListLD.observe(viewLifecycleOwner) { favoriteList ->
            savePosition { updateFavoriteList(favoriteList) }
        }

        viewModel.cartItemListLD.observe(viewLifecycleOwner) { cartItemList ->
            savePosition { updateCartItemList(cartItemList) }
        }

        viewModel.totalLD.observe(viewLifecycleOwner) { total ->
            when(total) {
                0 -> binding.regOrder.visibility = View.GONE
                else -> {
                    binding.regOrder.visibility = View.VISIBLE
                    binding.price.text = StringBuilder()
                        .append(total)
                        .append(" Р")
                        .toString()
                }
            }
        }
    }

    //Success
    private fun onUpdateSuccess(serviceResult: ServiceResult<Menu>) {
        binding.updateContainer.visibility = View.GONE
        binding.updateFailureContainer.visibility = View.GONE
        binding.menuContainer.visibility = View.VISIBLE
        binding.categoryTabs.visibility = View.VISIBLE

        serviceResult.data?.let { noNullMenu ->
            updateCategoryList(noNullMenu.categoryList)
            initMenu(noNullMenu)
        }
    }

    private fun updateCategoryList(categoryList: List<Category>) {
        binding.categoryTabs.removeAllTabs()
        tabList = mutableListOf()

        categoryList.forEach { category ->
            binding.categoryTabs.newTab().apply {
                id = category.id!!
                text = category.name
                binding.categoryTabs.addTab(this)
                tabList.add(this)
            }

        }
    }

    private fun updateCartItemList(cartItemList: List<CartItem>) {
        dishAdapter.cartItemList = cartItemList
        dishAdapter.notifyDataSetChanged()
    }

    private fun updateFavoriteList(favoriteList: List<Int>) {
        dishAdapter.favoriteList = favoriteList
        dishAdapter.notifyDataSetChanged()
    }

    private fun savePosition(func: () -> Unit) {
        val currentPosition = dishLinearLayoutManager.findFirstVisibleItemPosition()
        val startView: View = binding.dishRecycler.getChildAt(0)
        val padding = startView.top - binding.dishRecycler.paddingTop

        func()

        dishLinearLayoutManager.scrollToPositionWithOffset(currentPosition, padding)
    }

    private fun initMenu(menu: Menu) {
        dishAdapter.dishMap = menu.dishMap
        dishAdapter.favoriteList = menu.favoriteList
        dishAdapter.categoryList = menu.categoryList
        dishAdapter.cartItemList = menu.cartItemList
        dishAdapter.notifyDataSetChanged()
    }

    //Failure
    private fun onUpdateFailure(serviceResult: ServiceResult<Menu>) {
        if (!viewModel.isInitPrevious) {
            binding.menuContainer.visibility = View.GONE
            binding.updateContainer.visibility = View.GONE
            binding.categoryTabs.visibility = View.GONE
            binding.updateFailureContainer.visibility = View.VISIBLE
        }
    }

    //Load
    private fun onUpdateLoad(serviceResult: ServiceResult<Menu>) {
        if (!viewModel.isInitPrevious) {
            binding.menuContainer.visibility = View.GONE
            binding.updateFailureContainer.visibility = View.GONE
            binding.categoryTabs.visibility = View.GONE
            binding.updateContainer.visibility = View.VISIBLE
        }
    }

    private fun initShowCartButton() {
       binding.regOrder.setOnClickListener {
           val cartBottomDialog = CartBottomSheetDialog.newInstance {
               if (it) viewModel.updateCart()
           }
           cartBottomDialog.show(childFragmentManager, cartBottomDialog::class.java.toString())
       }
    }

    override fun onDishClick(dish: Dish) {
        val dishBottomDialog = DishBottomSheetDialog.newInstance(dish.id!!) {
            if (it) viewModel.updateCart()
        }

        dishBottomDialog.show(childFragmentManager, dishBottomDialog::class.java.toString())
    }

    override fun onDishFavoriteClick(dish: Dish) {
        viewModel.changeFavoriteStatus(dish)
    }

}
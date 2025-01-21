package com.neatroots.suddahutpadah.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.adapter.CartAdapter
import com.neatroots.suddahutpadah.databinding.DialogAnimationBinding
import com.neatroots.suddahutpadah.databinding.FragmentCartBinding
import com.neatroots.suddahutpadah.factory.MainViewModelFactory
import com.neatroots.suddahutpadah.model.CartModel
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.model.UserModel
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.utils.SharedPref
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.utils.Utils.gone
import com.neatroots.suddahutpadah.utils.Utils.visible
import com.neatroots.suddahutpadah.viewmodel.MainViewModel


//class CartFragment : Fragment(), CartAdapter.OnItemClickListener {
//    private val binding by lazy { FragmentCartBinding.inflate(layoutInflater) }
//    private lateinit var mainViewModel: MainViewModel
//    private lateinit var adapter: CartAdapter
//    private lateinit var user: UserModel
//    private lateinit var animationProgress: AlertDialog
//    private lateinit var cartList: ArrayList<CartModel>
//    private lateinit var productList: ArrayList<ProductModel>
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val repository = MainRepository(requireContext())
//        val factory = MainViewModelFactory(repository)
//        mainViewModel = ViewModelProvider(this@CartFragment, factory)[MainViewModel::class.java]
//        adapter = CartAdapter(this@CartFragment)
//        binding.rv.adapter = adapter
//        animationProgress = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
//        user = SharedPref.getUserData(requireContext()) ?: UserModel()
//        cartList = ArrayList()
//        productList = ArrayList()
//
//        binding.apply {
//
//            mainViewModel.servicesList.observe(viewLifecycleOwner) {
//                productList.addAll(it)
//            }
//
//            mainViewModel.cartItemsList.observe(viewLifecycleOwner) { list->
//                if (list.isNotEmpty()) {
//                    cartList.clear()
//                    cartList.addAll(list)
//                    loadingLayout.gone()
//                    mainLayout.visible()
//                    adapter.submitList(list)
//                    tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"
//
//                    btSwipeOrder.setOnActiveListener {
//                        showAnimationDialog("Order Placing..", "order_placed.json", false)
//                        mainViewModel.placeOrder(user.userId, Utils.getCurrentDateTime(), calculateGrandTotal(adapter).toString(), list)
//                    }
//
//                } else {
//                    loadingLayout.visible()
//                    mainLayout.gone()
//                    adapter.submitList(emptyList())
//                    animationView3.setAnimation("empty_cart.json")
//                    animationView3.playAnimation()
//                    tvStatus.text = "Your Cart is Empty"
//                }
//            }
//
//            mainViewModel.orderPlaced.observe(viewLifecycleOwner) { success ->
//                if(success) {
//                    animationProgress.dismiss()
//                    showAnimationDialog("Order Placed", "order_placed.json", true)
//                } else {
//                    Utils.showMessage(requireContext(), "Order Failed")
//                }
//            }
//
//        }
//
//    }
//
//    fun areAllCartProductsAvailable(productList: List<ProductModel>, cartList: List<CartModel>): Boolean {
//        return cartList.all { cart ->
//            productList.any { it.id == cart.productId }
//        }
//    }
//
//
//
//    private fun showAnimationDialog(title: String, animation: String, visible: Boolean) {
//        animationProgress = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
//        val processLayout = DialogAnimationBinding.inflate(LayoutInflater.from(context))
//        processLayout.animationView3.setAnimation(animation)
//        processLayout.animationView3.playAnimation()
//        animationProgress.setView(processLayout.root)
//        animationProgress.setCancelable(false)
//
//        if(!visible) processLayout.btBack.gone() else processLayout.btBack.visible()
//
//        processLayout.tvHeading.text = title
//
//        processLayout.btBack.setOnClickListener {
//            animationProgress.dismiss()
//            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
//        }
//
//        animationProgress.show()
//    }
//
//    private fun calculateGrandTotal(adapter: CartAdapter): Int {
//        var grandTotal = 0
//        val itemList = adapter.currentList
//        for (item in itemList) {
//            val totalPrice = item.qty.toInt() * item.price.toInt()
//            grandTotal += totalPrice
//        }
//        return grandTotal
//    }
//
//    override fun removeFromCart(product: CartModel) {
//        mainViewModel.removeFromCart(user.userId, product.productId)
//        binding.rv.adapter = adapter
//        adapter.submitList(cartList)
//        binding.tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"
//    }
//
//    override fun onQuantityChanged(product: CartModel) {
//        val updatedList = adapter.currentList.map {
//            if (it.productId == product.productId) {
//                it.copy(qty = product.qty)
//            } else {
//                it
//            }
//        }
//        adapter.submitList(updatedList)
//        binding.tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"
//
//    }
//
//}

class CartFragment : Fragment(), CartAdapter.OnItemClickListener {
    private val binding by lazy { FragmentCartBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: CartAdapter
    private lateinit var user: UserModel
    private lateinit var animationProgress: AlertDialog
    private lateinit var cartList: ArrayList<CartModel>
    private lateinit var productList: ArrayList<ProductModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MainRepository(requireContext())
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@CartFragment, factory)[MainViewModel::class.java]
        adapter = CartAdapter(this@CartFragment)
        binding.rv.adapter = adapter
        animationProgress = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        user = SharedPref.getUserData(requireContext()) ?: UserModel()
        cartList = ArrayList()
        productList = ArrayList()

        binding.apply {

            mainViewModel.servicesList.observe(viewLifecycleOwner) {
                productList.addAll(it)
            }

            mainViewModel.cartItemsList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    cartList.clear()
                    cartList.addAll(list)
                    loadingLayout.gone()
                    mainLayout.visible()
                    adapter.submitList(list)
                    tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"

                    btSwipeOrder.setOnActiveListener {
                        showAnimationDialog("Order Placing..", "order_placed.json", false)
                        mainViewModel.placeOrder(
                            user.userId,
                            Utils.getCurrentDateTime(),
                            calculateGrandTotal(adapter).toString(),
                            list
                        )
                    }

                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    adapter.submitList(emptyList())
                    animationView3.setAnimation("empty_cart.json")
                    animationView3.playAnimation()
                    tvStatus.text = "Your Cart is Empty"
                }
            }

            mainViewModel.orderPlaced.observe(viewLifecycleOwner) { success ->
                if (success) {
                    animationProgress.dismiss()
                    showAnimationDialog("Order Placed", "order_placed.json", true)
                } else {
                    Utils.showMessage(requireContext(), "Order Failed")
                }
            }
        }
    }

    fun areAllCartProductsAvailable(productList: List<ProductModel>, cartList: List<CartModel>): Boolean {
        return cartList.all { cart ->
            productList.any { it.id == cart.productId }
        }
    }

    private fun showAnimationDialog(title: String, animation: String, visible: Boolean) {
        animationProgress = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
        val processLayout = DialogAnimationBinding.inflate(LayoutInflater.from(context))
        processLayout.animationView3.setAnimation(animation)
        processLayout.animationView3.playAnimation()
        animationProgress.setView(processLayout.root)
        animationProgress.setCancelable(false)

        if (!visible) processLayout.btBack.gone() else processLayout.btBack.visible()

        processLayout.tvHeading.text = title

        processLayout.btBack.setOnClickListener {
            animationProgress.dismiss()
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }

        animationProgress.show()
    }

    private fun calculateGrandTotal(adapter: CartAdapter): Int {
        var grandTotal = 0
        val itemList = adapter.currentList
        for (item in itemList) {
            val qty = item.qty.toIntOrNull() ?: 0 // Default to 0 if parsing fails
            val price = item.price.toIntOrNull() ?: 0 // Default to 0 if parsing fails

            if (qty == 0 || price == 0) {
                Log.e("CartFragment", "Invalid data in CartModel: qty=${item.qty}, price=${item.price}")
            }

            val totalPrice = qty * price
            grandTotal += totalPrice
        }
        return grandTotal
    }

    @SuppressLint("SetTextI18n")
    override fun removeFromCart(product: CartModel) {
        mainViewModel.removeFromCart(user.userId, product.productId)
        binding.rv.adapter = adapter
        adapter.submitList(cartList)
        binding.tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"
    }

    @SuppressLint("SetTextI18n")
    override fun onQuantityChanged(product: CartModel) {
        val updatedList = adapter.currentList.map {
            if (it.productId == product.productId) {
                it.copy(qty = product.qty)
            } else {
                it
            }
        }
        adapter.submitList(updatedList)
        binding.tvGrandTotal.text = "₹ ${calculateGrandTotal(adapter)}"
    }
}
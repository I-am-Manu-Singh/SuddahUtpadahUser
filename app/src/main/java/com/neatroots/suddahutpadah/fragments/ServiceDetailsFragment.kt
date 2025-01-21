package com.neatroots.suddahutpadah.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.activities.AuthActivity
import com.neatroots.suddahutpadah.databinding.FragmentServiceDetailsBinding
import com.neatroots.suddahutpadah.factory.MainViewModelFactory
import com.neatroots.suddahutpadah.model.CartModel
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.model.UserModel
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.utils.Constants
import com.neatroots.suddahutpadah.utils.SharedPref
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.utils.Utils.gone
import com.neatroots.suddahutpadah.utils.Utils.visible
import com.neatroots.suddahutpadah.viewmodel.MainViewModel


class ServiceDetailsFragment : Fragment() {
    private val binding by lazy { FragmentServiceDetailsBinding.inflate(layoutInflater) }
    private val service: ServiceDetailsFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDetails(service.service)
        setupCounter("1", binding.tvQty, binding.btIncrease, binding.btDecrease)
        user = SharedPref.getUserData(requireContext()) ?: UserModel()

        val repository = MainRepository(requireContext())
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]


        binding.apply {

            loadingLayout.visible()
            mainLayout.gone()

            if (!service.service.available) {
                loadingLayout.gone()
                mainLayout.visible()
                btAddToCart.text = "Not Available"
                btAddToCart.isEnabled = false
            }

            mainViewModel.isInCart(user.userId, service.service.id)

            mainViewModel.inCartProduct.observe(viewLifecycleOwner) { cartItem ->
                setupCounter(cartItem.qty, binding.tvQty, binding.btIncrease, binding.btDecrease)
                tvQty.text = cartItem.qty

                btAddToCart.setOnClickListener {
                    if(Firebase.auth.currentUser == null) {
                        startActivity(Intent(requireActivity(), AuthActivity::class.java))
                    } else if(cartItem.qty != tvQty.text && btAddToCart.text == Constants.GO_TO_CART) {
                        mainViewModel.updateQty(user.userId, cartItem.productId, tvQty.text.toString())
                        findNavController().navigate(R.id.action_serviceDetailsFragment_to_cartFragment)
                    } else if(btAddToCart.text == Constants.GO_TO_CART) {
                        findNavController().navigate(R.id.action_serviceDetailsFragment_to_cartFragment)
                    } else {
                        val cartId = Firebase.database.getReference(Constants.CART_REF).push().key
                        val cart = CartModel(cartId!!, service.service.id, service.service.image,
                            service.service.title, tvQty.text.toString(),
                            service.service.offerPrice, service.service.originalPrice, Constants.ORDERED)

                        mainViewModel.addToCart(user.userId, cart)
                    }
                }

            }

            mainViewModel.isInCart.observe(viewLifecycleOwner) { success ->
                if (success) {
                    mainViewModel.isInCart(user.userId, service.service.id)
                } else {
                    btAddToCart.text = Constants.ADD_TO_CART
                    Utils.showMessage(requireContext(), "Something went wrong")
                }
            }

            mainViewModel.alreadyInCart.observe(viewLifecycleOwner) { success ->
                if (!service.service.available) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    btAddToCart.text = "Not Available"
                    btAddToCart.isEnabled = false
                } else if (success) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    btAddToCart.text = Constants.GO_TO_CART
                    btAddToCart.isEnabled = true
                } else {
                    loadingLayout.gone()
                    mainLayout.visible()
                    btAddToCart.text = Constants.ADD_TO_CART
                    btAddToCart.isEnabled = true
                }
            }


            btAddToCart.setOnClickListener {
                if(Firebase.auth.currentUser == null) {
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                } else if (!service.service.available) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    btAddToCart.text = "Not Available"
                    btAddToCart.isEnabled = false
                } else if(btAddToCart.text == Constants.GO_TO_CART) {
                    findNavController().navigate(R.id.action_serviceDetailsFragment_to_cartFragment)
                } else {
                    val cartId = Firebase.database.getReference(Constants.CART_REF).push().key
                    val cart = CartModel(cartId!!, service.service.id, service.service.image,
                        service.service.title, tvQty.text.toString(),
                        service.service.offerPrice, service.service.originalPrice, Constants.ORDERED)

                    mainViewModel.addToCart(user.userId, cart)
                }
            }


        }

    }

    private fun setDetails(service: ProductModel) {
        binding.apply {
           ivImage.load(service.image) {
               placeholder(R.drawable.placeholder)
               error(R.drawable.placeholder)
           }

            tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvTitle.text = service.title
            tvDescription.text = service.description
            tvOfferPrice.text = "₹${service.offerPrice}"
            tvOriginalPrice.text = "₹${service.originalPrice}"
            val dc = Utils.calculateDiscount(service.offerPrice.toInt(), service.originalPrice.toInt())
            tvPercentage.text = "${dc.toInt()}% Off"



        }

    }


    private fun addNextLineAfterFullStop(text: String): String {
        val lines = text.split("*").map { it.trim() }.filter { it.isNotEmpty() }
        val newText = StringBuilder()
        lines.forEachIndexed { index, line ->
            newText.append("• $line")
            if (index < lines.size - 1) {
                newText.append("\n")
            }
        }
        return newText.toString()
    }


    private fun setupCounter(initialCount: String, textView: TextView, increaseButton: ImageView, decreaseButton: ImageView) {
        var count = initialCount.toIntOrNull() ?: 1

        textView.text = count.toString()

        increaseButton.setOnClickListener {
            count++
            textView.text = count.toString()
        }

        decreaseButton.setOnClickListener {
            if (count > 1) {
                count--
                textView.text = count.toString()
            }
        }
    }


}
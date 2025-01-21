package com.neatroots.suddahutpadah.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neatroots.suddahutpadah.model.CartModel
import com.neatroots.suddahutpadah.model.CategoryModel
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    val status: LiveData<Boolean> = mainRepository.status
    val isInCart: LiveData<Boolean> = mainRepository.isInCart
    val alreadyInCart: LiveData<Boolean> = mainRepository.alreadyInCart
    val orderPlaced: LiveData<Boolean> = mainRepository.orderPlaced


    val inCartProduct: LiveData<CartModel> = mainRepository.inCartProduct
    val servicesList: LiveData<List<ProductModel>> = mainRepository.serviceList
    val categoryServiceList: LiveData<List<ProductModel>> = mainRepository.categoryServiceList
    val cartItemsList: LiveData<List<CartModel>> = mainRepository.cartItemsList
    val orderHistory: LiveData<List<CartModel>> =  mainRepository.orderHistory
    val pendingOrders: LiveData<List<CartModel>> =  mainRepository.pendingOrdersList
    val categoryList: LiveData<List<CategoryModel>> = mainRepository.categoryList


    fun addToCart(userId: String, cart: CartModel) {
        viewModelScope.launch {
            try {
                mainRepository.addToCart(userId, cart)
            } catch (e: Exception) {
                mainRepository.isInCart.postValue(false)
            }

        }

    }

    fun fetchCategoryServices(id: String) {
        viewModelScope.launch {
            try {
                mainRepository.fetchCategoryServices(id)
            } catch (e: Exception) {
                mainRepository.isInCart.postValue(false)
            }

        }

    }

    fun isInCart(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                mainRepository.isInCart(userId, productId)
            } catch (e: Exception) {
                mainRepository.isInCart.postValue(false)
            }

        }

    }

    fun removeFromCart(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                mainRepository.removeFromCart(userId, productId)
            } catch (e: Exception) {
                mainRepository.isInCart.postValue(false)
            }

        }
    }

    fun updateQty(userId: String, productId: String, qty: String) {
        viewModelScope.launch {
            try {
                mainRepository.updateQty(userId, productId, qty)
            } catch (e: Exception) {
                println(e.message)
            }

        }
    }

    fun placeOrder(userId: String, date: String, amount: String, orders: List<CartModel>)  {
        viewModelScope.launch {
            try {
                mainRepository.placeOrder(userId, date, amount, orders)
            } catch (e: Exception) {
                mainRepository.orderPlaced.postValue(false)
            }

        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                mainRepository.fetchCategories()
            } catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

}
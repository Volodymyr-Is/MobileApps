package com.example.myapp2.views

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapp2.data.PurchaseModel
import com.example.myapp2.data.RandomListItems
import kotlin.random.Random

class CardViewModel : ViewModel() {
    private val randomListItems = RandomListItems()

    val purchases = mutableStateOf<List<PurchaseModel>>(emptyList())
    val totalPrice = mutableStateOf(0)
    val otp = mutableStateOf("")
    val isGeneratedOTP = mutableStateOf(false)
    val isPayed = mutableStateOf(false)

    init {
        createPurchases()
    }

    fun createPurchases(){
        purchases.value = randomListItems.generateRandomPurchase(10)
        calculateTotal()
    }

    fun calculateTotal(){
        totalPrice.value = purchases.value.sumOf { it.price }
    }

    fun generateOTP(){
        if (!isGeneratedOTP.value) {
            otp.value = Random.nextInt(100000, 999999).toString()
            isGeneratedOTP.value = true
        }
    }
}
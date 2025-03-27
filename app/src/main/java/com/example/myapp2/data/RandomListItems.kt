package com.example.myapp2.data

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class RandomListItems {
    private val categories = listOf("Camera", "Television")
    private val colors = listOf(Color(0xFF3F51B5),Color(0xFFF44336),Color(0xFF03A9F4),Color(0xFFFF9800))

    fun generateRandomPurchase(count: Int): List<PurchaseModel>{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return List(count){
            PurchaseModel(name = "Buy " + categories.random(), date = dateFormat.format(Date()), price = Random.nextInt(100, 2001), color = colors.random())
        }
    }
}
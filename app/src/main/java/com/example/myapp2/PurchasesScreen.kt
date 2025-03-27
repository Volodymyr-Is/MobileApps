package com.example.myapp2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp2.data.PurchaseModel
import com.example.myapp2.views.CardViewModel

@Composable
fun PurchasesScreen(viewModel: CardViewModel = viewModel()) {
    val purchases by viewModel.purchases
    val totalPrice by viewModel.totalPrice
    val otp by viewModel.otp
    val isGeneratedOTP by viewModel.isGeneratedOTP
    val isPayed by viewModel.isPayed
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Credit card",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(8.dp)
        ) {
            items(purchases) { purchase ->
                PurchaseItem(purchase) {
                    Toast.makeText(
                        context,
                        "${purchase.name}: $${purchase.price}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "-$${totalPrice}",
                        fontSize = 16.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "44110000 1234",
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = otp,
                onValueChange = {},
                label = { Text("Get OTP to verify transaction") },
                modifier = Modifier.weight(1f),
                enabled = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.generateOTP() },
                enabled = !isGeneratedOTP,
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text("Get OTP", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isPayed) {
                    Toast.makeText(
                        context,
                        "You have successfully paid $${totalPrice}. Thank you!",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.isPayed.value = true
                }
            },
            enabled = isGeneratedOTP && !isPayed,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE),
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            Text("Pay", color = Color.White)
        }
    }
}

@Composable
fun PurchaseItem(purchase: PurchaseModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(purchase.color)
        ) {
            Icon(
                painter = painterResource(
                    id = if (purchase.name == "Buy Camera") R.drawable.ic_camera
                    else R.drawable.ic_tv
                ),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = purchase.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Text(text = purchase.date, fontSize = 14.sp, color = Color.Gray)
        }

        Text(text = "-$${purchase.price}", fontSize = 16.sp, color = Color.Red)
    }
}

@Preview(showBackground = true)
@Composable
fun PurchasesScreenPreview() {
    PurchasesScreen()
}
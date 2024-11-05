package android.mobile.tamrice.screen

import android.content.Context
import android.mobile.tamrice.model.CartItem
import android.mobile.tamrice.model.OrderRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.mobile.tamrice.screen.ui.theme.TamRiceTheme
import android.mobile.tamrice.service.API_CLIENT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamRiceTheme {
                OrderFoodScreen()
            }
        }
    }
}

@Composable
fun OrderFoodScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)
    var orderRequests by remember { mutableStateOf(emptyList<OrderRequest>()) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (token != null) {
            API_CLIENT.apiService.getOrder("Bearer $token")
                .enqueue(object : Callback<List<OrderRequest>> {
                    override fun onResponse(
                        call: Call<List<OrderRequest>>,
                        response: Response<List<OrderRequest>>
                    ) {
                        if (response.isSuccessful) {
                            orderRequests = response.body() ?: emptyList()
                        } else {
                            message = "Lỗi: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<List<OrderRequest>>, t: Throwable) {
                        message = "Lỗi: ${t.message}"
                    }
                })

        } else {
            message = "Bạn chưa đăng nhập"
        }

    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp), // This padding will apply to the entire LazyColumn
        verticalArrangement = Arrangement.spacedBy(16.dp) // Add vertical spacing between items

    ) {
        orderRequests.forEach { order ->
            items(order.items) { item ->
                OdersCard(item, order.createdAt)
            }
        }
    }

}


@Composable
fun OdersCard(orderItem: CartItem, createdAt: String) {
    val totalPrice = orderItem.quantity * orderItem.product.price

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))

    ) {
        Row(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Column {
                Text(
                    text = "Tên món: ${orderItem.product.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Tổng tiền: $totalPrice VND")
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ngày đặt: $createdAt", // Hiển thị ngày đặt hàng
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )

            }

            Column {
                Text(text = "Số lượng: ${orderItem.quantity}")
            }


        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TamRiceTheme {
        OrderFoodScreen()
    }
}
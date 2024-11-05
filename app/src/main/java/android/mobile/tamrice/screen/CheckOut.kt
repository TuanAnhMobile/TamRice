package android.mobile.tamrice.screen

import android.content.Context
import android.mobile.tamrice.R
import android.mobile.tamrice.model.ApiResponse
import android.mobile.tamrice.model.CartItem
import android.mobile.tamrice.model.OrderRequest
import android.mobile.tamrice.service.API_CLIENT

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CheckOutScreen(cartItems: List<CartItem>) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))

    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Client(cartItems = cartItems)
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun Client(cartItems: List<CartItem>) {
    val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)

    var showDialog by remember { mutableStateOf(false) }
    var dialogThankYou by remember { mutableStateOf(false) }

    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

//    val userName = sharedPreferences.getString("USER_NAME", "Người dùng") ?: "Người dùng"


    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "")
        Text(text = "Đơn hàng", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFBBDEFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
//            Text(text = "Người dùng: $userName", fontSize = 18.sp)

            if (address.isEmpty() || phoneNumber.isEmpty()) {
                Text(
                    text = "Vui lòng mời bạn nhập thông tin địa chỉ nhận hàng",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            } else {
                Row {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "",
                        tint = Color(0xFFFF5722)
                    )
                    Column {
                        Text(text = "Địa chỉ: $address", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Số điện thoại: $phoneNumber", fontSize = 18.sp)
                    }
                }
            }
        }

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(16.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(15.dp))
            .clickable(
                onClick = { showDialog = true } // Hiển thị dialog khi nhấn vào Box
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Thêm địa chỉ giao hàng", fontSize = 18.sp)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Nhập thông tin địa chỉ") },
            text = {
                Column {
                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Địa chỉ") }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false // Đóng dialog sau khi lưu
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    if (dialogThankYou){
        AlertDialog(
            onDismissRequest = {dialogThankYou = false},
            title = {Text(text = "Cảm ơn bạn")},
            text = {Text(text = "Cảm ơn bạn đã đặt hàng của chúng tôi")},
            confirmButton = {
                Button(onClick = { dialogThankYou = false }) {
                Text("OK")
            } })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(cartItems) { cartItem ->
                Row(
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    AsyncImage(
                        model = cartItem.product.image_url, // Chắc chắn rằng product không null
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(60.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = cartItem.product.name, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${cartItem.quantity} x ${cartItem.product.price} VND",
                            fontSize = 16.sp
                        )

                    }
                }
                Divider(color = Color.LightGray, thickness = 1.dp) // Dòng kẻ ngang

            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White) // Đảm bảo nền trắng cho phần tổng tiền và thanh toán

        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Tổng tiền: $totalPrice VND", modifier = Modifier.padding(start = 10.dp))
            Button(
                onClick = {
                    if (token != null) {
                        val orderRequest = OrderRequest(
                            id = "",
                            address = address,
                            phoneNumber = phoneNumber,
                            items = cartItems,
                            totalAmount = totalPrice,
                            createdAt = currentDate,
                        )
                        API_CLIENT.apiService.checkout("Bearer $token", orderRequest)
                            .enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(
                                    call: Call<ApiResponse>,
                                    response: Response<ApiResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        message = "Đơn hàng của bạn đã được gửi thành công!"
                                        dialogThankYou = true
                                    } else {
                                        message = "Lỗi khi gửi đơn hàng: ${response.code()}"
                                    }
                                }

                                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                    message = "Lỗi: ${t.message}"
                                }

                            })
                    } else {
                        message = "Bạn chưa đăng nhập"
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Text(text = "Đặt hàng")
            }
        }

    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Demo() {
    CheckOutScreen(cartItems = listOf())

}
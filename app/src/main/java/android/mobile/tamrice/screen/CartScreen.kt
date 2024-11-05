import android.content.Context
import android.mobile.tamrice.R
import android.mobile.tamrice.model.CartItem
import android.mobile.tamrice.screen.FoodScreen
import android.mobile.tamrice.screen.ui.theme.TamRiceTheme
import android.mobile.tamrice.service.API_CLIENT
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson


class CartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // Thêm dòng này nếu chưa có

            ShoppingCartScreen(navController)
        }
    }
}

@Composable
fun ShoppingCartScreen( navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)

    // Biến để lưu danh sách sản phẩm trong giỏ hàng
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (token != null) {
            API_CLIENT.apiService.getCart("Bearer $token")
                .enqueue(object : Callback<List<CartItem>> {
                    override fun onResponse(
                        call: Call<List<CartItem>>,
                        response: Response<List<CartItem>>
                    ) {
                        if (response.isSuccessful) {
                            cartItems = response.body() ?: emptyList()
                            Log.d("API_RESPONSEkkkkkkk", cartItems.toString())

                        } else {
                            message = "Loi : ${response.code()}"
                        }
                    }

                    override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                        message = "Lỗi: ${t.message}"

                    }
                })
        } else {
            message = "Bạn chưa đăng nhập"
        }

    }
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity } // Tính tổng số tiền

    if (cartItems.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "",
                modifier = Modifier.size(200.dp),
                tint = Color(0xFFFF9800)
            )
            Text(
                text = "Chưa có sản phẩm nào",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp
            )


        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ) {
                items(cartItems) { cartItems ->
                    CartItemView(cartItem = cartItems)
                }

            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White) // Đảm bảo nền trắng cho phần tổng tiền và thanh toán
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tổng tiền: ${totalPrice}VND", // Hiển thị tổng tiền
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )

                // Nút thanh toán
                Button(
                    onClick = {
                        val json = Gson().toJson(cartItems)
                        navController.navigate("checkout/${Uri.encode(json)}")

                    },
                    modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )                ) {
                    Text("Thanh toán")
                    Spacer(modifier = Modifier.width(8.dp))
                    //hien thi so luong
                    Text("(${cartItems.sumOf { it.quantity }})", fontSize = 15.sp) // Hiển thị số lượng sản phẩm
                }
            }


        }

    }
}

//@Composable
//fun CartItemView(cartItem: CartItem) {
//    Row(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth()
//    ) {
//        val product = cartItem.product
//
//        if (product != null) {
//            AsyncImage(
//                model = product.image_url,
//                contentDescription = "Product Image",
//                modifier = Modifier
//                    .size(60.dp)
//                    .padding(4.dp),
//                placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
//            )
//
//            Column(
//                modifier = Modifier.padding(start = 16.dp)
//            ) {
//                Text(text = product.name)
//                Text(text = "Giá: ${product.price}")
//                Text(text = "Số lượng: ${cartItem.quantity}")
//            }
//        } else {
//            // Chỉ hiển thị thông báo lỗi nếu product thực sự là null
//            Text(text = "Sản phẩm không tồn tại", color = Color.Red)
//        }
//    }
//}

@Composable
fun CartItemView(cartItem: CartItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2))
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Không cần kiểm tra null nữa
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.product.name)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Giá: ${cartItem.product.price}")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon dấu trừ (-) với nền bo tròn
                IconButton(onClick = {

                }) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xffFE724C), CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xffFE724C)
                        )
                    }
                }

                Text(text = "${cartItem.quantity}")

                IconButton(onClick = {

                }) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xffFE724C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
//    ShoppingCartScreen()
}


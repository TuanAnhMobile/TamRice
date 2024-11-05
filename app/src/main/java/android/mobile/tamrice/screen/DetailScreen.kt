import android.mobile.tamrice.R

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.content.Context
import android.mobile.tamrice.model.AddToCartRequest
import android.mobile.tamrice.model.ApiResponse
import android.mobile.tamrice.service.API_CLIENT
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FoodDetail(
    id: String?,
    name: String?,
    image_url: String?,
    price: String?,
    description: String?
) {

    Log.d("FoodDetailScreen333", "Product ID: $id")
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)
    // Biến để quản lý thông báo
    var message by remember { mutableStateOf("") }

    var quantity by remember { mutableStateOf(1) } // mac dinh la 1

    // Biến trạng thái để quản lý tổng giá dựa trên số lượng sản phẩm
    val priceDouble = price?.toDoubleOrNull() ?: 0.0 // Chuyển giá sang số thực (Double)
    var totalPrice by remember { mutableStateOf(priceDouble) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.Black)
            Text(
                text = "Cưm Túm",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Icon(Icons.Default.Favorite, contentDescription = "", tint = Color.Red)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            AsyncImage(
                model = image_url,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .clip(RoundedCornerShape(30.dp)),
                placeholder = painterResource(id = R.drawable.img_rice),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name ?: "",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(Icons.Default.Star, contentDescription = "", tint = Color(0xFFF7990E))
                Text(text = "4.5")
            }

            Text(
                text = description ?: "",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon dấu trừ (-) với nền bo tròn
                IconButton(onClick = {
                    if (quantity > 1) {
                        quantity--
                        totalPrice = priceDouble * quantity // Cập nhật tổng giá
                    }
                }) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
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

                // Hiển thị số lượng
                Text(
                    text = quantity.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Icon dấu cộng (+) với nền bo tròn
                IconButton(onClick = {
                    quantity++
                    totalPrice = priceDouble * quantity // Cập nhật tổng giá
                }) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
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

        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${"%.2f".format(totalPrice)} VND", // Định dạng số thực với 2 chữ số thập phân
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        // Kiểm tra token
                        if (token != null) {
                            // Tạo yêu cầu thêm vào giỏ hàng
                            //productId = id!! (kiem tra chac chan rang productId ko Null)
                            val request = AddToCartRequest(
                                productId = id!!,
                                quantity = quantity
                            ) // Sử dụng ID sản phẩm và số lượng

                            API_CLIENT.apiService.addToCart(request, "Bearer $token")
                                .enqueue(object : Callback<ApiResponse> {
                                    override fun onResponse(
                                        call: Call<ApiResponse>,
                                        response: Response<ApiResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            message = response.body()?.msg
                                                ?: "Thêm thành công" // msg mac dinh
                                            //?. Toan tu an toan tranh gay ra loi NullPointerException
                                            // neu response.body()?.msg (null) se tra ve msg Thêm thành công
                                        } else {
                                            message = "Lỗi: ${response.code()}"
                                        }
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                        message = "Lỗi: ${t.message}"
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                })
                        } else {
                            Toast.makeText(context, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800) // Màu cam
                    ),
                    modifier = Modifier
                        .height(40.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "")
                    Text(text = "Thêm giỏ hàng")
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FoodDetail(
        "",
        "Com Tam Suon Nuong",
        "",
        "2000",
        "Cơm tấm truyền thống với sườn nướng thơm lừng và đậm vị. Ăn kèm với trứng ốp la, dưa chua và canh chua."
    )
}

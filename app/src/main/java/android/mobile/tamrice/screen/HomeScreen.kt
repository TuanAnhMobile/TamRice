package android.mobile.tamrice.screen

import android.mobile.tamrice.R
import android.mobile.tamrice.model.Products
import android.mobile.tamrice.model.User
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.mobile.tamrice.screen.ui.theme.TamRiceTheme
import android.mobile.tamrice.service.API_CLIENT
import android.mobile.tamrice.service.API_USER
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamRiceTheme {
                val navController = rememberNavController() // Thêm dòng này nếu chưa có
                FoodScreen(navController);
            }
        }
    }
}

@Composable
fun FoodScreen(navController: NavController) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .background(Color(0xFFF5F5F5))
    ) {
        TopBar()
        var query by remember { mutableStateOf("") }
        SearchBar(
            query = query, queryChange = { query = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Offer()
        Category(navController, query)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Align content to the start
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Hi Tuan Anh",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xffF57C00)
            )

            Text(
                text = "Cơm & Tấm",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xffFF4B3A)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.logo_comtam),
            contentDescription = "",
            modifier = Modifier.padding(end = 8.dp)
        )

    }
}

@Composable
fun SearchBar(
    query: String,
    queryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query, onValueChange = queryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Black,
                contentDescription = ""
            )
        },
        shape = RoundedCornerShape(30.dp),
        placeholder = { Text(text = "Tìm kiếm món ăn bạn thích....") }
    )
}

@Composable
fun Offer() {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF4D97), Color(0xFFFFA18F)) // Pink to Peach gradient
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .width(335.dp)
            .padding(horizontal = 16.dp)
            .height(150.dp)
            .background(brush = gradient, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(
            text = "Cơm tấm đậm đà, ngon như bữa cơm nhà ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle Order Now */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .width(131.dp)
                .height(39.dp) // Adjust width of the button
        ) {
            Text(text = "MUA NGAY", color = Color(0xFFFF4D97))
        }
    }
}

@Composable
fun Category(navController: NavController, query: String) {
    var selectedCategory by remember { mutableStateOf("Đồ ăn") }
    var products by remember { mutableStateOf(emptyList<Products>()) }

    //call API
    fun fetchProducts() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(API_USER::class.java)
        service.getProducts().enqueue(object : Callback<List<Products>> {
            override fun onResponse(
                call: Call<List<Products>>,
                response: Response<List<Products>>
            ) {
                products = response.body() ?: emptyList()
                Log.d("API_PRODUCTS2222 :", "Du lieu san pham :" + products)
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                Log.e("API Error555555", "Không thể lấy dữ liệu sản phẩm: ${t.message}")
            }

        })

    }
    LaunchedEffect(Unit) {
        fetchProducts()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .horizontalScroll(
                    rememberScrollState()
                ),

            ) {
            Button(
                onClick = {
                    selectedCategory = "Đồ ăn" //phải khớp với dữ liệu lúc thêm SP
                    Log.d("Category Selected", "Danh mục đã chọn: Cơm Sườn")
                },
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Đồ ăn") Color(0xFFFF9800) else Color.Gray // Màu cam hoặc màu xám
                )
            ) {
                Text("Đồ ăn")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Cơm sườn" //phải khớp với dữ liệu lúc thêm SP
                    Log.d("Category Selected", "Danh mục đã chọn: Cơm Sườn")
                },
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Cơm sườn") Color(0xFFFF9800) else Color.Gray // Màu cam hoặc màu xám
                )
            ) {
                Text("Cơm Sườn")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Thịt nướng"
                    Log.d("Category Selected", "Danh mục đã chọn: Thịt nướng")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Thịt nướng") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Thịt nướng")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Sườn bì chả"
                    Log.d("Category Selected", "Danh mục đã chọn: Sườn bì chả")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Sườn bì chả") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Sườn bì chả")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Bì chả"
                    Log.d("Category Selected", "Danh mục đã chọn: Bì chả")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Bì chả") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Bì chả")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Trứng chả"
                    Log.d("Category Selected", "Danh mục đã chọn: Trứng chả")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Trứng chả") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Trứng chả")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Sườn cốt lết"
                    Log.d("Category Selected", "Danh mục đã chọn: Sườn cốt lết")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Sườn cốt lết") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Sườn cốt lết")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Thập cẩm"
                    Log.d("Category Selected", "Danh mục đã chọn: Thập cẩm")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Thập cẩm") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Thập cẩm")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    selectedCategory = "Đặc biệt"
                    Log.d("Category Selected", "Danh mục đã chọn: Đặc biệt")
                },
                modifier = Modifier
                    .width(120.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(80.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (selectedCategory == "Đặc biệt") Color(0xFFFF9800) else Color.Gray
                )
            ) {
                Text(text = "Đặc biệt")
            }
            Spacer(modifier = Modifier.width(10.dp))

        }

    }

    //hien thi danh muc
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Số cột là 2
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp), // Padding xung quanh grid
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Khoảng cách giữa các cột
        verticalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa các hàng
    ) {
        items(products.filter { it.category == selectedCategory }
            .filter { it.name.contains(query, ignoreCase = true) })//loc tim kiem san pham
        { product ->
            ProductItem(product, navController = navController)
        }

    }
}


@Composable
fun ProductItem(product: Products, navController: NavController) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFE0B2))
            .padding(16.dp)
            .clickable {
//                navController.navigate("detail")
                //nen su dung Uri.encode()
                navController.navigate(
                    "detail/${Uri.encode(product.id)}/${Uri.encode(product.name)}/${
                        Uri.encode(
                            product.image_url
                        )
                    }/${product.price}/${Uri.encode(product.description)}"
                )

            }
    ) {

        AsyncImage(
            model = product.image_url,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
        )

        Log.d("Image UR2222444L", product.image_url)

        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = " ${product.price} vnd",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f)
            )
            Icon(Icons.Default.Star, contentDescription = "", tint = Color(0xFFF7990E))


        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    TamRiceTheme {
//        FoodScreen("")
    }
}
package android.mobile.tamrice.components

import FoodDetail
import ShoppingCartScreen
import android.mobile.tamrice.R
import android.mobile.tamrice.model.CartItem
import android.mobile.tamrice.screen.CheckOutScreen
import android.mobile.tamrice.screen.FoodScreen
import android.mobile.tamrice.screen.OrderFoodScreen
import android.mobile.tamrice.screen.ProfileClient
import android.mobile.tamrice.screen.ProfileScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson


@Composable
fun BottomAppBar() {
    // Tạo NavController để quản lý điều hướng
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val cartItems = remember { mutableStateListOf<CartItem>() }

    Scaffold(
        bottomBar = {
            if (currentRoute != "detail/{id}/{name}/{image_url}/{price}/{description}") {
                BottomAppBar(
                    modifier = Modifier.height(90.dp),
                    containerColor = Color(0xFFF8F8F8) // Màu nền của BottomAppBar
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "",
                                tint = if (currentRoute == "home") Color(0xFFFF9800) else Color.Gray
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "order",
                        onClick = { navController.navigate("order") },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.bill),
                                contentDescription = "",
                                tint = if (currentRoute == "order") Color(0xFFFF9800) else Color.Gray,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "cart",
                        onClick = { navController.navigate("cart") },
                        icon = {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "",
                                tint = if (currentRoute == "cart") Color(0xFFFF9800) else Color.Gray
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = { navController.navigate("profile") },
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "",
                                tint = if (currentRoute == "profile") Color(0xFFFF9800) else Color.Gray
                            )
                        }
                    )
                }
            }

        }
    ) { paddingValues ->
        // NavHost chứa các màn hình
        NavHost(
            navController = navController,
            startDestination = "home", // Màn hình khởi đầu
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable("home") { FoodScreen(navController) }
            composable("order") { OrderFoodScreen() }
            composable("cart") { ShoppingCartScreen(navController) }
            composable("profile") { ProfileClient() }
            composable("detail/{id}/{name}/{image_url}/{price}/{description}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val name = backStackEntry.arguments?.getString("name")
                val imageUrl = backStackEntry.arguments?.getString("image_url") // Sử dụng image_url
                val price = backStackEntry.arguments?.getString("price")
                val description = backStackEntry.arguments?.getString("description")

                FoodDetail(
                    id,
                    name,
                    imageUrl,
                    price,
                    description
                ) // Gọi hàm FoodDetail với các tham số
            }
            composable("checkout/{cart}") { backStackEntry ->
                val json = backStackEntry.arguments?.getString("cart") ?: ""
                val cartItems = Gson().fromJson(json, Array<CartItem>::class.java).toList()
                CheckOutScreen(cartItems)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    BottomAppBar()
}
package android.mobile.tamrice.service

import android.mobile.tamrice.model.AddToCartRequest
import android.mobile.tamrice.model.ApiResponse
import android.mobile.tamrice.model.CartItem
import android.mobile.tamrice.model.OrderRequest
import android.mobile.tamrice.model.Products
import android.mobile.tamrice.model.User
import retrofit2.Call // Sử dụng đúng import của Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface API_USER {

    @POST("/users/reg")
    fun registerUser(@Body user: User): Call<User>

    @POST("/users/login")
    fun loginUser(@Body user: User): Call<User>

    @GET("/productsClient")
    fun getProducts(): Call<List<Products>>

    @POST("/productsClient/cart")
    fun addToCart(
        @Body request: AddToCartRequest,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @GET("/productsClient/getCart")
    fun getCart(@Header("Authorization") token: String): Call<List<CartItem>>

    @POST("/productsClient/checkout")
    fun checkout(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): Call<ApiResponse>

    @GET("/productsClient/getOrder")
    fun getOrder(@Header("Authorization") token: String): Call<List<OrderRequest>>
}
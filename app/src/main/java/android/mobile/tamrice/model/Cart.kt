package android.mobile.tamrice.model

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    val productId: String,
    val quantity: Int
)

data class ApiResponse(
    val msg: String
    // phan hoi tu server tra ve
)

data class CartItem(
    @SerializedName("_id") val id: String,
    val product: Products,
    val quantity: Int
)




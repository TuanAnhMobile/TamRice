package android.mobile.tamrice.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class OrderRequest(
    @SerializedName("_id") val id: String,
    val address: String,
    val phoneNumber: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    @SerializedName("createdAt") val createdAt: String
)


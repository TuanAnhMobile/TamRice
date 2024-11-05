package android.mobile.tamrice.model


import com.google.gson.annotations.SerializedName


data class Products(
    @SerializedName("_id") val id: String, // Ánh xạ từ _id trong MongoDB
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
)

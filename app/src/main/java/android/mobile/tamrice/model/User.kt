package android.mobile.tamrice.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val userID: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    val token: String? = null // Thêm token với giá trị mặc định là null

)

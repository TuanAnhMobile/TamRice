package android.mobile.tamrice.screen

import android.content.Intent
import android.mobile.tamrice.MainActivity
import android.mobile.tamrice.R
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
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamRiceTheme {
                ProfileClient()
            }
        }
    }
}

@Composable
fun ProfileClient() {
    val gradientColor = listOf(
        Color(0xFFFFA726),
        Color(0xFFFFCC80),
        Color(0xFFFFFFFF)
    )
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(gradientColor),
            ),
        verticalArrangement = Arrangement.Center
    ) {
        ProfileCard()
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            EditProfileButton()
            LogoutButton { showLogoutDialog = true }
        }

        if (showLogoutDialog) {
            DialogLogOut { showLogoutDialog = false }
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp) // Bóng của Card
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Avatar
            Image(
                painter = painterResource(id = R.drawable.img_tuananh), // Thay bằng ảnh đại diện của bạn
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Cắt ảnh thành hình tròn
                    .border(
                        width = 4.dp, // Độ dày của viền
                        color = Color.LightGray, // Màu của viền
                        shape = CircleShape // Định dạng viền hình tròn
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị thông tin người dùng
            UserInfoField(label = "Name", value = "Nguyen Dinh Tuan Anh")
            Spacer(modifier = Modifier.height(8.dp))
            UserInfoField(label = "Username", value = "Tuan Anh")
            Spacer(modifier = Modifier.height(8.dp))
            UserInfoField(label = "Email", value = "tuananh@example.com")
        }

        ButtonRow()
    }
}

@Composable
fun UserInfoField(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        )
    }
}

@Composable
fun ButtonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        CircularButton(
            painter = painterResource(id = R.drawable.shopping_cart), // Thay icon cho ngôi nhà
            backgroundColor = Color(0xFFF0F0F0), // Màu xám sáng
            shadowElevation = 4.dp
        )
        CircularButton(
            painter = painterResource(id = R.drawable.order), // Icon giống nhau
            backgroundColor = Color(0xFFF0F0F0), // Màu xám sáng
            shadowElevation = 0.dp
        )
        CircularButton(
            painter = painterResource(id = R.drawable.favourite), // Icon giống nhau
            backgroundColor = Color(0xFFF0F0F0), // Màu xám sáng
            shadowElevation = 0.dp
        )
    }
}

@Composable
fun CircularButton(
    painter: Painter,
    backgroundColor: Color,
    shadowElevation: Dp
) {
    Box(
        modifier = Modifier
            .size(60.dp) // Kích thước của nút
            .shadow(elevation = shadowElevation, shape = CircleShape)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "Home Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(32.dp) // Kích thước của icon
        )
    }
}

@Composable
fun EditProfileButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
        modifier = Modifier
            .padding(8.dp)

    ) {
        Text(text = "Chỉnh sửa", color = Color.White)
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {

    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
        modifier = Modifier
            .padding(8.dp)

    ) {
        Text(text = "Đăng xuất", color = Color.White)
    }
}

@Composable
fun DialogLogOut(onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Đăng xuất") },
        text = { Text(text = "Bạn có muốn đăng xuất không?") },
        confirmButton = {
            TextButton(onClick = {
                Toast.makeText(context, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginAndRegisterScreen::class.java)
                context.startActivity(intent);
            }) {
                Text(text = "Có ")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Không")
            }
        }
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview5() {
    TamRiceTheme {
        ProfileClient()

    }
}
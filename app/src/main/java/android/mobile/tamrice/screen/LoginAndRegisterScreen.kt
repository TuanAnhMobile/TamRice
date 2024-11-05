package android.mobile.tamrice.screen

import android.content.Context
import android.content.Intent
import android.mobile.tamrice.MainActivity
import android.mobile.tamrice.R
import android.mobile.tamrice.model.User
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.mobile.tamrice.screen.ui.theme.TamRiceTheme
import android.mobile.tamrice.service.API_CLIENT
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginAndRegisterScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamRiceTheme {
                LoginAndRes();
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAndRes() {
    val gradientColor = listOf(
        Color(0xFFFFA726),
        Color(0xFFFFCC80),
        Color(0xFFFFFFFF)
    )
    val scaffoldState = rememberBottomSheetScaffoldState();
    val coroutineScope = rememberCoroutineScope();

    var bottomTypeSheet by remember {
        mutableStateOf(0)
    }

    val showBottomSheet: (Int) -> Unit = { type ->
        bottomTypeSheet = type
        coroutineScope.launch {
            scaffoldState.bottomSheetState.expand();
        }
    }

    val hideBottomSheet: () -> Unit = {
        coroutineScope.launch {
            scaffoldState.bottomSheetState.hide();
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState, sheetContent = {
            when (bottomTypeSheet) {
                1 -> BottomSheetSignUp() {
                    hideBottomSheet()
                }

                2 -> BottomSheetLogin() {
                    hideBottomSheet()
                }
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(gradientColor)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_comtam),
                contentDescription = "",
                modifier = Modifier
                    .size(350.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Xin Chào!",
                color = Color.Red,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Thưởng thức cơm tấm Việt Nam đích thực với ứng dụng của chúng tôi, cung cấp món ăn ngon và dễ dàng đặt hàng.",
                color = Color.Black, // Light gray color
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            BodySignUp(modifier = Modifier) {
                showBottomSheet(1)
            }

            Spacer(modifier = Modifier.height(20.dp))

            BodyLogin(modifier = Modifier) {
                showBottomSheet(2)
            }
            Spacer(modifier = Modifier.height(80.dp))

        }

    }

}

@Composable
fun BodySignUp(modifier: Modifier, onShowBottomSheet: () -> Unit) {
    Button(
        onClick = { onShowBottomSheet() },
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(70.dp)
            .padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xffF57C00)
        ),
        shape = RoundedCornerShape(60.dp)
    ) {
        Text(text = "Tạo tài khoản", color = Color.White)
    }

}

@Composable
fun BodyLogin(modifier: Modifier, onShowBottomSheet: () -> Unit) {
    Button(
        onClick = { onShowBottomSheet() },
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(70.dp)
            .padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xffFFFFFF)
        ),
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(2.dp, Color(0xFFF57C00))
    ) {
        Text(text = "Đăng nhập", color = Color(0xffF57C00))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSignUp(function: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current;

    Column(
        modifier = Modifier
            .height(500.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .clip(RoundedCornerShape(16.dp))
            .height(500.dp)
            .padding(16.dp),
    ) {
        Text(
            text = "Hello...",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Register",
            fontSize = 35.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your Name", color = Color.Black) },
            placeholder = { Text(text = "Enter Your Name", color = Color.Black) },
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your UserName ", color = Color.Black) },
            placeholder = { Text(text = "Enter Your UserName", color = Color.Black) },
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your Email", color = Color.Black) },
            placeholder = { Text(text = "Enter Your Email", color = Color.Black) },
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your Password", color = Color.Black) },
            placeholder = { Text(text = "Enter Your Password", color = Color.Black) },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (name.isNotBlank() && username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    //khoi tao doi tuong moi
                    val user = User(
                        userID = "", // Có thể bỏ qua ID khi đăng ký
                        name = name,
                        username = username,
                        email = email,
                        password = password
                    )
                    registerUser(user, context);
                } else {
                    Toast.makeText(context, "Please enter username and password", Toast.LENGTH_LONG)
                        .show()
                }

            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(70.dp)
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffF57C00)
            ),
            shape = RoundedCornerShape(60.dp),
        ) {
            Text(text = "Register", color = Color(0xffffffff))
        }

    }
}

fun registerUser(user: User, context: Context) {
    val call = API_CLIENT.apiService.registerUser(user)
    call.enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
                val userName = user.name
                if (token != null) {
                    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("USER_TOKEN", token).apply()
                    sharedPreferences.edit().putString("USER_NAME", userName).apply()
                }

                // Hiển thị Toast thông báo thành công
                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
            } else {
                // Hiển thị Toast thông báo lỗi
                Log.d(
                    "ERRRORR222 REG",
                    "onFailure: ${response.code()} ${response.errorBody()?.string()}"
                )

                Toast.makeText(
                    context,
                    "Đăng ký thất bại: ${response.message()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Log.d("ERRRORR REG", "onFailure: " + t.message)
            Toast.makeText(context, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    }
    )

}

@Composable
fun BottomSheetLogin(hideBottomSheet: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clip(RoundedCornerShape(16.dp))
            .height(500.dp)
            .padding(16.dp),
    ) {
        // Row chứa Text "Welcome Back !!!" và IconButton Close
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome Back !!!",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // Đẩy Text về phía bên trái
            )
            IconButton(
                onClick = { hideBottomSheet() },
            ) {
                Icon(
                    imageVector = Icons.Default.Close, // Sử dụng Icon Close mặc định
                    contentDescription = "Close",
                    tint = Color.Red // Màu của Icon
                )
            }
        }
        Text(
            text = "Login",
            fontSize = 35.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your Email", color = Color.Black) },
            placeholder = { Text(text = "Enter Your Email", color = Color.Black) },
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            label = { Text(text = "Your Password", color = Color.Black) },
            placeholder = { Text(text = "Enter Your Password", color = Color.Black) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Forgot Password ?",
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.End)
        )
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    val user = User(
                        userID = "", // Có thể bỏ qua ID khi đăng nhập
                        name = "", // Không cần thiết khi đăng nhập
                        username = "", // Không cần thiết khi đăng nhập
                        email = email,
                        password = password
                    )
                    loginUser(user, context);
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(70.dp)
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xffF57C00)
            ),
            shape = RoundedCornerShape(60.dp),
        ) {
            Text(text = "Login", color = Color(0xffffffff))
        }
    }

}

fun loginUser(user: User, context: Context) {
    val call = API_CLIENT.apiService.loginUser(user)
    call.enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    Log.d("TOKEN_VALUEEE", "Token day nay : $token")
                } else {
                    Log.d("TOKEN_VALUE333", "Không tìm thấy token")
                }

                if (token != null) {
                    val sharedPreferences =
                        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("USER_TOKEN", token).apply()
                }
                // Hiển thị Toast thông báo thành công
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent);
            } else {
                // Hiển thị Toast thông báo lỗi
                Log.d(
                    "ERRRORR3333 REG",
                    "onFailure: ${response.code()} ${response.errorBody()?.string()}"
                )

                Toast.makeText(
                    context,
                    "Đăng nhập thất bại: ${response.message()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Log.d("ERRRORR REG", "onFailure: " + t.message)
            Toast.makeText(context, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview2() {
    TamRiceTheme {
        LoginAndRes()
    }
}
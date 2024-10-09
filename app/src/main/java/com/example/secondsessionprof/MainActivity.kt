package com.example.secondsessionprof

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.secondsessionprof.data.emailSave.EmailManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val signUpViewModel = SignUpViewModel(App.instance.baseAuthManager, EmailManager(this))
            NavHost(navController = navController, startDestination = "signUp") {
                composable("signUp") {
                    SignUp(navController = navController, signUpViewModel)
                }
                composable("home") {
                    HomeScreen(navController)
                }
                composable(route = "forgotPassword") {
                    ForgotPassword(navController)
                }
                composable(route = "signIn") {
                    SignIn(navController)
                }
                composable(route = "otpVerification"){
                    OtpVerification(navController)
                }
                composable(route = "newPassword") {
                    NewPassword(navController)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController, signUpViewModel: SignUpViewModel) {

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showFullNameError by remember { mutableStateOf(false) }
    var showPhoneNumberError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showConfirmPasswordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(true) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(fullName, phoneNumber, email, password, confirmPassword) {
        showFullNameError = fullName.isBlank()
        showPhoneNumberError = phoneNumber.isBlank()
        showEmailError = email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        showPasswordError = password.isBlank() || password.length < 6
        showConfirmPasswordError = confirmPassword.isBlank() || password != confirmPassword
        isButtonEnabled = !showFullNameError && !showPhoneNumberError && !showEmailError &&
                !showPasswordError && !showConfirmPasswordError
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Create an account",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = "Complete the sign up process to get started"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Full Name"
        )
        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                showFullNameError = it.isBlank()
            },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showFullNameError,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Phone Number"
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it.filter { char -> char.isDigit() }
                showPhoneNumberError = it.isBlank() || !it.all { char -> char.isDigit() }
            },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showPhoneNumberError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Email Addres"
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() || it.isBlank()
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showEmailError,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Password"
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = it.isBlank() || it.length < 6
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showPasswordError,
            singleLine = true,
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Confirm Password"
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showConfirmPasswordError = it != password
            },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth(),
            isError = showConfirmPasswordError,
            singleLine = true,
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        var isChecked by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(text = "By ticking this box, you agree to our Terms and conditions and private policy")
        }

        Button(
            onClick = {
                val allFieldsValid = !showFullNameError && !showPhoneNumberError && !showEmailError &&
                        !showPasswordError && !showConfirmPasswordError

                if (allFieldsValid) {
                    Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    signUpViewModel.signUp(fullName = fullName, phone = phoneNumber, email = email, password = password)
                    navController.navigate("home")
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
                colors = if(isButtonEnabled) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(Color.LightGray)
            ,
                shape = RoundedCornerShape(4.dp)
        ){
                Text("Sign Up")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Already have an account?")
            TextButton(
                onClick = {
                    navController.navigate("signIn")
                }
            ) {
                Text("Sign In")
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }

    var showPassword by remember { mutableStateOf(true) }
    var isChecked by remember { mutableStateOf(false) }

    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(email, password) {
        showEmailError = email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        showPasswordError = password.isBlank() || password.length < 6
        isButtonEnabled = !showEmailError && !showPasswordError
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Welcome Back",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = "Fill in your email and password to continue"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Email Address"
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() || it.isBlank()
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            isError = showEmailError,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Password",
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = it.isBlank() || it.length < 6
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showPasswordError,
            singleLine = true,
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(text = "Remember password")
            TextButton(
                onClick = {
                    navController.navigate("forgotPassword")
                },
                modifier = Modifier.padding(start = 48.dp)
            ) {
                Text(text = "Forgot Password", color = Color.Blue, textAlign = TextAlign.End)
            }
        }
        val context = LocalContext.current
        Button(
            onClick = {
                val allFieldsValid = !showEmailError && !showPasswordError

                if (allFieldsValid) {
                    Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = if(isButtonEnabled) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(Color.LightGray)
            ,
            shape = RoundedCornerShape(4.dp)
        ){
            Text("Sign Up")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController){
    var email by remember { mutableStateOf("") }

    var showEmailError by remember { mutableStateOf(false) }

    var isButtonEnabled by remember { mutableStateOf(false) }

    var context = LocalContext.current

    LaunchedEffect(
        email
    ) {
        showEmailError = email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isButtonEnabled = !showEmailError
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Forgot Password",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = "Enter your email address"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "Email Address"
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showEmailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() || it.isBlank()
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            isError = showEmailError,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )
        Button(
            onClick = {
                val allFieldsValid = !showEmailError

                if (allFieldsValid) {
                    navController.navigate("otpVerification")
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = if(isButtonEnabled) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(Color.Red)
            ,
            shape = RoundedCornerShape(4.dp)
        ){
            Text("Send OTP")
        }
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Remember password? Back to")
            TextButton(
                onClick = {
                    navController.navigate("signIn")
                }
            ) {
                Text("Sign in")
            }
        }

    }

}

@Composable
fun OtpVerification(navController: NavController){

    var otpInput1 by remember { mutableStateOf("") }
    var otpInput2 by remember { mutableStateOf("") }
    var otpInput3 by remember { mutableStateOf("") }
    var otpInput4 by remember { mutableStateOf("") }
    var otpInput5 by remember { mutableStateOf("") }
    var otpInput6 by remember { mutableStateOf("") }

    var otpShowError1 by remember { mutableStateOf(false) }
    var otpShowError2 by remember { mutableStateOf(false) }
    var otpShowError3 by remember { mutableStateOf(false) }
    var otpShowError4 by remember { mutableStateOf(false) }
    var otpShowError5 by remember { mutableStateOf(false) }
    var otpShowError6 by remember { mutableStateOf(false) }

    var isVerified by remember { mutableStateOf(false) }

    var remainingTime by remember {mutableStateOf(60L)}
    var resendEnabled by remember {mutableStateOf(false)}


    LaunchedEffect(otpInput1) {

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "OTP Verification",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Enter the 6 digit numbers sent to your email"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            OutlinedTextField(
                value = otpInput1,
                onValueChange = {
                    otpInput1 = it
                    otpShowError2 = it.isBlank()},
                modifier = Modifier.size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = otpInput2,
                onValueChange = {
                    otpInput2 = it
                    otpShowError2 = it.isBlank()
                },
                modifier = Modifier
                    .size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = otpInput3,
                onValueChange = {
                    otpInput3 = it
                    otpShowError3 = it.isBlank()
                },
                modifier = Modifier
                    .size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = otpInput4,
                onValueChange = {
                    otpInput4 = it
                    otpShowError4 = it.isBlank()
                },
                modifier = Modifier
                    .size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = otpInput5,
                onValueChange = {
                    otpInput5 = it
                    otpShowError5 = it.isBlank()
                },
                modifier = Modifier
                    .size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = otpInput6,
                onValueChange = {
                    otpInput6 = it
                    otpShowError6 = it.isBlank()
                },
                modifier = Modifier
                    .size(40.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row {
            Text(
                text = "If you didn’t receive code,",
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            TextButton(
                onClick = {
                    if (resendEnabled){
                    //Тут отпишку надо снова отправить
                    }
                }
            ) {
            Text(
                text = if (resendEnabled) "resend" else "${formatTime(remainingTime)}",
                color = if (resendEnabled) Color.Blue else Color.DarkGray
            )
            }
        }

        Button(
            onClick = {
                val otpCheck = otpInput1 + otpInput2 + otpInput3 + otpInput4 + otpInput5 + otpInput6
                // проверить отп
                Log.d("OTP", "$otpCheck")
                navController.navigate("newPassword")
            },
            enabled = !otpInput1.isBlank() &&
                    !otpInput2.isBlank() &&
                    !otpInput3.isBlank() &&
                    !otpInput4.isBlank() &&
                    !otpInput5.isBlank() &&
                    !otpInput6.isBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify")
        }

        if (isVerified) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Verification Successful",
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPassword(navController: NavController){
    var showConfirmPasswordError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(true) }
    var isChecked by remember { mutableStateOf(false) }

    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(confirmPassword, password) {
        showPasswordError = password.isBlank() || password.length < 6
        showConfirmPasswordError = confirmPassword.isBlank() || password != confirmPassword
        isButtonEnabled = !showConfirmPasswordError
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = "New Password",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = "Enter new password"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Password"
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showPasswordError = it.isBlank() || it.length < 6
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showPasswordError,
            singleLine = true,
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Confirm Password"
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showConfirmPasswordError = it != password
            },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth(),
            isError = showConfirmPasswordError,
            singleLine = true,
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red
            )
        )



        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(text = "Remember password")
            TextButton(
                onClick = {
                    navController.navigate("forgotPassword")
                },
                modifier = Modifier.padding(start = 48.dp)
            ) {
                Text(text = "Forgot Password", color = Color.Blue, textAlign = TextAlign.End)
            }
        }
        val context = LocalContext.current
        Button(
            onClick = {
                val allFieldsValid = !showConfirmPasswordError && !showPasswordError

                if (allFieldsValid) {
                    Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = if(isButtonEnabled) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(Color.LightGray)
            ,
            shape = RoundedCornerShape(4.dp)
        ){
            Text("Sign Up")
        }
    }
}

private fun formatTime(seconds: Long): String {
    return String.format("%02d:%02d", seconds / 60, seconds % 60)
}

@Preview(showBackground = true)
@Composable
fun NewPasswordPreview(){
    NewPassword(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun OtpVerScreen(){
    OtpVerification(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview(){
    ForgotPassword(navController = rememberNavController())
}


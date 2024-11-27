package com.example.prac1.presentation.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.repository.AuthResult
import com.example.prac1.presentation.viewmodel.AuthViewModel

@Composable
fun SignScreen() {

}

@Composable
fun RegisterScreen(
    onNavigateToAuth: () -> Unit
) {

}

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signInState by authViewModel.signInState.collectAsState(null)
    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                text = stringResource(R.string.flowery),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле для ввода пароля
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка "Войти"
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.signIn(email = email, password = password)
                        //authViewModel.checkAuthorization()
                    } else {
                        Toast.makeText(context, "Fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = signInState == null
            ) {
                Text(text = if (signInState is AuthResult.Loading) "Loading..." else "Войти")
            }

            if (signInState is AuthResult.Error) {
                val error = (signInState as AuthResult.Error).exception.message
                Text(
                    text = error ?: "Неизвестная ошибка",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

            } else if (signInState is AuthResult.Success) Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}

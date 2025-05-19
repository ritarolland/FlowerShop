package com.example.prac1.presentation.composable

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.repository.AuthState
import com.example.prac1.presentation.viewmodel.AuthViewModel


@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToAuth: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val signInState by authViewModel.signInState.collectAsState(AuthState.Default)
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(top = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.create_account),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(R.string.below_welcome),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(modifier = Modifier.padding(16.dp))
            GlideImage(
                imageUrl = imageUri?.toString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(180.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable { pickImageLauncher.launch("image/*") },
                description = ""
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .border(
                        width = 1.dp, color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = ImageVector.vectorResource(R.drawable.user_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.name),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .border(
                        width = 1.dp, color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = ImageVector.vectorResource(R.drawable.mail_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.email),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .border(
                        width = 1.dp, color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = ImageVector.vectorResource(R.drawable.lock_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.password),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        StarPasswordTransformation()
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .clickable(interactionSource = null, indication = null) {
                            passwordVisible = !passwordVisible
                        },
                    imageVector = if (passwordVisible) ImageVector.vectorResource(R.drawable.visibility_off_icon)
                    else ImageVector.vectorResource(R.drawable.visibility_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            CustomButtonFilled(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                        authViewModel.signUp(
                            email = email,
                            password = password,
                            imageUri = imageUri,
                            name = name,
                            context = context
                        )
                    } else {
                        Toast.makeText(context, R.string.fill_in_all_fields, Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                enabled = (signInState is AuthState.Default || signInState is AuthState.Error),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (signInState is AuthState.Loading) stringResource(R.string.loading)
                    else stringResource(R.string.create_account),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = stringResource(R.string.have_account),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.login_small),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(interactionSource = null, indication = null) {
                        onNavigateToAuth()
                    }
                )
            }
            if (signInState is AuthState.Error) {
                val error =
                    "${context.getString(R.string.error)} ${(signInState as AuthState.Error).message}"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            } else if (signInState is AuthState.Success)
                Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show()

        }
    }
}




        /*Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
                text = stringResource(R.string.flowery),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))

                GlideImage(
                    imageUrl = imageUri?.toString(),
                    modifier = Modifier
                        .clickable { pickImageLauncher.launch("image/*") }
                        .align(Alignment.CenterHorizontally)
                        .size(256.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    description = ""
                )

            Spacer(modifier = Modifier.padding(16.dp))
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

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.signUp(email = email, password = password, imageUri = imageUri, context = context)
                    } else {
                        Toast.makeText(context, "Fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = signInState == null
            ) {
                Text(text = if (signInState is AuthState.Loading) "Loading..." else "Sign up")
            }

            if (signInState is AuthState.Error) {
                val error = (signInState as AuthResult.Error).exception.message
                Text(
                    text = error ?: "Неизвестная ошибка",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

            } else if (signInState is AuthState.Success)
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            Spacer(modifier = Modifier.padding(32.dp))
            Text(text = "Already have an account? Sign in now",
                modifier = Modifier.clickable { onNavigateToAuth() })
        }
*/
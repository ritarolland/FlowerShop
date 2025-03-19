package com.example.prac1.presentation.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.repository.AuthState
import com.example.prac1.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signInState by authViewModel.signInState.collectAsState(AuthState.Default)
    var passwordVisible by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = colorResource(R.color.Neutral20)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(top = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.Text)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(R.string.below_welcome),
                fontSize = 16.sp,
                color = colorResource(R.color.Neutral70)
            )
            Spacer(modifier = Modifier.padding(32.dp))
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .border(
                        width = 1.dp, color = colorResource(R.color.Neutral40),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = colorResource(R.color.Neutral10),
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
                    tint = colorResource(R.color.Neutral70)
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    textStyle = TextStyle(
                        color = colorResource(R.color.Text),
                        fontSize = 16.sp
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.email),
                            color = colorResource(R.color.Neutral60)
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
                        cursorColor = colorResource(R.color.Neutral70)
                    )
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .border(
                        width = 1.dp, color = colorResource(R.color.Neutral40),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = colorResource(R.color.Neutral10),
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
                    tint = colorResource(R.color.Neutral70)
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        color = colorResource(R.color.Text),
                        fontSize = 16.sp
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.password),
                            color = colorResource(R.color.Neutral60)
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
                        cursorColor = colorResource(R.color.Neutral70)
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
                    tint = colorResource(R.color.Neutral70)
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            CustomButtonFilled(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.signIn(email = email, password = password)
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
                    else stringResource(R.string.login),
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
                    text = stringResource(R.string.no_account),
                    fontSize = 16.sp,
                    color = colorResource(R.color.Text)
                )
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 16.sp,
                    color = colorResource(R.color.Primary),
                    modifier = Modifier.clickable(interactionSource = null, indication = null) {
                        onNavigateToRegister()
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

class StarPasswordTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString("*".repeat(text.length)),
            offsetMapping = OffsetMapping.Identity
        )
    }
}


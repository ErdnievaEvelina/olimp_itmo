package com.example.olimpitmo.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SignPage(navController: NavController){
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Text("Sign Up", fontSize = 28.sp)
        Spacer(modifier=Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email=it},
            label = {Text("Email")}
        )
        Spacer(modifier=Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password=it},
            label = {Text("password")},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier=Modifier.height(20.dp))

        Button(onClick = {navController.navigate("login")},
            modifier=Modifier.fillMaxWidth()) {

            Text("Sign Up")
        }



    }

}

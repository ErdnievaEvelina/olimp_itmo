package com.example.olimpitmo

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.olimpitmo.data.MatchesRepositoryImpl
import com.example.olimpitmo.data.RetrofitInstance
import com.example.olimpitmo.presentation.LoginPage
import com.example.olimpitmo.presentation.MainPage
import com.example.olimpitmo.presentation.MatchesViewModel
import com.example.olimpitmo.presentation.SearchPage
import com.example.olimpitmo.presentation.SignPage
import com.example.olimpitmo.ui.theme.OlimpITMOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OlimpITMOTheme {
              MainFunc()
            }
        }
    }
}

@Composable
fun MainFunc(){
    val navController=rememberNavController()

    val viewModel: MatchesViewModel=viewModel(
        factory=object: ViewModelProvider.Factory{
            override fun<T: ViewModel> create(modelClass: Class<T>):T{
                return MatchesViewModel(MatchesRepositoryImpl(RetrofitInstance.api)) as T
            }
        }
    )

    NavHost(navController, startDestination = "login"){
        composable("login"){LoginPage(navController)}
        composable("sign"){SignPage(navController)}
        composable("main"){MainPage(navController,viewModel)}
        composable("search"){SearchPage(navController,viewModel)
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OlimpITMOTheme {
    }
}
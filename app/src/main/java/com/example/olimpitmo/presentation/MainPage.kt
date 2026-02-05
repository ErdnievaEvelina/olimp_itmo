package com.example.olimpitmo.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainPage(
    navController: NavController,
    viewModel: MatchesViewModel){
    val matchesList by viewModel.matchesList.collectAsState()
    val context= LocalContext.current
    LaunchedEffect(key1=viewModel.showError) {
        viewModel.showError.collectLatest { show->
            if (show){
                Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
    if (matchesList.isEmpty()){
        Box(
            modifier=Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("MATCHES", color = Color.Blue)
            Button(onClick = {navController.navigate("search")}) {
                Text("Search Page")
            }
            LazyColumn {
                items(matchesList.size) { index ->
                    MatcheItem(matchesList[index])
                    Spacer(modifier = Modifier.size(15.dp))

                }
            }
        }
    }

}
@Composable
fun MatcheItem(match: DatesMatch){

        Card(
            modifier = Modifier.padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    match.season?.league?.country?.name?.let { Text(text = it) }
                    Spacer(modifier = Modifier.height(26.dp))
                    match.date?.let { Text(text = it.substring(11,16)) }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    match.homeTeam?.name?.let { Text(text = it, fontWeight = FontWeight.Bold) }
                    Spacer(modifier = Modifier.width(20.dp))
                    match.awayTeam?.name?.let { Text(text = it, fontWeight = FontWeight.Bold) }

                }
                Spacer(modifier = Modifier.height(15.dp))

                match.season?.league?.name?.let { Text(text = it) }

            }
        }

}
package com.example.olimpitmo.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("RememberReturnType")
@Composable
fun SearchPage(
    navController: NavController,
    viewModel: MatchesViewModel){
    val matchesList by viewModel.matchesList.collectAsState()
    val matcheListByLeague by viewModel.matchesListByLegue.collectAsState()
    var search by remember{ mutableStateOf("") }
    var searchDate by remember{ mutableStateOf("") }


        val filteredMatches = remember(matchesList,search,searchDate){
            if (search.isEmpty()&&searchDate.isEmpty()){
                matchesList
            };if (search.isNotEmpty()&& searchDate.isEmpty()){
                matchesList.filter { matche->
                    matche.season?.league?.name?.contains(search,ignoreCase=true) == true
                }
            };if (search.isEmpty()&& searchDate.isNotEmpty()){
                matcheListByLeague
        }else{
            matchesList
        }
        }

        Column(
            modifier=Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = {search=it},
                label = {Text("leagues")},
                singleLine = true,
                modifier=Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = searchDate,
                onValueChange = {searchDate=it
                    viewModel.getMatchesByLeagueDate(search)},
                label = {Text("date")},
                singleLine = true,
                modifier=Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {viewModel.getMatchesByLeagueDate(search)}) {
                Text("Find")
            }

            LazyColumn {
                items(filteredMatches.size) { index ->
                    MatcheItem(filteredMatches[index])
                    Spacer(modifier = Modifier.size(15.dp))

                }
            }

        }




}

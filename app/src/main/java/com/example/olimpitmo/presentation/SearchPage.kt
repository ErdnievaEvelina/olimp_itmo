package com.example.olimpitmo.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchPage(
    navController: NavController,
    viewModel: MatchesViewModel
) {
    val matchesList by viewModel.matchesList.collectAsState()
    val matchesListByDate by viewModel.matchesListByDate.collectAsState()
    val completedMatches by viewModel.completedMatches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var searchLeague by remember { mutableStateOf("") }
    var searchDate by remember { mutableStateOf("") }
    var archiveFromDate by remember { mutableStateOf("") }
    var archiveToDate by remember { mutableStateOf("") }
    var showArchiveDialog by remember { mutableStateOf(false) }
    var showArchiveSection by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.showError) {
        viewModel.showError.collectLatest { show ->
            if (show) {
                val message = viewModel.errorMessage.value ?: "Error"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    val displayedMatches = remember(
        matchesList,
        matchesListByDate,
        completedMatches,
        searchLeague,
        searchDate,
        showArchiveSection
    ) {
        when {
            showArchiveSection -> {
                if (searchLeague.isNotEmpty()) {
                    completedMatches.filter { match ->
                        match.season?.league?.name?.contains(searchLeague, ignoreCase = true) == true
                    }
                } else {
                    completedMatches
                }
            }
            searchDate.isNotEmpty() && searchLeague.isEmpty() -> {
                matchesListByDate
            }
            searchLeague.isNotEmpty() && searchDate.isEmpty() -> {
                matchesList.filter { match ->
                    match.season?.league?.name?.contains(searchLeague, ignoreCase = true) == true
                }
            }
            searchLeague.isNotEmpty() && searchDate.isNotEmpty() -> {
                matchesListByDate.filter { match ->
                    match.season?.league?.name?.contains(searchLeague, ignoreCase = true) == true
                }
            }
            else -> matchesList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Переключатель между обычным поиском и архивом
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    showArchiveSection = false
                    viewModel.clearCompletedMatches()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showArchiveSection) Color.Blue else Color.Gray
                )
            ) {
                Text("Поиск")
            }

            Button(
                onClick = {
                    showArchiveDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showArchiveSection) Color.Blue else Color.Gray
                )
            ) {
                Text("Архив")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!showArchiveSection) {
            OutlinedTextField(
                value = searchLeague,
                onValueChange = { searchLeague = it },
                label = { Text("Название лиги") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchDate,
                onValueChange = { searchDate = it },
                label = { Text("Дата (ГГГГ-ММ-ДД)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (searchDate.isNotEmpty()) {
                        viewModel.getMatchesByDate(searchDate)
                    } else {
                        Toast.makeText(context, "Введите дату", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Поиск по дате")
                }

                Button(onClick = {
                    searchLeague = ""
                    searchDate = ""
                    viewModel.loadInitialMatches()
                }) {
                    Text("Сброс")
                }
            }
        } else {
            // Секция архива
            Text(
                text = "Завершенные матчи",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Blue,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Фильтр по лиге в архиве
            OutlinedTextField(
                value = searchLeague,
                onValueChange = { searchLeague = it },
                label = { Text("Фильтр по лиге") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Кнопка для изменения диапазона дат архива
            Button(
                onClick = { showArchiveDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Изменить диапазон дат архива")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (archiveFromDate.isNotEmpty() && archiveToDate.isNotEmpty()) {
                Text(
                    text = "Период: $archiveFromDate - $archiveToDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Индикатор загрузки
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (displayedMatches.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showArchiveSection) {
                        "Нет завершенных матчей за выбранный период"
                    } else {
                        if (searchLeague.isNotEmpty() || searchDate.isNotEmpty()) {
                            "Матчи не найдены"
                        } else {
                            "Загрузка..."
                        }
                    }
                )
            }
        } else {
            LazyColumn {
                items(displayedMatches.size) { match ->
                    if (showArchiveSection) {
                        CompletedMatchItem(match = displayedMatches[match])
                    } else {
                        MatcheItem(match = displayedMatches[match])
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }

    if (showArchiveDialog) {
        ArchiveDateDialog(
            onDismiss = { showArchiveDialog = false },
            onConfirm = { from, to ->
                archiveFromDate = from
                archiveToDate = to
                showArchiveSection = true
                showArchiveDialog = false
                viewModel.getCompletedMatches(from, to)
            }
        )
    }
}

@Composable
fun ArchiveDateDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите диапазон дат") },
        text = {
            Column {
                OutlinedTextField(
                    value = fromDate,
                    onValueChange = { fromDate = it },
                    label = { Text("Начальная дата (ГГГГ-ММ-ДД)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = toDate,
                    onValueChange = { toDate = it },
                    label = { Text("Конечная дата (ГГГГ-ММ-ДД)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Пример: 2024-01-01",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                        onConfirm(fromDate, toDate)
                    }
                },
                enabled = fromDate.isNotEmpty() && toDate.isNotEmpty()
            ) {
                Text("Показать архив")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun CompletedMatchItem(match: DatesMatch) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    match.season?.league?.country?.name?.let {
                        Text(text = it, style = MaterialTheme.typography.bodySmall)
                    }
                    match.season?.league?.name?.let {
                        Text(text = it, fontWeight = FontWeight.Bold)
                    }
                }
                match.date?.let {
                    Text(
                        text = it.substring(0, 10), // Только дата
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                match.homeTeam?.name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = "${match.homeResult ?: "0"} - ${match.awayResult ?: "0"}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                        fontSize = 18.sp
                    )
                }

                match.awayTeam?.name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Статус матча
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Green.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Завершен",
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
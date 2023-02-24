package com.example.bl_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bl_app.api.TableModel
import com.example.bl_app.ui.components.seasons.GamesOfLastSeason
import com.example.bl_app.ui.components.seasons.GamesOfThisSeason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastGamesDays() {
    val viewModel: TableModel = viewModel()
    var selectedFilter by remember {
        mutableStateOf(0)
    }

    val entries = (1 until 35).map { it.toString() }
    var selectedEntry by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val resp = viewModel.fetchMatchData(day = null)
            val day =  resp.firstOrNull()?.group?.groupOrderID ?: return@withContext
            selectedEntry = day.toString()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SampleSpinner(
            entries,
            selected = selectedEntry ?: entries.first(),
            onSelectionChanged = {
                selectedEntry = it
            }
        )

        LazyRow {
            item {
                ElevatedFilterChip(
                    selected = selectedFilter == 0,
                    onClick = { selectedFilter = 0 },
                    label = { Text("Match day") },
                    leadingIcon = {
                        if (selectedFilter == 0) Icon(Icons.Default.Check, null)
                    }
                )
            }
            item {
                ElevatedFilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = { Text("This season") },
                    leadingIcon = {
                        if (selectedFilter == 1) Icon(Icons.Default.Check, null)
                    }
                )
            }
            item {
                ElevatedFilterChip(
                    selected = selectedFilter == 2, onClick = { selectedFilter = 2 },
                    label = { Text(text = "Last season") },
                    leadingIcon = {
                        if (selectedFilter == 2) Icon(Icons.Default.Check, null)
                    }
                )
            }
        }
        when (selectedFilter) {
            0 -> PastGamesView(selectedEntry?.toIntOrNull())
            1 -> GamesOfThisSeason()
            2 -> GamesOfLastSeason()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleSpinner(
    list: List<String>,
    selected: String,
    onSelectionChanged: (selection: String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) } // initial value

    Box {
        Column {
            OutlinedTextField(
                value = selected,
                onValueChange = { },
                label = { Text(text = "Match day") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, null) },
                readOnly = true
            )
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                list.forEach { entry ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSelectionChanged(entry)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = entry,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .align(Alignment.Start)
                            )
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        )
    }
}

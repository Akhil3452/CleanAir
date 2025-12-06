package uk.ac.tees.mad.cleanair.ui.screens.symptoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.cleanair.data.local.SymptomEntity
import uk.ac.tees.mad.cleanair.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogSymptomsScreen(viewModel: SymptomsViewModel = hiltViewModel(), navController: NavController) {
    val symptoms = viewModel.symptoms.collectAsState().value

    var symptomText by remember { mutableStateOf("") }
    var comfortLevel by remember { mutableStateOf(3f) }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Symptoms", color = White, fontWeight = FontWeight.Bold) },
                navigationIcon = {Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, tint = Color.White, modifier = Modifier.clickable { navController.popBackStack() })},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSky)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(LightSky, SkyBlue, DeepSky)))
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = symptomText,
                    onValueChange = { symptomText = it },
                    label = { Text("Symptoms (e.g. Coughing, Wheezing)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text("Comfort Level: ${comfortLevel.toInt()}", color = White, fontWeight = FontWeight.Bold)
                Slider(
                    value = comfortLevel,
                    onValueChange = { comfortLevel = it },
                    valueRange = 1f..5f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = White,
                        activeTrackColor = White
                    )
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        if (symptomText.isNotEmpty()) {
                            viewModel.addSymptom(
                                SymptomEntity(
                                    symptoms = symptomText,
                                    comfortLevel = comfortLevel.toInt(),
                                    notes = notes
                                )
                            )
                            symptomText = ""
                            notes = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = White)
                ) {
                    Text("Save Entry", color = DeepSky, fontWeight = FontWeight.Bold)
                }

                Divider(thickness = 1.dp, color = Color.White.copy(alpha = 0.3f))

                Text(
                    text = "Symptom History",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(symptoms) { entry ->
                        SymptomHistoryCard(entry) {
                            viewModel.deleteSymptom(entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SymptomHistoryCard(entry: SymptomEntity, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${entry.date}", fontSize = 12.sp, color = Neutral700)
            Spacer(modifier = Modifier.height(4.dp))
            Text(entry.symptoms, fontWeight = FontWeight.Bold, color = DeepSky)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Comfort Level: ${entry.comfortLevel}/5", color = Neutral700)
            if (!entry.notes.isNullOrEmpty()) {
                Text("Notes: ${entry.notes}", color = Neutral700)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = DeepSky)
                }
            }
        }
    }
}

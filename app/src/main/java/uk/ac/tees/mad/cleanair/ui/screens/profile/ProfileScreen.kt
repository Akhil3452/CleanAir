package uk.ac.tees.mad.cleanair.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.cleanair.ui.theme.*

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val name by viewModel.name
    val profileUrl by viewModel.profileUrl
    val loading by viewModel.loading
    var newName by remember { mutableStateOf(name) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfile(it) { success, msg ->
                if (!success) {
                    println("Upload failed: $msg")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFB))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileUrl),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Upload", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { viewModel.updateName(newName) { } },
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
            ) {
                Text("Save Changes")
            }

            Spacer(Modifier.height(40.dp))
            Text(
                text = "“Every breath matters.”",
                color = Color.Gray,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VeryUnhealthyRed)
            ) {
                Text("Logout")
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AquaTeal)
            }
        }
    }
}

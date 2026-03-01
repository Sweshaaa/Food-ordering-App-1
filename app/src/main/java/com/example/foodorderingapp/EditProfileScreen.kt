package com.example.foodorderingapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    var displayName by remember { mutableStateOf(user?.displayName ?: "") }
    val email = user?.email ?: ""
    var message by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = {
                displayName = it
                message = null
            },
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Prevent email editing here
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (displayName.isBlank()) {
                    message = "Display name cannot be empty"
                    return@Button
                }

                isLoading = true
                val updates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()

                user?.updateProfile(updates)
                    ?.addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            message = "Profile updated successfully"
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        } else {
                            message = task.exception?.message ?: "Update failed"
                        }
                    }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Updating..." else "Save Changes")
        }

        message?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }

        TextButton(
            onClick = { onBackClick() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
}

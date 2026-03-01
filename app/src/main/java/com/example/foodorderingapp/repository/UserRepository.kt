package com.example.foodorderingapp.user.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class UserRepository {

    private val auth = FirebaseAuth.getInstance()

    // Get current Firebase user
    fun getCurrentUser() = auth.currentUser

    // Update user display name
    fun updateDisplayName(displayName: String, onComplete: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onComplete(false, "No user logged in")
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // Sign out user
    fun signOut() {
        auth.signOut()
    }

    // Delete user
    fun deleteUser(onComplete: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.message)
            }
        }
    }
}
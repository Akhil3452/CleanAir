package uk.ac.tees.mad.cleanair.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val authenticated = mutableStateOf(auth.currentUser != null)

    fun signup(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    firestore.collection("users").document(auth.currentUser?.uid ?: "").set(
                        hashMapOf(
                            "profile" to null,
                            "name" to "Lannister",
                            "email" to email,
                            "password" to password
                        )
                    ).addOnCompleteListener {
                        authenticated.value = true
                        onResult(it.isSuccessful, it.exception?.message)
                    }.addOnFailureListener {
                        onResult(false, it.message)
                    }
                }.addOnFailureListener {
                    onResult(false, it.message)
                }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    authenticated.value = true
                    onResult(it.isSuccessful, it.exception?.message)
                }
                .addOnFailureListener {
                    onResult(false, it.message)
                }
        }
    }
}

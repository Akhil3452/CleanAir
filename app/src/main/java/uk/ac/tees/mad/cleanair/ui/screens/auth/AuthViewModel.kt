package uk.ac.tees.mad.cleanair.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.cleanair.data.remote.AqiApi
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    val authenticated = mutableStateOf(auth.currentUser != null)

    fun signup(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    firestore.collection("users").document(auth.currentUser?.uid ?: "").set(
                        hashMapOf(
                            "profile" to null,
                            "name" to "Lannister",
                            "email" to email,
                            "password" to password
                        )
                    ).addOnSuccessListener {
                        authenticated.value = true
                        onResult(true, null)
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
                .addOnSuccessListener {
                    authenticated.value = true
                    onResult(true, null)
                }
                .addOnFailureListener {
                    onResult(false, it.message)
                }
        }
    }
}

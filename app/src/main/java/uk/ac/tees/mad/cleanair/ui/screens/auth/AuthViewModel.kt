package uk.ac.tees.mad.cleanair.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val auth: FirebaseAuth
//) : ViewModel() {
//
//    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        viewModelScope.launch {
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener {
//                    onResult(it.isSuccessful, it.exception?.message)
//                }
//        }
//    }
//
//    fun signup(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        viewModelScope.launch {
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener {
//                    onResult(it.isSuccessful, it.exception?.message)
//                }
//        }
//    }
//}

package uk.ac.tees.mad.cleanair.ui.screens.profile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    val name = mutableStateOf("")
    val profileUrl = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener {
                name.value = it.getString("name") ?: ""
                profileUrl.value = it.getString("profile")
            }
    }

    fun uploadProfile(uri: Uri, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
//                loading.value = true
//                //val url = supabaseStorage.uploadProfileImage(uri)
//                if (url != null) {
//                    updateFirestoreProfile(url)
//                    profileUrl.value = url
//                    onComplete(true, null)
//                } else onComplete(false, "Upload failed")
            } catch (e: Exception) {
                onComplete(false, e.message)
            } finally {
                loading.value = false
            }
        }
    }

    private fun updateFirestoreProfile(url: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("profile", url)
    }

    fun updateName(newName: String, onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("name", newName)
            .addOnSuccessListener {
                name.value = newName
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun logout() {
        auth.signOut()
    }
}

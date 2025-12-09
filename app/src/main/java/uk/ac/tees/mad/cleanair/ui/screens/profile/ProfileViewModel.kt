package uk.ac.tees.mad.cleanair.ui.screens.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val name = androidx.compose.runtime.mutableStateOf("")
    val profileUrl = androidx.compose.runtime.mutableStateOf<String?>(null)
    val loading = androidx.compose.runtime.mutableStateOf(false)

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

    fun uploadProfile(context: Context, uri: Uri, onComplete: (Boolean, String?) -> Unit) {
        loading.value = true
        try {
            MediaManager.get()
                .upload(uri)
                .option("folder", "cleanair/profiles")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        loading.value = true
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        loading.value = false
                        onComplete(false, error?.description)
                    }

                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        loading.value = false
                        val imageUrl = resultData?.get("secure_url") as? String
                        if (imageUrl != null) {
                            updateFirestoreProfile(imageUrl)
                            profileUrl.value = imageUrl
                            onComplete(true, null)
                        } else {
                            onComplete(false, "Failed to get URL")
                        }
                    }
                })
                .dispatch()
        } catch (e: Exception) {
            e.printStackTrace()
            loading.value = false
            onComplete(false, e.message)
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

package uk.ac.tees.mad.cleanair.util
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.utils.ObjectUtils
import android.content.Context

object CloudinaryInstance {

    private var initialized = false

    fun init(context: Context) {
        if (!initialized) {
            val config = hashMapOf(
                "cloud_name" to "ddef8gfnw",
                "api_key" to "755668594987879",
                "api_secret" to "38Gk-VECKnnW1SFRllVE6koSSeM",
                "secure" to true
            )
            MediaManager.init(context, config)
            initialized = true
        }
    }
}
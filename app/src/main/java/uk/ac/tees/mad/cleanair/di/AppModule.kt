package uk.ac.tees.mad.cleanair.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.cleanair.data.local.AppDatabase
import uk.ac.tees.mad.cleanair.data.local.SymptomDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuthentication() : FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesFirebaseFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "cleanair_db").build()

    @Provides
    @Singleton
    fun provideSymptomDao(db: AppDatabase): SymptomDao = db.symptomDao()

}
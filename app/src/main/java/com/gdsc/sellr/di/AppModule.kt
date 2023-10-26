package com.gdsc.sellr.di

import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.content.Context
import com.gdsc.sellr.data.repository.MainRepo
import com.gdsc.sellr.data.repository.MainRepoImpl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        database: DatabaseReference,
        app: Application,
    ): MainRepo {
        return MainRepoImpl(
            database,
            app
        )
    }

//    @Provides
//    @Singleton
//    fun provideFirestoreInstance(): FirebaseFirestore =
//        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDatabaseReferenceItem(): DatabaseReference =
        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")


//    @Provides
//    @Singleton
//    fun provideFirebaseAuthInstance(): FirebaseAuth =
//        FirebaseAuth.getInstance()

//    @Provides
//    @Singleton
//    fun providerAuthRepository(
//        auth: FirebaseAuth,
//        db: FirebaseFirestore,
//        app: Application,
//        sharedPreferences: SharedPreferences
//    ): AuthenticationRepository {
//        return AuthenticationRepositoryImpl(auth, db, app, sharedPreferences)
//    }

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(@ApplicationContext app: Application): Application = app

//    @Provides
//    @Singleton
//    fun provideStorageReference(): StorageReference =
//        FirebaseStorage.getInstance().reference
//
//    @Provides
//    @Singleton
//    fun provideSharedPreferences(app: Application): SharedPreferences =
//        app.getSharedPreferences("sellr", Context.MODE_PRIVATE)

}
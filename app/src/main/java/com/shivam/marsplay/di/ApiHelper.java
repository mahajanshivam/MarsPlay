package com.shivam.marsplay.di;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiHelper {

    @Provides
    @Singleton
    StorageReference provideFirebaseStorageReference() {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        return mStorageRef;
    }
}

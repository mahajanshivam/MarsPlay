package com.shivam.marsplay.di;

import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiHelper.class, AppModule.class})
public interface ApiComponent {

    StorageReference provideFirebaseStorageReference();
}

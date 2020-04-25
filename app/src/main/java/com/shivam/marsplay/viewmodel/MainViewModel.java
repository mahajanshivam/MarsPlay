package com.shivam.marsplay.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.shivam.marsplay.repository.MainRepository;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {

    private Application application;
    private MainRepository mainRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        mainRepository = new MainRepository(application);
    }
}

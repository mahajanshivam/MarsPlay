package com.shivam.marsplay.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.shivam.marsplay.repository.MainRepository;

public class CameraViewModel extends AndroidViewModel {

    private Application application;
    private MainRepository mainRepository;

    public CameraViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        this.mainRepository = MainRepository.getInstance(application);
    }
}

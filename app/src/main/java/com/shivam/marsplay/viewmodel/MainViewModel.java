package com.shivam.marsplay.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shivam.marsplay.repository.MainRepository;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mainRepository;
    private MutableLiveData<Boolean> photoUploadLiveData;
    private MutableLiveData<String> urlArrayListMutableLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mainRepository = MainRepository.getInstance(application);
    }

    public MutableLiveData<Boolean> uploadPhoto(byte[] bytes) {

        photoUploadLiveData = mainRepository.UploadImageFile(bytes);
        return photoUploadLiveData;
    }

    public MutableLiveData<String> listAllImageFiles() {

        urlArrayListMutableLiveData = mainRepository.listAllFiles();
        return urlArrayListMutableLiveData;
    }
}

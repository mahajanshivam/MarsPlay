package com.shivam.marsplay.repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivam.marsplay.application.MarsPlayApplication;
import com.shivam.marsplay.util.Constants;

import java.util.ArrayList;

import dagger.Module;

@Module
public class MainRepository {

    private static MainRepository mainRepository = null;
    private StorageReference mStorageRef;
    private Application application;

    private MainRepository(Application application) {

        this.application = application;
        this.mStorageRef = MarsPlayApplication.get(application).getApiComponent().provideFirebaseStorageReference();

    }

    public static MainRepository getInstance(Application application) {

        if (mainRepository == null) {
            mainRepository = new MainRepository(application);
        }

        return mainRepository;
    }

    public MutableLiveData<Boolean> UploadImageFile(byte[] bytes) {

        MutableLiveData<Boolean> photoUploadLiveData = new MutableLiveData<>();

        StorageReference photoRef = mStorageRef.child(Constants.BUCKET_NAME + System.currentTimeMillis());

        photoRef.putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        photoUploadLiveData.postValue(true);


//                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Uri downloadUrl = uri;
//
//                            }
//                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(application.getApplicationContext(), "Upload Failed! " , Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                        photoUploadLiveData.postValue(false);

                    }
                });

        return photoUploadLiveData;
    }

    public MutableLiveData<String> listAllFiles() {

        MutableLiveData<String> arrayListMutableLiveData = new MutableLiveData<>();
        StorageReference listRef = mStorageRef.child(Constants.BUCKET_NAME);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//
//                        }

                        ArrayList<String> urlArrayList = new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {

                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    urlArrayList.add(downloadUrl.toString());
                                    arrayListMutableLiveData.postValue(downloadUrl.toString());

                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        arrayListMutableLiveData.postValue(null);
                    }
                });
        return arrayListMutableLiveData;
    }
}

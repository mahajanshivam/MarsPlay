package com.shivam.marsplay.repository;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivam.marsplay.application.MarsPlayApplication;

import java.io.File;
import java.io.IOException;

import dagger.Module;

@Module
public class MainRepository {


    private StorageReference mStorageRef;
    private Application application;

    public MainRepository(Application application) {

        this.application = application;
        this.mStorageRef = MarsPlayApplication.get(application).getApiComponent().provideFirebaseStorageReference();

        justCheck(application);
    }

    public void UploadImageFile() {

        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

//        riversRef.putBytes(bytes)
//        riversRef.putStream(stream)
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
//                                Toast.makeText(application.getApplicationContext(), "Upload success! URL - " + downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

//                        Toast.makeText(application.getApplicationContext(), "Upload Failed! " , Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void downloadFiles() {
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");

//            mStorageRef.getBytes(bytes)
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void justCheck(Application application) {
        Toast.makeText(application.getApplicationContext(), "just checking", Toast.LENGTH_SHORT).show();
    }

}

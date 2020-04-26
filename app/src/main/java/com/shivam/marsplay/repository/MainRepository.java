package com.shivam.marsplay.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivam.marsplay.application.MarsPlayApplication;
import com.shivam.marsplay.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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

//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//        Uri file = Uri.fromFile(photoFile);
        StorageReference photoRef = mStorageRef.child(Constants.BUCKET_NAME + System.currentTimeMillis());

        photoRef.putBytes(bytes)
//        riversRef.putStream(stream)
//        photoRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        photoUploadLiveData.postValue(true);


                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                Log.d("photolog", "Upload success! URL - " + downloadUrl.toString());

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

                        Log.d("photolog", "upload failed - " + exception.getMessage());
                        exception.printStackTrace();

                        photoUploadLiveData.postValue(false);

                    }
                });

        return photoUploadLiveData;
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


    public MutableLiveData<String> listAllFiles() {

        MutableLiveData<String> arrayListMutableLiveData = new MutableLiveData<>();

        StorageReference listRef = mStorageRef.child(Constants.BUCKET_NAME);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {

                        }

                        ArrayList<String> urlArrayList = new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.

                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Log.d("photolog", "item URL - " + downloadUrl.toString());

                                    urlArrayList.add(downloadUrl.toString());
                                    arrayListMutableLiveData.postValue(downloadUrl.toString());

                                    writeToFireBaseDatabase(downloadUrl.toString());
                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.d("photolog", "get list failure " + e.getMessage());
                        e.printStackTrace();
                        arrayListMutableLiveData.postValue(null);
                    }
                });

        return arrayListMutableLiveData;
    }

    public void writeToFireBaseDatabase(String url) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DATABASE_NAME);

        myRef.setValue(url);
    }

    public MutableLiveData<ArrayList<String>> getListFromFirebase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(Constants.DATABASE_NAME);
        MutableLiveData<ArrayList<String>> listMutableLiveData = new MutableLiveData<ArrayList<String>>();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterator<DataSnapshot> dataSnapshots = snapshot.getChildren().iterator();

                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    String url = dataSnapshotChild.getValue(String.class); // check here whether you are getting the TagName_Chosen

                    Log.d("photolog", "item from firebase - " + url);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("photolog", "read from firebase is cancelled");

            }
        });

        return listMutableLiveData;

    }
}

package com.shivam.marsplay.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.shivam.marsplay.R;
import com.shivam.marsplay.adapter.PhotoRecyclerAdapter;
import com.shivam.marsplay.databinding.ActivityMainBinding;
import com.shivam.marsplay.listener.UploadOptionSelectListener;
import com.shivam.marsplay.ui.fragment.BottomSheetFragment;
import com.shivam.marsplay.util.ItemOffsetDecoration;
import com.shivam.marsplay.viewmodel.MainViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ActivityMainBinding activityMainBinding;

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final int TAKE_PICTURE = 1;
    private static final int GALLERY_PICTURE = 2;
    private Uri imageUri;
    private PhotoRecyclerAdapter photoRecyclerAdapter;
    private ArrayList<String> urlArrayList = new ArrayList<>();
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        activityMainBinding.rvPhotos.setLayoutManager(new GridLayoutManager(this, 2));
        photoRecyclerAdapter = new PhotoRecyclerAdapter(this, urlArrayList);
        activityMainBinding.rvPhotos.setAdapter(photoRecyclerAdapter);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(MainActivity.this, R.dimen.item_offest);
        activityMainBinding.rvPhotos.addItemDecoration(itemDecoration);

        dialog = new ProgressDialog(MainActivity.this);
        initUI();

        mainViewModel.getListFromFirebase();
    }

    private void initUI() {


        activityMainBinding.tvLoading.setVisibility(View.VISIBLE);
        activityMainBinding.rvPhotos.setVisibility(View.GONE);

        mainViewModel.listAllImageFiles().observe(this, urlListObserver);
    }

    public void showOption(View view) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(uploadOptionSelectListener);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public void startCamera() {

        if (allPermissionsGranted()) {
            launchCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                launchCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchCamera() {

//        Intent intent = new Intent(MainActivity.this,CameraActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photo = new File(Environment.getExternalStorageDirectory() + "/shivam_marsplay", System.currentTimeMillis() + ".jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(photo));
//        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case TAKE_PICTURE:

//                    Uri selectedImage = imageUri;
//                    getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = getContentResolver();
//                    Bitmap bitmap;


                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri tempUri = getImageUri(photo);
                        File finalFile = new File(getRealPathFromURI(tempUri));

//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);

//                        imageView.setImageBitmap(bitmap);
                        Toast.makeText(this, finalFile.getAbsolutePath().toString(),
                                Toast.LENGTH_LONG).show();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        mainViewModel.uploadPhoto(byteArray).observe(MainActivity.this, photoUploadObserver);
                        dialog.setMessage("Uploading Photo.. Please wait");
                        dialog.show();
                        initUI();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("Camera", e.toString());
                    }

                    break;

                case GALLERY_PICTURE:

                    if (data != null) {

//                        Uri selectedImage = data.getData();
//                        String[] filePath = {MediaStore.Images.Media.DATA};
//                        Cursor c = getContentResolver().query(selectedImage, filePath,
//                                null, null, null);
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String selectedImagePath = c.getString(columnIndex);
//                        c.close();
//
//                        if (selectedImagePath != null) {
//
//                        }
//
//                        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        Log.d("photolog", "selectedImagePath = " + selectedImagePath);
//
//                        if (bitmap == null) {
//                            Log.d("photolog", "bitmap is null");
//                        }
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] byteArray = stream.toByteArray();
//
//                        mainViewModel.uploadPhoto(byteArray).observe(MainActivity.this, photoUploadObserver);

                        Uri pickedImage = data.getData();
                        //set the selected image to ImageView
                        activityMainBinding.ivDummy.setImageURI(pickedImage);

                        Bitmap bitmap = ((BitmapDrawable) activityMainBinding.ivDummy.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        Log.d("photolog", "selectedImagePath = " + selectedImagePath);

                        if (bitmap == null) {
                            Log.d("photolog", "bitmap is null");
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        mainViewModel.uploadPhoto(byteArray).observe(MainActivity.this, photoUploadObserver);
                        dialog.setMessage("Uploading Photo.. Please wait");
                        dialog.show();
                        initUI();
                    } else {
                        Toast.makeText(getApplicationContext(), "Image Selection failed", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private Observer<Boolean> photoUploadObserver = success -> {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (success) {

            Log.d("photolog", "inside photoUploadObserver success");

            initUI();
        } else {
            Toast.makeText(this, "Photo Upload Successful", Toast.LENGTH_LONG).show();
        }
    };

    private Observer<String> urlListObserver = url -> {

        if (url == null) {
            Log.d("photolog", "urlListObserver urlList  null ");
        } else {

            Log.d("photolog", "urlListObserver url  " + url);
            this.urlArrayList.add(url);

            if (urlArrayList.size() > 0) {
                activityMainBinding.tvLoading.setVisibility(View.GONE);
                activityMainBinding.rvPhotos.setVisibility(View.VISIBLE);
            }
            photoRecyclerAdapter.notifyDataSetChanged();
        }
    };

    private UploadOptionSelectListener uploadOptionSelectListener = new UploadOptionSelectListener() {
        @Override
        public void onGallerySelected() {
            selectGallery();
        }

        @Override
        public void onCameraSelected() {
            startCamera();
        }
    };

    private void selectGallery() {

//        Intent pictureActionIntent = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(
//                pictureActionIntent,
//                GALLERY_PICTURE);


        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, false);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Complete Action Using"), GALLERY_PICTURE);
    }
}

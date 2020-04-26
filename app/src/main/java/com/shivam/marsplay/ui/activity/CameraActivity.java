//package com.shivam.marsplay.ui.activity;
//
//import android.graphics.Matrix;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Size;
//import android.view.ScaleGestureDetector;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.AspectRatio;
//import androidx.camera.core.CameraControl;
//import androidx.camera.core.CameraInfo;
//import androidx.camera.core.CameraInfoUnavailableException;
//import androidx.camera.core.CameraX;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureConfig;
//import androidx.camera.core.Preview;
//import androidx.camera.core.PreviewConfig;
//import androidx.camera.core.impl.PreviewConfig;
//import androidx.databinding.DataBindingUtil;
//
//import com.shivam.marsplay.R;
//import com.shivam.marsplay.databinding.ActivityCameraBinding;
//
//import java.io.File;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class CameraActivity extends AppCompatActivity {
//
//    private ActivityCameraBinding activityCameraBinding;
//    private TextureView textureView;
//    private Executor executor = Executors.newSingleThreadExecutor();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        activityCameraBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
//        textureView = activityCameraBinding.textureView;
//
//        textureView.post(() -> startCamera());
//
//        textureView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
//
//            updateTransform();
//
//        });
//    }
//
//    private void startCamera() {
//
//        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen
//
//        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).setTargetResolution(screen)
//                .setLensFacing(CameraX.LensFacing.FRONT).build();
//        Preview preview = new Preview(pConfig);
//
//        preview.setOnPreviewOutputUpdateListener(
//                new Preview.OnPreviewOutputUpdateListener() {
//                    //to update the surface texture we  have to destroy it first then re-add it
//                    @Override
//                    public void onUpdated(Preview.PreviewOutput output) {
//                        ViewGroup parent = (ViewGroup) textureView.getParent();
//                        parent.removeView(textureView);
//                        parent.addView(textureView, 0);
//
//                        textureView.setSurfaceTexture(output.getSurfaceTexture());
//                        updateTransform();
//                    }
//                });
//
//        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
//        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);
//
//        activityCameraBinding.fabCamera.setOnClickListener(v -> {
//            File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
//            imgCap.takePicture(file, executor, new ImageCapture.OnImageSavedListener() {
//
//                @Override
//                public void onImageSaved(@NonNull File file) {
//                    String msg = "Pic captured at " + file.getAbsolutePath();
//                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onError(@NonNull ImageCapture.ImageCaptureError useCaseError, @NonNull String message, @Nullable Throwable cause) {
//                    String msg = "Pic capture failed : " + message;
//                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                    if (cause != null) {
//                        cause.printStackTrace();
//                    }
//                }
//            });
//
//        });
//
//        CameraX.bindToLifecycle(this, preview, imgCap);
//
//        try {
//            CameraInfo cameraInfo = CameraX.getCameraInfo(CameraX.LensFacing.FRONT);
//            CameraControl cameraControl = CameraX.getCameraControl(CameraX.LensFacing.FRONT);
//
//            ScaleGestureDetector.SimpleOnScaleGestureListener scaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//
//                @Override
//                public boolean onScale(ScaleGestureDetector detector) {
//
//
//                    float currentZoomRatio = cameraInfo.;
//                    val delta = detector.scaleFactor
//                    cameraControl.setZoomRatio(currentZoomRatio * delta);
//                    return true;
//                }
//            };
//            ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(CameraActivity.class, scaleListener);
//
//            textureView.setOnTouchListener((v, event) -> {
//                scaleGestureDetector.onTouchEvent(event);
//                return true;
//            });
//
//
//        } catch (CameraInfoUnavailableException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void updateTransform() {
//        Matrix mx = new Matrix();
//        float w = textureView.getMeasuredWidth();
//        float h = textureView.getMeasuredHeight();
//
//        float cX = w / 2f;
//        float cY = h / 2f;
//
//        int rotationDgr;
//        int rotation = (int) textureView.getRotation();
//
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                rotationDgr = 0;
//                break;
//            case Surface.ROTATION_90:
//                rotationDgr = 90;
//                break;
//            case Surface.ROTATION_180:
//                rotationDgr = 180;
//                break;
//            case Surface.ROTATION_270:
//                rotationDgr = 270;
//                break;
//            default:
//                return;
//        }
//
//        mx.postRotate((float) rotationDgr, cX, cY);
//        textureView.setTransform(mx);
//    }
//}

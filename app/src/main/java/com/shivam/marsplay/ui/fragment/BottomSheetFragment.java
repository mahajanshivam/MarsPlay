package com.shivam.marsplay.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shivam.marsplay.databinding.BottomSheetUploadOptionBinding;
import com.shivam.marsplay.listener.UploadOptionSelectListener;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetUploadOptionBinding binding;
    private UploadOptionSelectListener uploadOptionSelectListener;

    public BottomSheetFragment() {
    }

    public BottomSheetFragment(UploadOptionSelectListener uploadOptionSelectListener) {
        this.uploadOptionSelectListener = uploadOptionSelectListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = BottomSheetUploadOptionBinding.inflate(inflater, container, false);

        binding.ivGallery.setOnClickListener(v -> {
            uploadOptionSelectListener.onGallerySelected();
            dismiss();
        });

        binding.ivCamera.setOnClickListener(v -> {
            uploadOptionSelectListener.onCameraSelected();
            dismiss();
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}

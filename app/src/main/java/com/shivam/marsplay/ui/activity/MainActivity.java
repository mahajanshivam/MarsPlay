package com.shivam.marsplay.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.shivam.marsplay.R;
import com.shivam.marsplay.viewmodel.MainViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    }


}

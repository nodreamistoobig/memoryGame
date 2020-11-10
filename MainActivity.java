package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new TileView(this));
    }

    public void onClick(View v){
        setContentView(R.layout.activity_main);
    }
}
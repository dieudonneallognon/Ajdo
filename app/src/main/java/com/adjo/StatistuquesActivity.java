package com.adjo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StatistuquesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistuques);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}

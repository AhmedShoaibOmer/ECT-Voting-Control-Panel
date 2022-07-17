package com.aso.ectvotingcotrolpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aso.ectvotingcotrolpanel.ui.candidates.CandidatesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mCandidatesButton).setOnClickListener(view -> {
            startActivity(new Intent(this, CandidatesActivity.class));
        });
    }
}
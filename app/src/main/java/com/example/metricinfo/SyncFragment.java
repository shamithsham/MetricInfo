package com.example.metricinfo;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SyncFragment extends Fragment {

    private TextView welcomeText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_sync, container, false);

        welcomeText = view.findViewById(R.id.titleText);


        float textWidth = welcomeText.getPaint().measureText(welcomeText.getText().toString());

        Shader textShader = new LinearGradient(
                0, 0, textWidth, 0,
                new int[]{Color.parseColor("#671587"), Color.parseColor("#FF5722")},
                null,
                Shader.TileMode.CLAMP
        );

        welcomeText.getPaint().setShader(textShader);

        return view;
    }
}
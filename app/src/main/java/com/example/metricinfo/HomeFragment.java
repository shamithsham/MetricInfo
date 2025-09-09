package com.example.metricinfo;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private TextView welcomeText;
    private TextView statusText;
    private TextView checkInTimeText;
    private TextView checkOutStatusText;
    private TextView workDurationText;
    private AppCompatButton checkOutButton;
    private TextView locationText;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        float textWidth = welcomeText.getPaint().measureText(welcomeText.getText().toString());

        Shader textShader = new LinearGradient(
                0, 0, textWidth, 0,   // horizontal gradient (left → right)
                new int[]{Color.parseColor("#671587"), Color.parseColor("#FF5722")},
                null,
                Shader.TileMode.CLAMP
        );

        welcomeText.getPaint().setShader(textShader);

        return view;
    }

    private void initViews(View view) {

        checkOutButton = view.findViewById(R.id.checkOutButton);
        welcomeText = view.findViewById(R.id.titleText);



    }


}
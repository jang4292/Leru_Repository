package com.bpm202.SensorProject.Main.Exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bpm202.SensorProject.R;

public class NoSettingExerciseActivity extends AppCompatActivity {

    private Button btnSettingExercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_seeting_exercise);

        btnSettingExercise = findViewById(R.id.btn_setting_exercise);
        btnSettingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}

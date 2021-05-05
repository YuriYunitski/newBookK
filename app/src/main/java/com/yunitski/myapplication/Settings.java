package com.yunitski.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    RadioGroup group;
    RadioButton rub, euro, doll;
    Button apply;
    static boolean rubChecked, euroChecked, dollChecked;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        rubChecked = true;
        euroChecked = false;
        dollChecked = false;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currency", "rub");
        editor.apply();
        rub = findViewById(R.id.radioButtonRuble);
        euro = findViewById(R.id.radioButtonEuro);
        doll = findViewById(R.id.radioButtonDollar);
        group = findViewById(R.id.currencyGroup);
        apply = findViewById(R.id.set_apply);
        apply.setOnClickListener(this);
        rub.setChecked(true);

    }

    @Override
    public void onClick(View v) {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonRuble){
                    sharedPreferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currency", "rub");
                    editor.apply();
                } else if (checkedId == R.id.radioButtonEuro){
                    sharedPreferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currency", "euro");
                    editor.apply();
                } else if (checkedId == R.id.radioButtonDollar){
                    sharedPreferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currency", "dollar");
                    editor.apply();
                }
            }
        });
    }
}
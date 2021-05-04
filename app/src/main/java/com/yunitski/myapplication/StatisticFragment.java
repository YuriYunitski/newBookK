package com.yunitski.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        button = view.findViewById(R.id.gogo);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        startActivity(new Intent(getContext(), StatActivity.class));
    }
}
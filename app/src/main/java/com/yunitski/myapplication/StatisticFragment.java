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

import java.util.ArrayList;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    Button button;
    DBHelper dbHelper;

    String s1, s2, s3, s4;

    TextView totInc, totOutc, perc;
    SQLiteDatabase database;
    ArrayList<String> totalIncome, totalOutcome, operation, val;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        totInc = view.findViewById(R.id.tv_inc);
        totOutc = view.findViewById(R.id.tv_outc);
        perc = view.findViewById(R.id.tv_perc);
        s1 = "";
        s2 = "";
        s3 = "";
        s4 = "";
        totalIncome = new ArrayList<>();
        totalOutcome = new ArrayList<>();
        operation = new ArrayList<>();
        val = new ArrayList<>();
        dbHelper = new DBHelper(getContext());
        showStat();
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    private void showStat(){
        database = dbHelper.getWritableDatabase();
        String income = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165294';";
        Cursor c = database.rawQuery(income, null);
        int in = 0;
        int out = 0;
        String ssss = "0";
        if (!(c.getCount() <= 0)) {
            if (c.moveToFirst()) {
                do {
                    totalIncome.add(c.getString(0));
                } while (c.moveToNext());
            }
            String outcome = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165293';";
            c = database.rawQuery(outcome, null);
            if (c.moveToFirst()) {
                do {
                    totalOutcome.add(c.getString(0));
                } while (c.moveToNext());
            }
            c.close();
            database.close();

            for (int i = 0; i < totalIncome.size(); i++) {
                in += Integer.parseInt(totalIncome.get(i));
            }
            for (int i = 0; i < totalOutcome.size(); i++) {
                out += Integer.parseInt(totalOutcome.get(i));
            }
            double percent = (double) out / in * 100;
            String ss = "" + percent;
            char[] sss = ss.toCharArray();
            if (percent < 100) {
                ssss = "" + sss[0] + sss[1] + sss[2] + sss[3];
            } else {
                ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4];
            }
        }

        totInc.setText("Общий доход: " + in + "₽");
        totOutc.setText("Общий расход: " + out + "₽");
        perc.setText("Процент расхода от дохода: " + ssss + "%");
    }
}
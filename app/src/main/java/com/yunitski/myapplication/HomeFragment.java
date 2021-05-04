package com.yunitski.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yunitski.myapplication.Element;
import com.yunitski.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    TextView balance;
    FloatingActionButton fab;
    ArrayList<Element> elements;
    static boolean outcome, income;
    EditText inpValueET;
    RadioButton radioButtonOut, radioButtonIn;
    RadioGroup radioGroup;
    DBHelper dbHelper;
    ElementAdapter adapter;
    SharedPreferences sharedPreferences;
    int res;
    SQLiteDatabase database;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_list);
        balance = view.findViewById(R.id.balance);
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
        dbHelper = new DBHelper(getContext());

        setHasOptionsMenu(true);
        registerForContextMenu(balance);
        registerForContextMenu(recyclerView);
        loadBalance();
        updateUI();
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
        loadBalance();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        loadBalance();
    }

    @Override
    public void onStop() {
        super.onStop();
        updateUI();
        saveBalance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
    }

    @Override
    public void onClick(View v) {
        launchDialogAdd();
        updateUI();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clean_history) {
            getActivity().deleteDatabase(InputData.DB_NAME);
            loadBalance();
            updateUI();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.balance:
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.balance_context_menu, menu);
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.balance_delete:
                balance.setText("" + 0);
                saveBalance();
                updateUI();
                break;
        }
        return super.onContextItemSelected(item);
    }
    private void launchDialogAdd(){
        loadBalance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Добавить операцию");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        builder.setView(view);
        inpValueET = view.findViewById(R.id.input_value);
        radioButtonIn = view.findViewById(R.id.income);
        radioButtonOut = view.findViewById(R.id.outcome);
        radioButtonOut.setChecked(true);
        outcome = true;
        income = false;
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.income) {
                    income = true;
                    outcome = false;
                } else if (checkedId == R.id.outcome) {
                    income = false;
                    outcome = true;
                }
            }

        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (income){
                    if (!inpValueET.getText().toString().isEmpty()) {
                        res = R.drawable.ic_baseline_arrow_drop_up_24;
                        int i = Integer.parseInt(inpValueET.getText().toString());
                        int bal = Integer.parseInt(balance.getText().toString());
                        int k = bal + i;
                        balance.setText("" + k);
                        ContentValues cv = new ContentValues();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        cv.put(InputData.TaskEntry.VALUE, i);
                        cv.put(InputData.TaskEntry.TOTAL_VALUE, bal);
                        cv.put(InputData.TaskEntry.DATE, dateC());
                        cv.put(InputData.TaskEntry.OPERATION, res);
                        db.insert(InputData.TaskEntry.TABLE, null, cv);
                        db.close();
                        saveBalance();
                        updateUI();
                    }
                } else if(outcome){
                    if (!inpValueET.getText().toString().isEmpty()) {
                        res = R.drawable.ic_baseline_arrow_drop_down_24;
                        int i = Integer.parseInt(inpValueET.getText().toString());
                        int bal = Integer.parseInt(balance.getText().toString());
                        int k = bal - i;
                        balance.setText("" + k);
                        ContentValues cv = new ContentValues();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        cv.put(InputData.TaskEntry.VALUE, i);
                        cv.put(InputData.TaskEntry.TOTAL_VALUE, bal);
                        cv.put(InputData.TaskEntry.DATE, dateC());
                        cv.put(InputData.TaskEntry.OPERATION, res);
                        db.insert(InputData.TaskEntry.TABLE, null, cv);
                        db.close();
                        saveBalance();
                        updateUI();
                    }
                } else {
                    Toast.makeText(getContext(), "no operation selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        updateUI();
        builder.create().show();
    }
    public String dateC(){
        Calendar c = new GregorianCalendar();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        return d + "." + m + "." + y;
    }
    private void saveBalance(){
        sharedPreferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("b", balance.getText().toString());
        editor.apply();
    }
    void loadBalance(){
        sharedPreferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String bb =sharedPreferences.getString("b", "0");
        balance.setText(bb);
    }
    void updateUI(){
        elements = new ArrayList<Element>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(InputData.TaskEntry.TABLE, new String[]{InputData.TaskEntry._ID, InputData.TaskEntry.VALUE, InputData.TaskEntry.TOTAL_VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InputData.TaskEntry.DATE);
            int idx1 = cursor.getColumnIndex(InputData.TaskEntry.TOTAL_VALUE);
            int idx2 = cursor.getColumnIndex(InputData.TaskEntry.VALUE);
            int idx3 = cursor.getColumnIndex(InputData.TaskEntry.OPERATION);
            elements.add(0, new Element("" + cursor.getString(idx2), "" + cursor.getString(idx1),  "" + cursor.getString(idx), cursor.getInt(idx3)));
        }
        if (adapter == null) {
            adapter = new ElementAdapter(getLayoutInflater(), elements);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(elements);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }
}
package com.yunitski.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private String dollar, euro, belRub, pound;
    TextView t1, t2, t3, t4;
    Button button;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        t1 = view.findViewById(R.id.dollar);
        t2 = view.findViewById(R.id.euro);
        t3 = view.findViewById(R.id.bel_rub);
        t4 = view.findViewById(R.id.phound);
        t1.setText("Доллар США");
        t2.setText("Евро");
        t3.setText("Белорусский рубль");
        t4.setText("Фунт стерлингов Соединенного королевства");
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        init();
        return view;
    }


    @Override
    public void onClick(View v) {
        t1.setText(dollar);
        t2.setText(euro);
        t3.setText(belRub);
        t4.setText(pound);
    }


    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb(){
        try {
            doc = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get();

            Elements elements = doc.getElementsByTag("tbody");
            Element element = elements.get(0);
            Elements elementsFromTable = element.children();
            String s = elementsFromTable.get(11).toString();
            String[] st = s.split("<td>");
            String[] name = st[4].split("<");
            String[] value = st[5].split("<");
            String s2 = elementsFromTable.get(12).toString();
            String[] st2 = s2.split("<td>");
            String[] name2 = st2[4].split("<");
            String[] value2 = st2[5].split("<");
            String s3 = elementsFromTable.get(4).toString();
            String[] st3 = s3.split("<td>");
            String[] name3 = st3[4].split("<");
            String[] value3 = st3[5].split("<");
            String s4 = elementsFromTable.get(29).toString();
            String[] st4 = s4.split("<td>");
            String[] name4 = st4[4].split("<");
            String[] value4 = st4[5].split("<");
            dollar = name[0] + " : " + value[0] + getString(R.string.ruble);
            euro = name2[0]  + " : " + value2[0] + "₽";
            belRub = name3[0] + " : " + value3[0] + "₽";
            pound = name4[0] + " : " + value4[0] + "₽";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
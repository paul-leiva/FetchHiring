package com.example.fetchhiring;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String urlString = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    Button viewListButton;
    List<EntryRow> resultRows = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewListButton = findViewById(R.id.viewListButton);

        viewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListButton.setText("Loading");
                retrieveData();
                EntryRowAdapter adapter = new EntryRowAdapter(MainActivity.this, R.layout.adapter_view_layout, resultRows);
                ListView table1 = (ListView) findViewById(R.id.table1);
                table1.setAdapter(adapter);
            }
        });
    }

    private void retrieveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultRows = getSiteData();

                // main work
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // construct the table
                        viewListButton.setText("View List");
                    }
                });
            }
        }).start();
    }

    private List<EntryRow> getSiteData() {
        String responseData = "";
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection(); // open the connection
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                responseData += line + "\n";
            }
        }
        catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect(); // close the connection
        }

        String [] items = responseData.split("\\}");

        List<List<Integer>> includedRows = new ArrayList<>();

        for (int i = 1; i < items.length - 1; i++) {
            String [] values = items[i].split(", ");

            String name = values[2].substring(values[2].indexOf(" ") + 1); // second sorting criteria (if not null or blank)
            String listIdString = values[1].substring(values[1].indexOf(" ") + 1); // first sorting criteria
            String idString = values[0].substring(values[0].indexOf(" ") + 1);

            // Filter out any items where "name" is blank or null
            if (name.length() == 2 || name.equals("null")) {
                // Filter out case
                continue;
            }
            else { // if not filtered out, place into list
                int id = Integer.parseInt(idString);
                int listId = Integer.parseInt(listIdString);
                name = name.substring(name.indexOf(" ") + 1, name.length() - 1);
                List<Integer> thisRow = new ArrayList<>(); // this row = [listId, name, id]
                thisRow.add(listId);
                thisRow.add(Integer.parseInt(name));
                thisRow.add(id);
                includedRows.add(thisRow);
            }
        }

        Collections.sort(includedRows, new Comparator<List<Integer>>()
        {
            public int compare(List<Integer> e1, List<Integer> e2)
            {
                //@Overrides
                if (e1.get(0).compareTo(e2.get(0)) == 0) { // first sort by listId
                    return e1.get(1).compareTo(e2.get(1)); // then sort by name
                }
                else {
                    return e1.get(0).compareTo(e2.get(0));
                }
            }
        });

        List<EntryRow> listOfERs = new ArrayList<>();

        for (List<Integer> x : includedRows) {
            EntryRow er = new EntryRow(x.get(0).toString(), x.get(1).toString(), x.get(2).toString());
            listOfERs.add(er);
        }

        //return includedRows; // includedRows.toString();

        return listOfERs;
    }
}
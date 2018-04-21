package com.example.fredastaire.mybowlingscores;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Fred Astaire on 10/6/2017.
 */

public class HistoryActivity extends Activity {

    private ArrayList<BowlingScores> allBowlingScores;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.history_layout);

        // get the data from the App
        MyBowlingScoresApplication app = (MyBowlingScoresApplication) getApplication();
        allBowlingScores = app.getAllBowlingScores();
        BowlingScores.updateRunningAverage(allBowlingScores);

        //  View  ---  Adapter   ---  Data

        ArrayAdapter<BowlingScores> adapter = new ArrayAdapter<BowlingScores>(this,
                R.layout.history_row,
                allBowlingScores
        );

        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                verifyDeleteRow(position);
                Log.d("DEBUG", "I hear the item selection: " + position);
            }

        });

    }

    private void verifyDeleteRow(final int position) {
        // pop up a dialog to confirm delete row
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this Series?")
                .setMessage("Do you want to delete this data? \n"
                        + allBowlingScores.get(position))
                .setIcon(R.drawable.ic_dialog_my_bowling_scores)
                .setCancelable(false)
                .setNegativeButton("No! Leave it there!",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Delete",

                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            BowlingScores toDelete = allBowlingScores.get(position);

                            MyBowlingScoresApplication app = (MyBowlingScoresApplication) getApplication();
                            app.deleteBowlingScores(toDelete);
                            onCreate(savedInstanceState);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

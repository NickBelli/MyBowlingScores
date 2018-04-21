package com.example.fredastaire.mybowlingscores;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.example.fredastaire.mybowlingscores.BowlingScoresDatabaseHelper.*;

import com.example.fredastaire.mybowlingscores.BowlingScores;

import java.util.ArrayList;

/**
 * Created by Fred Astaire on 9/21/2017.
 */

public class MyBowlingScoresApplication extends Application {

    private ArrayList<BowlingScores> allBowlingScores;
    private SQLiteDatabase bowlingScoresDB;

    @Override
    public void onCreate() {
        super.onCreate();

        BowlingScoresDatabaseHelper databaseHelper = new BowlingScoresDatabaseHelper(this);
        bowlingScoresDB = databaseHelper.getWritableDatabase();

        // TODO get the data out of the database
        //allBowlingScores = new ArrayList<BowlingScores>();
        readBowlingScoresFromDB();

    }

    private void readBowlingScoresFromDB() {
        allBowlingScores = new ArrayList<BowlingScores>();

        Cursor bowlingScoresCursor;

        bowlingScoresCursor = bowlingScoresDB.query(BOWLING_SCORES_TABLE,
                new String[]{RECORD_ID, DATE, GAME1, GAME2, GAME3},
                null, null, null, null, DATE);

        bowlingScoresCursor.moveToFirst();
        BowlingScores tempBS;

        if (!bowlingScoresCursor.isAfterLast()) {
            do {
                long id = bowlingScoresCursor.getLong(0);
                long dateEpoch = bowlingScoresCursor.getLong(1);
                int game1 = bowlingScoresCursor.getInt(2);
                int game2 = bowlingScoresCursor.getInt(3);
                int game3 = bowlingScoresCursor.getInt(4);

                tempBS = new BowlingScores(id, dateEpoch, game1, game2, game3);

                allBowlingScores.add(tempBS);

                Log.d("Bowling Database", tempBS.toString());
            } while (bowlingScoresCursor.moveToNext());
        }
        bowlingScoresCursor.close();

    }

    public void addBowlingScores(BowlingScores bowlingScores) {
        assert bowlingScores != null;

        ContentValues cv = new ContentValues();
        cv.put(BowlingScoresDatabaseHelper.DATE, bowlingScores.getDateEpoch()); //USE EPOCH
        cv.put(BowlingScoresDatabaseHelper.GAME1, bowlingScores.getGame1());
        cv.put(BowlingScoresDatabaseHelper.GAME2, bowlingScores.getGame2());
        cv.put(BowlingScoresDatabaseHelper.GAME3, bowlingScores.getGame3());

        Log.d("Bowling Database", "Before Inserting a record " + bowlingScores);

        long idPassedBack = bowlingScoresDB.insert(BowlingScoresDatabaseHelper.BOWLING_SCORES_TABLE, null, cv);
        bowlingScores.setId(idPassedBack);

        Log.d("Bowling Database", "After Inserting a record " + bowlingScores);
        allBowlingScores.add(bowlingScores);
    }

    public void deleteBowlingScores(BowlingScores toDelete) {
        int count = bowlingScoresDB.delete(BOWLING_SCORES_TABLE,
                RECORD_ID + "=" + toDelete.getId(), null);
        Log.d("DATABASE", "Deleted: " + count + "record." + " " + toDelete);

        allBowlingScores.remove(toDelete);
        BowlingScores.updateRunningAverage(allBowlingScores);

    }

    public ArrayList<BowlingScores> getAllBowlingScores() {
        return allBowlingScores;
    }

    private void setAllBowlingScores(ArrayList<BowlingScores> allBowlingScores) {
        this.allBowlingScores = allBowlingScores;
    }

}

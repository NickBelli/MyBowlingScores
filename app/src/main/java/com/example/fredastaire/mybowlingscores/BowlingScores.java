package com.example.fredastaire.mybowlingscores;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Fred Astaire on 9/20/2017.
 */

public class BowlingScores implements Comparable<BowlingScores> {

    private long id;
    private int game1;
    private int game2;
    private int game3;
    private Date date;
    private double runningAverage;

    public BowlingScores(int game1, int game2, int game3, Date date) {
        this.game1 = game1;
        this.game2 = game2;
        this.game3 = game3;
        this.date = date;
    }

    public BowlingScores(long id, long dateEpoch, int game1, int game2, int game3) {
        this.id = id;
        setDateEpoch(dateEpoch);
        this.game1 = game1;
        this.game2 = game2;
        this.game3 = game3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGame1() {
        return game1;
    }

    public void setGame1(int game1) {
        this.game1 = game1;
    }

    public int getGame2() {
        return game2;
    }

    public void setGame2(int game2) {
        this.game2 = game2;
    }

    public int getGame3() {
        return game3;
    }

    public void setGame3(int game3) {
        this.game3 = game3;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDateEpoch() {
        return date.getTime() / 1000;
    }

    public void setDateEpoch(long seconds) {
        date = new Date(seconds * 1000);
    }

    public double getRunningAverage() {
        return runningAverage;
    }

    public void setRunningAverage(double runningAverage) {
        this.runningAverage = runningAverage;
    }

    public int calculateSeriesScore() {
        return game1 + game2 + game3;
    }

    public double calculateSeriesAverage() {
        return (double) calculateSeriesScore() / 3;
    }

    public static void updateRunningAverage(ArrayList<BowlingScores> allBowlingScores) {

        int weeks = 1;
        long total = 0;

        Collections.sort(allBowlingScores);

        if (allBowlingScores.size() > 0) {

            for (BowlingScores bs : allBowlingScores) {
                total += bs.calculateSeriesScore();
                bs.setRunningAverage((double) total / (weeks * 3));
                weeks++;
            }

        }
    }

    public String toString() {
        String result;

        // "ID:" + id + " " +

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        result = df.format(date) + "  "
                + game1 + "     " + game2 + "      " + game3
                + "      " + calculateSeriesScore() + "   "
                + String.format("%.1f", calculateSeriesAverage()) + "   "
                + String.format("%.1f", runningAverage);
        return result;
    }

    @Override
    public boolean equals(Object that) {
        BowlingScores bs = (BowlingScores) that;

        return this.date.equals(bs.date);
    }

    @Override
    public int compareTo(BowlingScores that) {
        int difference;

        difference = this.date.compareTo(that.date);
        return difference;
    }
}

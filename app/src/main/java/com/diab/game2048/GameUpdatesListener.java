package com.diab.game2048;

/**
 * Created by Mohamed Magdy on 10/16/2015.
 */
public interface GameUpdatesListener {

    /**
     *will be called when the score is changed
     * @param newScore
     */
    public void onScoreChanged (int newScore);

    /**
     * will be called in the case of the user lost the game
     */
    public void onGameLoss ();


    /**
     * will be called in the case of the user win the game
     */
    public void onGameWin ();
}

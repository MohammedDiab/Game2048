package com.diab.game2048;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * The Class is responsible for displaying the game panel , and the game logic
 *
 */
public class GamePanelView extends GridLayout {

    /**
     * copy from the current context
     */
    private Context ctx;

    /**
     * number of the columns used to draw the grid, the rows will be equal to that number too
     * since we are drawing a square
     */
    private int columnCount = 4;

    /**
     * the matrix that will represent the tiles and their positions on the grid
     */
    private TileView[][] tilesMatrix;

    /**
     * array to keep track of the empty spots in the grid
     */
    private ArrayList<Point> emptyTiles;

    /**
     *  the tile width , it will be calculated based on the grid width and the column count
     */
    private int tileWidth = 0;

    /**
     *  the tile height, it will calculated based on the grid height and the rows count
     */
    private int tileHeight = 0;

    /**
     *  this over lay is used for the animation purpose, since we can't really move the tiles inside the grid itself
     */
    private OverLayView overLayView;

    /**
     *  listener that helps the panel to communicate to the main activity
     */
    private GameUpdatesListener listener = null;

    /**
     *  the current score
     */
    private int score = 0;

    /**
     *  array to keep track of the dummy tiles that are responsible for the animation
     */
    private List<TileView> dummyTiles;


    public GamePanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initialize the members
        ctx = context;
        emptyTiles =new ArrayList<Point>();
        dummyTiles = new ArrayList<TileView>();
        tilesMatrix = new TileView[columnCount][columnCount];

        setColumnCount(columnCount);
        setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryBackground));
        this.setOnTouchListener(touchListener);

    }

    /**
     * it will called when the app will draw the grid
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        tileWidth = (w) / 4;
        tileHeight = (h) / 4;

        drawPanel(tileWidth, tileHeight);
    }

    /**
     * draw the panel and add two random tiles
     * @param tileWidth
     * @param tileHeight
     */
    private void drawPanel(int tileWidth, int tileHeight) {
        // fill in the matrix with all the empty tiles
        for (int row = 0; row < columnCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                TileView tmp = new TileView(ctx, 0, ctx.getResources().getColor(R.color.defaultTileBackgroundColor));
                tilesMatrix[row][col] = tmp;
                emptyTiles.add(new Point(row, col));
                addView(tmp, tileWidth, tileHeight);
            }
        }

       /* addTileAtSpecificLocation(0,0,2);
        addTileAtSpecificLocation(0,1,4);
        addTileAtSpecificLocation(0,2,8);
        addTileAtSpecificLocation(0,3,16);
        addTileAtSpecificLocation(1,0,32);
        addTileAtSpecificLocation(1,1,64);
        addTileAtSpecificLocation(1,2,128);
        addTileAtSpecificLocation(1,3,256);
        addTileAtSpecificLocation(1,3,256);
        addTileAtSpecificLocation(2,0,512);
        addTileAtSpecificLocation(2,1,1024);
        addTileAtSpecificLocation(2,2,2048);*/


        // add two random tiles
        addRandomTile();
        addRandomTile();
    }


    public void setOverLayView(OverLayView view) {
        overLayView = view;
    }

    public void setGameUpdatesListener(GameUpdatesListener gameUpdatesListener) {
        listener = gameUpdatesListener;
    }

    /**
     * check if the user can do more possible moves, when we can't insert any more tiles to the grid
     * @return boolean
     */
    private boolean hasMorePossibleMoves() {
        //scan the matrix for adjacent tiles either on rows or cols
        for (int row = 0; row < columnCount; row++) {
            for (int col = 0; col < columnCount; col++) {

                //check if the user reached 2048!
                if (tilesMatrix[row][col].getNum()==2048) {
                    listener.onGameWin();
                }

                // check the element on the right
                if ((col < columnCount - 1 && tilesMatrix[col][row].getNum() == tilesMatrix[col + 1][row].getNum())
                        //check the element on the left
                        || (col > 0 && tilesMatrix[col][row].getNum() == tilesMatrix[col - 1][row].getNum())
                        // check the element below
                        || (row < columnCount - 1 && tilesMatrix[col][row].getNum() == tilesMatrix[col][row + 1].getNum())
                        //check the element above
                        || (row > 0 && tilesMatrix[col][row].getNum() == tilesMatrix[col][row - 1].getNum())

                        || (tilesMatrix[row][col].getNum()==0)
                        ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * update the empty tiles matrix
     */
    private void updateEmptyTiles() {
        emptyTiles.clear();
        for (int row = 0; row < columnCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                if (tilesMatrix[row][col].getNum() == 0) {
                    emptyTiles.add(new Point(row, col));
                }
            }
        }
    }

    /**
     *  add random tile to the grid
     */
    private void addRandomTile() {
        updateEmptyTiles();
        //check first if we still have empty tiles
        if (emptyTiles.size() > 0) {
            Random random = new Random();
            int max = emptyTiles.size();
            int min = 0;
            int tileRandomIndex = random.nextInt(max - min) + min;
            Point randomTilePoint = emptyTiles.get(tileRandomIndex);
            // get number from 0 to 4 , if the number is less than or equal 2 the tile value will be two either wise it will be 4
            int tileValue = random.nextInt(5);
            tileValue = tileValue <= 2 ? 2 : 4;
            tilesMatrix[randomTilePoint.x][randomTilePoint.y].setNum(tileValue);
            emptyTiles.remove(randomTilePoint);
        } else {
            //no more room, the user may be lost the game but we need to check if there are more possible moves
            if (!hasMorePossibleMoves()) {
                if (listener != null) {
                    listener.onGameLoss();
                }
            }
        }
    }

    /**
     * used for testing purposes, to add a tile at a specific location in the matrix
     * @param x
     * @param y
     * @param value
     */
    private void addTileAtSpecificLocation(int x, int y, int value) {
        //check first if we still have empty tiles
        if (emptyTiles.size() > 0) {
            tilesMatrix[x][y].setNum(value);
        }

    }

    /**
     * listen on the touch events to know which direction the user is going (up, down , left , right)
     */
    private OnTouchListener touchListener = new OnTouchListener() {


        private float downX,downY,upX,upY;
        /**
         * allow threshold, to the moves so we will give the user some space to swipe, instead of tacking a swipe for a short move
         */
        private int threshold = 30 ;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //store the down points so that we can compare them against the up to know the swipe direction
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    //get the up points and compare them to the down points so we can get the direction
                    upX = event.getX();
                    upY = event.getY();

                    float xDifference = upX - downX;
                    float yDifference = upY - downY;

                    // means we move on the x axes , so it is either left or right
                    if (Math.abs(xDifference) > Math.abs(yDifference)) {
                        if (xDifference< -1*threshold) {
                            swipeLeft();
                        } else if (xDifference > threshold) {
                            swipeRight();
                        }
                        //means we move on the y axes, so it is either up or down
                    } else {
                        if (yDifference < -1*threshold) {
                            swipeUp();
                        } else if (yDifference > threshold){
                            swipeDown();
                        }
                    }
                    if (!hasMorePossibleMoves()) {
                        if (listener!=null) {
                            listener.onGameLoss();
                        }
                    }

                    break;
            }
            return false;
        }
    };

    /**
     *  handle the swipe left action
     */
    private void swipeLeft() {
        boolean shouldAddAnotherTile = false;

        for (int row = 0; row < columnCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                for (int elementNextToBorder = col + 1; elementNextToBorder < columnCount; elementNextToBorder++) {
                    if (tilesMatrix[row][elementNextToBorder].getNum() > 0) {

                        if (tilesMatrix[row][col].getNum() <= 0) {
                            moveTile(tilesMatrix[row][elementNextToBorder], tilesMatrix[row][col], elementNextToBorder, col, row, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][elementNextToBorder].getNum());
                            tilesMatrix[row][elementNextToBorder].setNum(0);
                            col--;
                            shouldAddAnotherTile= true;

                        } else if (tilesMatrix[row][col].getNum() == tilesMatrix[row][elementNextToBorder].getNum()) {
                            moveTile(tilesMatrix[row][elementNextToBorder], tilesMatrix[row][col], elementNextToBorder, col, row, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][elementNextToBorder].getNum() * 2);
                            tilesMatrix[row][elementNextToBorder].setNum(0);
                            updateScore(tilesMatrix[row][col].getNum());
                            shouldAddAnotherTile= true;
                        }

                        break;
                    }
                }
            }
        }

        if (shouldAddAnotherTile) {
            addRandomTile();
        }
    }

    /**
     *  handle the swipe right action
     */
    private void swipeRight() {
        boolean shouldAddAnotherTile = false;

        for (int row = 0; row < columnCount; row++) {
            for (int col = columnCount - 1; col >= 0; col--) {
                for (int elementNextToBorder = col - 1; elementNextToBorder >= 0; elementNextToBorder--) {
                    if (tilesMatrix[row][elementNextToBorder].getNum() > 0) {

                        if (tilesMatrix[row][col].getNum() <= 0) {
                            moveTile(tilesMatrix[row][elementNextToBorder], tilesMatrix[row][col], elementNextToBorder, col, row, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][elementNextToBorder].getNum());
                            tilesMatrix[row][elementNextToBorder].setNum(0);

                            col++;
                            shouldAddAnotherTile = true;
                        } else if (tilesMatrix[row][col].getNum() == tilesMatrix[row][elementNextToBorder].getNum()) {
                            moveTile(tilesMatrix[row][elementNextToBorder], tilesMatrix[row][col], elementNextToBorder, col, row, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][elementNextToBorder].getNum() * 2);
                            tilesMatrix[row][elementNextToBorder].setNum(0);
                            updateScore(tilesMatrix[row][col].getNum());
                            shouldAddAnotherTile = true;
                        }
                        break;
                    }
                }
            }
        }
        if (shouldAddAnotherTile) {
            addRandomTile();
        }
    }

    /**
     *  handle the swipe up action
     */
    private void swipeUp() {
        boolean shouldAddAnotherTile = false;

        for (int col = 0; col < columnCount; col++) {
            for (int row = 0; row < columnCount; row++) {
                for (int elementNextToBorder = row + 1; elementNextToBorder < columnCount; elementNextToBorder++) {
                    if (tilesMatrix[elementNextToBorder][col].getNum() > 0) {

                        if (tilesMatrix[row][col].getNum() <= 0) {
                            moveTile(tilesMatrix[elementNextToBorder][col], tilesMatrix[row][col], col, col, elementNextToBorder, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[elementNextToBorder][col].getNum());
                            tilesMatrix[elementNextToBorder][col].setNum(0);
                            row--;
                            shouldAddAnotherTile = true;

                        } else if (tilesMatrix[row][col].getNum() == tilesMatrix[elementNextToBorder][col].getNum()) {
                            moveTile(tilesMatrix[elementNextToBorder][col], tilesMatrix[row][col], col, col, elementNextToBorder, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][col].getNum() * 2);
                            tilesMatrix[elementNextToBorder][col].setNum(0);
                            updateScore(tilesMatrix[row][col].getNum());
                            shouldAddAnotherTile = true;
                        }
                        break;
                    }
                }
            }
        }
        if (shouldAddAnotherTile) {
            addRandomTile();
        }
    }

    /**
     * handle the swipe down action
     */
    private void swipeDown() {
        boolean shouldAddAnotherTile = false;

        for (int col = 0; col < columnCount; col++) {
            for (int row = columnCount - 1; row >= 0; row--) {
                for (int elementNextToBorder = row - 1; elementNextToBorder >= 0; elementNextToBorder--) {
                    if (tilesMatrix[elementNextToBorder][col].getNum() > 0) {

                        if (tilesMatrix[row][col].getNum() <= 0) {
                            moveTile(tilesMatrix[elementNextToBorder][col], tilesMatrix[row][col], col, col, elementNextToBorder, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[elementNextToBorder][col].getNum());
                            tilesMatrix[elementNextToBorder][col].setNum(0);
                            row++;
                            shouldAddAnotherTile = true;

                        } else if (tilesMatrix[row][col].getNum() == tilesMatrix[elementNextToBorder][col].getNum()) {
                            moveTile(tilesMatrix[elementNextToBorder][col], tilesMatrix[row][col], col, col, elementNextToBorder, row);
                            tilesMatrix[row][col].setNum(tilesMatrix[row][col].getNum() * 2);
                            tilesMatrix[elementNextToBorder][col].setNum(0);
                            updateScore(tilesMatrix[row][col].getNum());
                            shouldAddAnotherTile = true;
                        }
                        break;
                    }
                }
            }
        }
        if (shouldAddAnotherTile) {
            addRandomTile();
        }
    }

    /**
     * update the score, and update the main activity throw the listener
     * @param scoreUpdate
     */
    private void updateScore(int scoreUpdate) {
        score += scoreUpdate;
        if (listener != null) {
            listener.onScoreChanged(score);
        }
    }

    /**
     * move the tile from the origin to the destination throw an animation
     * @param origin
     * @param destination
     * @param originX
     * @param destX
     * @param originY
     * @param destY
     */
    public void moveTile(final TileView origin, final TileView destination, int originX, int destX, int originY, int destY) {

        final TileView dummyTile = getDummyTile(origin.getNum());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(tileWidth, tileHeight);
        lp.leftMargin = originX * tileWidth;
        lp.topMargin = originY * tileHeight;
        dummyTile.setLayoutParams(lp);
        if (destination.getNum() <= 0) {
            destination.getTextView().setVisibility(View.INVISIBLE);
        }
        TranslateAnimation ta = new TranslateAnimation(0, tileWidth * (destX - originX), 0, tileHeight * (destY - originY));
        ta.setDuration(180);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //destination.setNum(destination.getNum());
                destination.getTextView().setVisibility(View.VISIBLE);
                hideTheDummyTile(dummyTile);
            }
        });
        dummyTile.startAnimation(ta);
    }

    /**
     * @param num
     * @return TileView
     */
    private TileView getDummyTile(int num) {
        TileView dummy;
        //if we have one from the last animation then we should get it back
        if (dummyTiles.size() > 0) {
            dummy = dummyTiles.remove(0);
        } else {
            // in case of we don't have any one, we should create a new one
            // and add it to the over lay layer, so it can be moved throw the animation
            dummy = new TileView(getContext());
            overLayView.addView(dummy);
        }
        dummy.setVisibility(View.VISIBLE);
        dummy.setNum(num);
        return dummy;
    }

    /**
     * hide the dummy tile
     * @param c
     */
    private void hideTheDummyTile(TileView c) {
        c.setVisibility(View.INVISIBLE);
        c.setAnimation(null);
        dummyTiles.add(c);
    }

    public void restart ()  {
        emptyTiles.clear();
        score=0;
        if (listener!=null) {
            listener.onScoreChanged(score);
        }
        removeAllViews();
        drawPanel(tileWidth,tileHeight);
    }
}

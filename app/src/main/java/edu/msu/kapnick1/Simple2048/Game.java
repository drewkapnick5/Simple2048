package edu.msu.kapnick1.Simple2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Game {
    public static final int SWIPE_THRESHOLD = 50;
    public static final int SWIPE_VELOCITY_THRESHOLD = 50;
    //Paint for filling in the area for game
    private Paint fillPaint;
    //Paint for filling in the tile for game
    private Paint tilePaint;
    //Paint for outlining the area the game is in
    private Paint backGroundPaint;
    //Paint for writing score
    private Paint scorePaint;
    //Paint for writing tile numbers
    private Paint numTile;

    /**
     * The size of the game in pixels
     */
    private int gameSize;

    /**
     * The size of a tile in pixels
     */
    private float tileSize;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;
    /**
     * Left margin in pixels
     */
    private float tileMargin;
    private float tileM_X;
    private float tileM_Y;
    /**
     * Array containing board positions
     */
    private List<Pair> rowPairs = new ArrayList<>();
    private List<Pair> colPairs = new ArrayList<>();

    private int[][] board =  {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};

    //Percentage of display width or height that is occupied by puzzle
    final static float SCALE_IN_VIEW = 0.9f;



    /**
     * Random number generator
     */
    private Random random = new Random();
    private boolean init = true;

    private Canvas canv;

    private int score = 0;

    private int tileCount = 0;

    private Dictionary colors = new Hashtable();



    public Game(Context context){
        // Create paint for filling in the area the puzzle will be solved in
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.GRAY);
        numTile = new Paint(Paint.ANTI_ALIAS_FLAG);
        numTile.setColor(Color.DKGRAY);
//        numTile.setTextSize(75f);
        numTile.setTextAlign(Paint.Align.CENTER);
        tilePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tilePaint.setColor(Color.rgb(230,210,195));
        backGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backGroundPaint.setColor(Color.rgb(170,170,170));
        scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setColor(Color.GRAY);
//        scorePaint.setTextSize(55f);
        colors.put(2, Color.rgb(230,210,195));
        colors.put(4, Color.rgb(220,183,156));
        colors.put(8, Color.rgb(242,177,121));
        colors.put(16, Color.rgb(245,149,99));
        colors.put(32, Color.rgb(246,124,95));
        colors.put(64, Color.rgb(246,94,59));
        colors.put(128, Color.rgb(237,207,114));
        colors.put(256, Color.rgb(237,204,95));
        colors.put(512, Color.rgb(237,200,79));
        colors.put(1024, Color.rgb(237,197,62));
        colors.put(2048, Color.rgb(237,195,45));
    }

    public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        //Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        canv = canvas;

        gameSize = (int)(minDim * SCALE_IN_VIEW);
        tileSize = gameSize/4.86f;
        tileMargin = (gameSize - tileSize*4)/5;
        numTile.setTextSize(tileSize*.375f);
        scorePaint.setTextSize(tileSize*.375f);

        //Compute the margins to center the game
        marginX = (wid - gameSize) / 2;
        marginY = (hit - gameSize) / 2;
        marginY = marginY + (marginY/4);
        tileM_X = marginX + tileMargin;
        tileM_Y = marginY + tileMargin;

        for (int i=0; i<4; i++){
            colPairs.add(new Pair<>(tileM_X+(tileMargin+tileSize)*i, tileM_X + tileMargin*i + tileSize*(i+1)));
            rowPairs.add(new Pair<>(tileM_Y+(tileMargin+tileSize)*i, tileM_Y + tileMargin*i + tileSize*(i+1)));
        }

        //Draw the outline of the game
        canvas.drawRoundRect(marginX, marginY, marginX + gameSize, marginY + gameSize, 20f, 20f, fillPaint);

//        //Draw top left 2048 tile
//        numTile.setColor(Color.WHITE);
//        tilePaint.setColor((Integer) colors.get(2048));
//        canvas.drawRoundRect(marginX, marginX, getColRight(0) + marginX, marginY - marginX, 20f, 20f, tilePaint);
//        canvas.drawText(String.valueOf(2048), marginX + ((getColRight(0) + marginX - marginX)/2), marginY/1.75f , numTile);


        //draw score tile and score
        //canvas.drawRoundRect(marginX + gameSize/2, marginX, marginX + gameSize, marginY - marginX, 20f, 20f, fillPaint);
//        canvas.drawRoundRect(getColLeft(3) - marginX, marginX, marginX + gameSize, marginY - marginX, 20f, 20f, fillPaint);
//        canvas.drawText("SCORE", marginX + ((getColRight(0) + marginX - marginX)/2), marginY/1.75f , numTile);
//        canvas.drawText(String.valueOf(score), getColLeft(3) - marginX + ((getColRight(0) + marginX - marginX)/2), marginY/1.75f, numTile);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("SCORE:", marginX , marginY - marginX , scorePaint);
        scorePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(score), marginX + gameSize, marginY - marginX, scorePaint);
        numTile.setColor(Color.DKGRAY);



        for (int i = 0; i<4;i++){
            canvas.drawRoundRect(getColLeft(i), getRowTop(0), getColRight(i), getRowBottom(0), 20f, 20f, backGroundPaint);
            canvas.drawRoundRect(getColLeft(i), getRowTop(1), getColRight(i), getRowBottom(1), 20f, 20f, backGroundPaint);
            canvas.drawRoundRect(getColLeft(i), getRowTop(2), getColRight(i), getRowBottom(2), 20f, 20f, backGroundPaint);
            canvas.drawRoundRect(getColLeft(i), getRowTop(3), getColRight(i), getRowBottom(3), 20f, 20f, backGroundPaint);
        }
        if(init){
            for (int i=0; i<2; i++){
                add2Board();
            }
        }
        init = false;
        for (int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                if (board[i][j] != 0){
                    tileCount += 1;
                    if (board[i][j] > 2048){
                        tilePaint.setColor((Integer) colors.get(2048));
                    }else {
                        tilePaint.setColor((Integer) colors.get(board[i][j]));
                    }
                    if (board[i][j] > 4){
                        numTile.setColor(Color.WHITE);
                    }
                    canvas.drawRoundRect(getColLeft(j), getRowTop(i), getColRight(j), getRowBottom(i), 20f, 20f, tilePaint);
                    canvas.drawText(String.valueOf(board[i][j]), getColLeft(j) + tileSize/2f, getRowTop(i) + tileSize/1.6f, numTile);
                    numTile.setColor(Color.DKGRAY);
                }
            }
        }

    }


    public boolean shiftDown(){
        boolean result = false;
        int t = tileCount;
        for (int col =0; col<4; col++){
            //insert tiles highest row first
            int row_insert = 3;
            for(int row=3; row>-1;row--) {
                //skips lowest row
                if (row > 2){
                    ;
                }
                //if there is a non zero tile present
                else if (board[row][col] != 0){
                    //moves the row insert to the next open row highest to the top
                    while (board[row_insert][col] != 0){
                        //adds tiles together if they are the same
                        if (board[row][col] == board[row_insert][col]){
                            board[row_insert][col] = board[row][col] + board[row_insert][col];
                            score = score + board[row_insert][col];
                            board[row][col] = 0;
                            result = true;
                        }
                        row_insert -= 1;
                        //breaks if the col is full and cannot shift up
                        if (row_insert > 3);{
                            break;
                        }
                    }
                    //checks to make sure it doesn't accidentally move a piece down
                    if (row_insert > row){
                        board[row_insert][col] = board[row][col];
                        board[row][col] = 0;
                        result = true;
                    }

                }
            }
        }
        return result;
    }

    public boolean shiftUp(){
        boolean result = false;
        for (int col =0; col<4; col++){
            //insert tiles highest row first
            int row_insert = 0;
            for(int row=0; row<4;row++) {
                //skips highest row
                if (row < 1){
                    ;
                }
                //if there is a non zero tile present
                else if (board[row][col] != 0){
                    //moves the row insert to the next open row highest to the top
                    while (board[row_insert][col] != 0){
                        //adds tiles together if they are the same
                        if (board[row][col] == board[row_insert][col]){
                            board[row_insert][col] = board[row][col] + board[row_insert][col];
                            score = score + board[row_insert][col];
                            board[row][col] = 0;
                            result = true;
                        }
                        row_insert += 1;
                        //breaks if the col is full and cannot shift up
                        if (row_insert < 0);{
                            break;
                        }
                    }
                    //checks to make sure it doesn't accidentally move a piece down
                    if (row_insert < row){
                        board[row_insert][col] = board[row][col];
                        board[row][col] = 0;
                        result = true;
                    }

                }
            }
        }
        return result;
    }

    public boolean shiftRight(){
        boolean result = false;
        for (int row =0; row<4; row++){
            //insert tiles furthest right col first
            int col_insert = 3;
            for(int col=3; col>-1;col--) {
                //skips furthest right col
                if (col > 2){
                    ;
                }
                //if there is a non zero tile present
                else if (board[row][col] != 0){
                    //moves the col insert to the next open col furthest to right
                    while (board[row][col_insert] != 0){
                        //adds tiles together if they are the same
                        if (board[row][col] == board[row][col_insert]){
                            board[row][col_insert] = board[row][col] + board[row][col_insert];
                            score = score + board[row][col_insert];
                            board[row][col] = 0;
                            result = true;
                        }
                        col_insert -= 1;
                        //breaks if the row is full and cannot shift right
                        if (col_insert < 0);{
                            break;
                        }
                    }
                    //checks to make sure it doesn't accidentally move a piece to the left
                    if (col_insert > col){
                        board[row][col_insert] = board[row][col];
                        board[row][col] = 0;
                        result = true;
                    }

                }
            }
        }
        return result;
    }

    public boolean shiftLeft(){
        boolean result = false;
        for (int row =0; row<4; row++){
            //insert tiles furthest left col first
            int col_insert = 0;
            for(int col=0; col<4;col++) {
                //skips furthest left col
                if (col < 1){
                    ;
                }
                //if there is a non zero tile present
                else if (board[row][col] != 0){
                    //moves the col insert to the next open col furthest to left
                    while (board[row][col_insert] != 0){
                        //adds tiles together if they are the same
                        if (board[row][col] == board[row][col_insert]){
                            board[row][col_insert] = board[row][col] + board[row][col_insert];
                            score = score + board[row][col_insert];
                            board[row][col] = 0;
                            result = true;
                        }
                        col_insert += 1;
                        //breaks if the row is full and cannot shift left
                        if (col_insert > 3);{
                            break;
                        }
                    }
                    //checks to make sure it doesn't accidentally move a piece to the right
                    if (col_insert < col){
                        board[row][col_insert] = board[row][col];
                        board[row][col] = 0;
                        result = true;
                    }

                }
            }
        }
        return result;
    }

    public void add2Board(){
        int ranCol = random.nextInt(4);
        int ranRow = random.nextInt(4);
        while(board[ranRow][ranCol] != 0){
            ranCol = random.nextInt(4);
            ranRow = random.nextInt(4);
        }
        board[ranRow][ranCol] = 2;
    }

    public void resetBoard(){
        board = new int[][] {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
        score = 0;
        init = true;
    }

    public float getColLeft(int index){
        return (float) colPairs.get(index).first;
    }
    public float getColRight(int index){
        return (float) colPairs.get(index).second;
    }
    public float getRowTop(int index){
        return (float) rowPairs.get(index).first;
    }
    public float getRowBottom(int index){
        return (float) rowPairs.get(index).second;
    }


    public Canvas getCanv() {
        return canv;
    }

    public int getTileCount() {
        return tileCount;
    }

    public void setTileCount(int tileCount) {
        this.tileCount = tileCount;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}


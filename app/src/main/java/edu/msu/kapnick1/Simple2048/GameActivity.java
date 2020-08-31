package edu.msu.kapnick1.Simple2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.os.Bundle;
import android.widget.Toast;

import static edu.msu.kapnick1.Simple2048.Game.SWIPE_THRESHOLD;
import static edu.msu.kapnick1.Simple2048.Game.SWIPE_VELOCITY_THRESHOLD;

public class GameActivity extends AppCompatActivity implements  GestureDetector.OnGestureListener{

    private GestureDetectorCompat gestureDetector;
    private GameView gameView;
    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Instantiate the gesture detector for us and we are the listener
        gestureDetector = new GestureDetectorCompat(this, this);
        gameView = (GameView)findViewById(R.id.gameView);
        game = gameView.getGame();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
//        return game.onTouchEvent(gameView, event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        // Which is greater?
        if (Math.abs(diffX) > Math.abs(diffY)){
            //right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                if (diffX > 0){
                    onSwipeRight();
                }else{
                    onSwipeLeft();
                }
                result = true;
            }
        } else {
            //up or down
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                if (diffY > 0){
                    onSwipeBottom();
                }else{
                    onSwipeTop();
                }
                result = true;
            }
        }

        return result;
    }

    private void onSwipeTop() {
        boolean result = game.shiftUp();
        //checks if shift occurred, if so add another 2 tile
        if (result){
            game.add2Board();
        }
        gameView.invalidate();
        int tileCount = game.getTileCount();
        if (tileCount == 16){
            if (checkEndGame()){
                endGameDisplay();
            }
        }
        game.setTileCount(0);

    }

    private void onSwipeBottom() {
        boolean result = game.shiftDown();
        //checks if shift occurred, if so add another 2 tile
        if (result){
            game.add2Board();
        }
        gameView.invalidate();
        int tileCount = game.getTileCount();
        if (tileCount == 16){
            if (checkEndGame()){
                endGameDisplay();
            }
        }
        game.setTileCount(0);

    }

    private void onSwipeLeft() {
//        Toast.makeText(this, "Swipe Left", Toast.LENGTH_LONG).show();
        boolean result = game.shiftLeft();
        //checks if shift occurred, if so add another 2 tile
        if (result){
            game.add2Board();
        }
        gameView.invalidate();
        int tileCount = game.getTileCount();
        if (tileCount == 16){
            if (checkEndGame()){
                endGameDisplay();
            }
        }
        game.setTileCount(0);

    }

    private void onSwipeRight() {
        boolean result = game.shiftRight();
        //checks if shift occurred, if so add another 2 tile
        if (result){
            game.add2Board();
        }
        gameView.invalidate();
        int tileCount = game.getTileCount();
        if (tileCount == 16){
            if (checkEndGame()){
                endGameDisplay();
            }
        }
        game.setTileCount(0);


    }

    public boolean checkEndGame(){
        int[][] prevBoard = game.getBoard();
        if (game.shiftRight()){
            game.setBoard(prevBoard);
            return false;
        }
        else if (game.shiftLeft()){
            game.setBoard(prevBoard);
            return false;
        }
        else if (game.shiftUp()){
            game.setBoard(prevBoard);
            return false;
        }
        else if (game.shiftDown()){
            game.setBoard(prevBoard);
            return false;
        }else{
            return true;
        }

    }

    public void endGameDisplay(){
        Toast.makeText(this, "You have lost the game", Toast.LENGTH_SHORT).show();
    }


    public void newGame(View view) {
        game.resetBoard();
        gameView.invalidate();
    }

}




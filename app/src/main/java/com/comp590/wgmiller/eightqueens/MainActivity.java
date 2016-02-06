package com.comp590.wgmiller.eightqueens;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button spots[][];
    ArrayList<int[][]> solutions = new ArrayList<int[][]>();
    static int dropCount = 0;
    static int col = 0;
    static GridLayout g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spots = new Button[8][8];
        g = (GridLayout)findViewById(R.id.gridView);
        g.setUseDefaultMargins(true);
        int d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 40, getResources().getDisplayMetrics());
        for (int j= 0; j < 8; j++) {
            for (int i = 0; i < 8; i++){
                spots[i][j] = new Button(MainActivity.this);
                spots[i][j].setText(" ");
                if ((i + j) % 2 == 0) {
                    spots[i][j].setBackgroundColor(Color.LTGRAY);
                }
                else {
                    spots[i][j].setBackgroundColor(Color.GRAY);
                }

                spots[i][j].setLayoutParams(new GridLayoutManager.LayoutParams(d, d));
                //spots[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                g.addView(spots[i][j]);
                final int x = i;
                final int y = j;
                spots[i][j].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (spots[x][y].getText().toString() == " "){
                            tryDrop(x,y);
                        }
                        else{
                            //showPop("current: " + (x));
                            if(x == (col-1)){
                                spots[x][y].setText(" ");
                                col--;
                            }
                            spots[x][y].setText(" ");
                        }
                    }
                }
                );
            }
        }
    }
    public void getAll(View v){
        int t = col;
        solveAll(g);
        showPop(solutions.size() + " solutions!");
        col = t;
        final NumberPicker np = new NumberPicker(MainActivity.this);
        np.setMinValue(0);
        np.setMaxValue(solutions.size());
        int d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 40, getResources().getDisplayMetrics());
        np.setLayoutParams(new GridLayoutManager.LayoutParams(d, d));
        g.addView(np);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
                                         //@Override
                                         public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                             setBoard(newVal - 1);
                                         }
                                     }
        );

        solutions = new ArrayList<int[][]>();
    }
    public boolean solveAll(View v){
        //solutions = new ArrayList<int[][]>();
        boolean b = false;
        /*int s = 0;
        while(b){
            b = solve(g);
            solutions.add(saveBoard());
            s++;
        }
        showPop(s + "solutions!");*/
        if(col == 8){
            solutions.add(saveBoard());
            //showPop("8 Queens Solved!");
            b = true;
            //return true;
        }
        else{
            for(int r = 0; r < 8; r++){
                if(!hasConflict(col, r)){
                    tryDrop(col, r);
                    //dropCount++;
                    boolean e = solveAll(g);
                    //if(e) b = true;//return true;
                    col--;
                    spots[col][r].setText(" ");
                    //break;
                }
            }
            //if(dropCount==0) showPop("No Solution");
            //return false;
        }
        return b;//false on solve
    }
    public int[][] saveBoard(){
        int board[][] = new int[8][8];
        for (int j= 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if(spots[i][j].getText().toString() == " "){
                    board[i][j] = 0;
                }
                else{
                    board[i][j] = 1;
                    //spots[i][j].setText(" ");
                }
            }
        }
        return board;
    }
    public void setBoard(int p){
        int board[][] = solutions.get(p);
        for (int j= 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if(board[i][j] == 0){
                    spots[i][j].setText("♛");
                }
                else{
                    spots[i][j].setText(" ");
                }
            }
        }
    }
    public boolean solve(View v){
        int t = 0;
        if(col == 8){
            showPop("8 Queens Solved!");
            return true;
        }
        else{
            for(int r = 0; r < 8; r++){
                t = r;
                if(!hasConflict(col, r)){
                    tryDrop(col, r);
                    dropCount++;
                    boolean e = solve(g);
                    if(e) return true;
                    col--;
                    spots[col][t].setText(" ");
                    //break;
                }
                /*if(t == 7){
                    col--;
                    spots[col][t].setText(" ");
                    Log.d("myTag", "col" + col);
                }*/

            }

        }
        if(col == 0)
            showPop("No Solution" + col);
        return false;
    }
    public void tryDrop(int a, int b){
        failure:
        if(col == a){
            /*for(int i = 0; i < 8; i++){
                if(hasConflict(a,b)) {
                    showPop("Move ");
                    break failure;
                }

            }*/
            if(hasConflict(a,b)) {
                showPop("Conflicting Queen");
                break failure;
            }
            col++;
            spots[a][b].setText("♛");

        }
        else{
            showPop("Try row " + (col + 1));
        }
    }
    public boolean hasConflict(int a, int b){
        boolean t = false;
        for (int i = 0; i < 8; i++) {
            if (spots[i][b].getText() == "♛"){
                //spots[i][b].setBackgroundColor(Color.RED);
                return true;
            }
            if (spots[a][i].getText() == "♛"){
                //spots[a][i].setBackgroundColor(Color.RED);
                return true;
            }
            if ((a+i < 8 && b+i < 8) && spots[a+i][b+i].getText() == "♛"){
                //spots[a+i][b+i  ].setBackgroundColor(Color.RED);
                return true;
            }
            if ((a-i >= 0 && b-i >= 0) && spots[a-i][b-i].getText() == "♛"){
                //spots[a-i][b-i].setBackgroundColor(Color.RED);
                return true;
            }
            if ((a+i < 8 && b-i >= 0) && spots[a+i][b-i].getText() == "♛"){
                //spots[a+i][b-i].setBackgroundColor(Color.RED);
                return true;
            }
            if ((a-i >= 0 && b+i < 8) && spots[a-i][b+i].getText() == "♛"){
                //spots[a-i][b+i].setBackgroundColor(Color.RED);
                return true;
            }
        }
        return t;
    }
    public void showPop(String s){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, s, duration);
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

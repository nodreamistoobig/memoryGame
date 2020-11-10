package com.example.memorygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class GameView extends View {
    boolean stop_count = false;
    int tile1, tile2, n = 4, opened = 0;
    float x,y,w,h,tileW, tileH, margin = 100;
    Boolean calculated = false;
    ArrayList<Integer> current = new ArrayList<>(n*n);
    ArrayList<Integer> hided = new ArrayList<>(n*n);
    String[] colors = {"#7851a9","#000080", "#87cefa", "#009b76", "#ecebbd", "#ffc72e", "#d53e07", "#fca4fc", "#d0d0d0", "#ffffff"};

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < n*n; i++) {
            hided.add(i/2);
            current.add(n*n/2);
        }
        Collections.shuffle(hided);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void calcParams (Canvas c){
        h = c.getHeight();
        w = c.getWidth();
        tileH = (h-(n+1)*margin)/n;
        tileW = (w-(n+1)*margin)/n;

        calculated = true;
    }

    public void drawTiles(Canvas canvas){
        for (int i = 1; i<=n; i++){
            for (int j = 1; j<=n; j++){
                Paint p = new Paint();
                int color_num = 4*(i-1) + j-1;
                p.setColor(Color.parseColor(colors[current.get(color_num)]));
                float x0 = i*margin + tileW*(i-1);
                float y0 = j*margin + tileH*(j-1);
                float x1 = i*margin + tileW*(i);
                float y1 = j*margin + tileH*(j);
                canvas.drawRect(x0, y0, x1, y1, p);
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!calculated)
            calcParams(canvas);
        drawTiles(canvas);
    }

    public void defineTile(float x, float y){
        int row=0, column=0;
        while(x-margin>tileW){
            x=x-margin-tileW;
            row++;
        }
        while(y-margin>tileH){
            y=y-margin-tileH;
            column++;
        }

        if (x-margin>0 && y-margin>0){
            opened++;
            if (opened == 1){
                tile1 = 4*row + column;
                tile2 = -1;
            }
            else if (opened == 2){
                tile2 = 4*row + column;}

            if (tile1 == tile2){
                opened--;
                tile2 = -1;
            }
            else
                negateColor(row,column);
        }
    }

    public void negateColor(int row, int column){
        int tile_num = 4*row + column;
        if (current.get(tile_num) == n*n/2)
            current.set(tile_num, hided.get(tile_num));
    }

    private void deleteSameTiles(){
        if (tile1 != tile2 && tile2>=0 && hided.get(tile1) == hided.get(tile2)){
            current.set(tile1, n*n/2+1);
            current.set(tile2, n*n/2+1);
            stop_count = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (opened<2){
            x = event.getX();
            y = event.getY();
            defineTile(x,y);
            invalidate();

            if (opened == 2)
                stop_count=true;

            Task t = new Task();
            t.execute();

            deleteSameTiles();
        }
        return super.onTouchEvent(event);
    }


    class Task extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                if (stop_count)
                    break;
            }
            if (stop_count){
                stop_count = false;
            }
            else {
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            for (int i = 0; i < n*n; i++) {
                if (current.get(i) < n*n/2)
                    current.set(i,n*n/2);
            }
            opened=0;
            invalidate();
        }
    }
}

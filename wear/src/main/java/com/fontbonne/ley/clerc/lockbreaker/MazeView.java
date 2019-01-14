package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeView extends View {

    private Cell[][] cells;
    private Cell player, exit;
    private static final int COLS = 7;
    private static final int ROWS = 7;

    private float cellSize, hMargin, vMargin;
    private static final float WALL_THICKNESS = 4;
    private Paint wallPaint, playerPaint, exitPaint;
    private Random random;
    private boolean directed;
    private boolean visible = true;
    private boolean visibleMovements = true;


    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);
        playerPaint.setStyle(Paint.Style.FILL);

        exitPaint = new Paint();
        exitPaint.setColor(Color.BLUE);

        random = new Random();

        createMaze();
    }

    public void refresh(){
        invalidate();
    }

    public String createCellStream(){
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if(cells[x][y].topWall){
                    sb.append("1 ");
                }
                else{
                    sb.append("0 ");
                }
                if(cells[x][y].rightWall){
                    sb.append("1 ");
                }
                else{
                    sb.append("0 ");
                }
                if(cells[x][y].bottomWall){
                    sb.append("1 ");
                }
                else{
                    sb.append("0 ");
                }
                if(cells[x][y].leftWall){
                    sb.append("1 ");
                }
                else{
                    sb.append("0 ");
                }
            }
        }
        sb.append(Integer.toString(exit.col));
        sb.append(" ");
        sb.append(Integer.toString(exit.row));

        return sb.toString();
    }

    public void decodeCellStream(String stream){
        String[] streamArray = stream.split(" ");
        Cell[][] dummy_cells = new Cell[COLS][ROWS];

        int element_number = ROWS*COLS*4;
        int i;
        for(i = 0; i < element_number; i++){
            int col = (int) Math.floor((double) i / (double) 28);
            int row = (int) Math.floor((double)(i - 28*col) / (double) 4);
            int index = i % 4;

            switch(index){
                case 0:
                    dummy_cells[col][row] = new Cell(col, row, Direction.NONE);
                    if(streamArray[i].equals("1")){
                        dummy_cells[col][row].topWall = true;
                    }
                    else{
                        dummy_cells[col][row].topWall = false;
                    }
                    break;
                case 1:
                    if(streamArray[i].equals("1")){
                        dummy_cells[col][row].rightWall = true;
                    }
                    else{
                        dummy_cells[col][row].rightWall = false;
                    }
                    break;
                case 2:
                    if(streamArray[i].equals("1")){
                        dummy_cells[col][row].bottomWall = true;
                    }
                    else{
                        dummy_cells[col][row].bottomWall = false;
                    }
                    break;
                case 3:
                    if(streamArray[i].equals("1")){
                        dummy_cells[col][row].leftWall = true;
                    }
                    else{
                        dummy_cells[col][row].leftWall = false;
                    }
                    break;
            }
        }
        exit.col = Integer.parseInt(streamArray[i]);
        exit.row = Integer.parseInt(streamArray[i+1]);

        cells = dummy_cells;
        invalidate();
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
        invalidate();
    }

    public void setVisible(boolean visible){
        this.visible = visible;
        invalidate();
    }

    public void setVisibleMovements(boolean visibleMovements) {
        this.visibleMovements = visibleMovements;
    }

    private void createMaze() {
        Stack<Cell> stack = new Stack();
        Cell current, next;

        cells = new Cell[COLS][ROWS];

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = new Cell(x, y, Direction.NONE);
            }
        }

        player = new Cell(3, 3, Direction.TOP);
        current = cells[3][3];

        current.visited = true;
        do {
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else {
                if (exit == null) { //first time we go in a dead-end -> will be our goal
                    exit = current;
                }
                current = stack.pop();

            }
        } while (!stack.empty());
    }

    private void removeWall(Cell current, Cell next) {
        if (current.col == next.col && current.row == next.row + 1) {
            current.topWall = false;
            next.bottomWall = false;
        }
        if (current.col == next.col && current.row == next.row - 1) {
            current.bottomWall = false;
            next.topWall = false;
        }
        if (current.col == next.col + 1 && current.row == next.row) {
            current.leftWall = false;
            next.rightWall = false;
        }
        if (current.col == next.col - 1 && current.row == next.row) {
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    private Cell getNeighbour(Cell current) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        //left neighbour
        if (current.col > 0) {
            if (!cells[current.col - 1][current.row].visited) {
                neighbours.add(cells[current.col - 1][current.row]);
            }
        }
        //right neighbour
        if (current.col < COLS - 1) {
            if (!cells[current.col + 1][current.row].visited) {
                neighbours.add(cells[current.col + 1][current.row]);
            }
        }
        //bottom neighbour
        if (current.row < ROWS - 1) {
            if (!cells[current.col][current.row + 1].visited) {
                neighbours.add(cells[current.col][current.row + 1]);
            }
        }
        //top neighbour
        if (current.row > 0) {
            if (!cells[current.col][current.row - 1].visited) {
                neighbours.add(cells[current.col][current.row - 1]);
            }
        }

        //choose a neighbour randomly
        if (neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        } else {
            return null;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();

        if (width / height < COLS / ROWS) {
            cellSize = width / (COLS + 1);
        } else {
            cellSize = height / (ROWS + 1);
        }

        hMargin = (width - COLS * cellSize) / 2;
        vMargin = (height - ROWS * cellSize) / 2;

        canvas.translate(hMargin, vMargin);


        canvas.drawRect(exit.col * cellSize, exit.row * cellSize,
                (exit.col + 1) * cellSize, (exit.row + 1) * cellSize, exitPaint);

        if(visibleMovements) {
            drawPlayer(canvas);
        }
        else{
            canvas.drawRect(3 * cellSize, 3 * cellSize,
                    4 * cellSize, 4 * cellSize, playerPaint);
        }

        if(visible) {
            for (int x = 0; x < COLS; x++) {
                for (int y = 0; y < ROWS; y++) {
                    if (cells[x][y].topWall) {
                        canvas.drawLine(x * cellSize, y * cellSize,
                                (x + 1) * cellSize, y * cellSize, wallPaint);
                    }

                    if (cells[x][y].leftWall) {
                        canvas.drawLine(x * cellSize, y * cellSize,
                                x * cellSize, (y + 1) * cellSize, wallPaint);
                    }

                    if (cells[x][y].bottomWall) {
                        canvas.drawLine(x * cellSize, (y + 1) * cellSize,
                                (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    }

                    if (cells[x][y].rightWall) {
                        canvas.drawLine((x + 1) * cellSize, y * cellSize,
                                (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    }
                }
            }
        }
    }

    private void drawPlayer(Canvas canvas) {
        if (directed) {
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            switch (player.direction) {
                case Direction.TOP:
                    path.moveTo(player.col * cellSize, (player.row + 1) * cellSize);
                    path.lineTo((player.col + 1) * cellSize, (player.row + 1) * cellSize);
                    path.lineTo((player.col) * cellSize + cellSize / 2, player.row * cellSize);
                    path.lineTo(player.col * cellSize, (player.row + 1) * cellSize);
                    break;
                case Direction.BOTTOM:
                    path.moveTo(player.col * cellSize, player.row * cellSize);
                    path.lineTo((player.col + 1) * cellSize, player.row * cellSize);
                    path.lineTo(player.col * cellSize + cellSize / 2, (player.row + 1) * cellSize);
                    path.lineTo(player.col * cellSize, player.row * cellSize);
                    break;
                case Direction.LEFT:
                    path.moveTo((player.col + 1) * cellSize, player.row * cellSize);
                    path.lineTo((player.col + 1) * cellSize, (player.row + 1) * cellSize);
                    path.lineTo(player.col * cellSize, player.row * cellSize + cellSize / 2);
                    path.lineTo((player.col + 1) * cellSize, player.row * cellSize);
                    break;
                case Direction.RIGHT:
                    path.moveTo(player.col * cellSize, player.row * cellSize);
                    path.lineTo(player.col * cellSize, (player.row + 1) * cellSize);
                    path.lineTo((player.col + 1) * cellSize, player.row * cellSize + cellSize / 2);
                    path.lineTo(player.col * cellSize, player.row * cellSize);
                    break;
            }
            path.close();
            canvas.drawPath(path, playerPaint);
        } else {
            canvas.drawRect(player.col * cellSize, player.row * cellSize,
                    (player.col + 1) * cellSize, (player.row + 1) * cellSize, playerPaint);
        }
    }


    public void movePlayer(int direction) {
        int row_offset = 0;
        int col_offset = 0;

        if (directed) {
            switch (player.direction) {
                case Direction.TOP:
                    if (player.row > 0) {
                        row_offset = -1;
                    }
                    break;
                case Direction.BOTTOM:
                    if (player.row < ROWS - 1) {
                        row_offset = 1;
                    }
                    break;
                case Direction.LEFT:
                    if (player.col > 0) {
                        col_offset = -1;
                    }
                    break;
                case Direction.RIGHT:
                    if (player.col < COLS - 1) {
                        col_offset = 1;
                    }
                    break;
            }
        } else {
            switch (direction) {
                case Direction.TOP:
                    if (player.row > 0) {
                        row_offset = -1;
                    }
                    break;
                case Direction.BOTTOM:
                    if (player.row < ROWS - 1) {
                        row_offset = 1;
                    }
                    break;
                case Direction.LEFT:
                    if (player.col > 0) {
                        col_offset = -1;
                    }
                    break;
                case Direction.RIGHT:
                    if (player.col < COLS - 1) {
                        col_offset = 1;
                    }
                    break;
            }
        }

        if((row_offset == 1 && cells[player.col][player.row].bottomWall) ||
                (row_offset == -1 && cells[player.col][player.row].topWall)){
            row_offset = 0;
        }
        if((col_offset == 1 && cells[player.col][player.row].rightWall) ||
                (col_offset == -1 && cells[player.col][player.row].leftWall)){
            col_offset = 0;
        }

        player.row += row_offset;
        player.col += col_offset;
        invalidate();
    }

    public void rotatePlayer(boolean direction) {
        if (direction == Rotation.CLOCKWISE) {
            player.direction = (player.direction + 1) % 4;
        } else {
            player.direction = (player.direction + 3) % 4;
        }
        invalidate();
    }

    public boolean checkWin(){
        return (player.row == exit.row && player.col == exit.col);
    }


    private class Cell {
        boolean
                topWall = true,
                leftWall = true,
                bottomWall = true,
                rightWall = true,
                visited = false;

        int col, row;
        int direction;

        public Cell(int col, int row, int direction) {
            this.col = col;
            this.row = row;
            this.direction = direction;
        }
    }

    public class Direction {
        public static final int TOP = 0;
        public static final int RIGHT = 1;
        public static final int BOTTOM = 2;
        public static final int LEFT = 3;
        public static final int NONE = 4;
    }

    public class Rotation {
        public static final boolean CLOCKWISE = true;
        public static final boolean COUNTERCLOCKWISE = false;
    }

}

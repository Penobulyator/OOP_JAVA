package model;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Model{

    static private int _width;
    static private int _height;
    static private Random _rand = new Random();
    private LinkedList<Point> _snakeCords = new LinkedList<Point>(); //first is tail, last is head
    private Cell[][] _matrix;
    private Point _foodCords;
    private Point _headCords;

    private void placeFood(){
        _foodCords.x = _rand.nextInt(_width);
        _foodCords.y = _rand.nextInt(_height);
        while(_matrix[_foodCords.x][_foodCords.y] == Cell.SNAKE || _matrix[_foodCords.x][_foodCords.y] == Cell.SNAKE_HEAD){
            _foodCords.x = _rand.nextInt(_width);
            _foodCords.y = _rand.nextInt(_height);
        }
        _matrix[_foodCords.x][_foodCords.y] = Cell.FOOD;
    }
    public Model(int width, int height){
        _width = width;
        _height = height;
        _matrix = new Cell[width][height];
        for (Cell[] arr: _matrix) {
            for (Cell cell : arr)
                cell = Cell.EMPTY;
        }
    }
    public void start(){
        _matrix[0][0] = Cell.SNAKE;
        _matrix[0][1] = Cell.SNAKE_HEAD;
        _snakeCords.add(new Point(0,0));
        _snakeCords.add(new Point(0,1));
        placeFood();
    }
    public void moveRight(){
        //check for warning ?????
        Point cur_head = _snakeCords.getLast();
        _snakeCords.addLast(new Point(cur_head.x + 1, cur_head.y));
        if (_snakeCords.getLast().equals(_foodCords)){
            placeFood();
        }
        else{
            _snakeCords.removeFirst();
        }
    }
}

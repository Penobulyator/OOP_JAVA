package mvc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class View implements ActionListener {
    private Cell[][] _matrix;
    private LinkedList<Point> _snakeCords;
    public View (int width, int height){
        _matrix = new Cell[width][height];
        for(Cell[] arr : _matrix){
            for (Cell cell : arr)
                cell = Cell.EMPTY;
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

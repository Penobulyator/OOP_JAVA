package helperClasses;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class Parser {
    Queue<String[]> _commands = new ArrayDeque<String[]>();
    BufferedReader _reader;
    public Parser(){
        _reader = new BufferedReader(new InputStreamReader(System.in));
    }
    public Parser(String FileName){
        try {
            _reader = new BufferedReader(new FileReader(FileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void pars(){
        String line = "";
        while(true){
            try {
                line = _reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null)
                return;
            else{
                if (line.charAt(0) != '#'){ //if line isn't a comment
                    _commands.add(line.split(" "));
                }
            }
        }
    }
    public String[] getCommand(){
        return _commands.poll();
    }
}

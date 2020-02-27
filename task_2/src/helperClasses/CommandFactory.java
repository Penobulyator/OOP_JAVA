package helperClasses;
import helperClasses.commands.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CommandFactory {
    Map<String, Class<?>> _buf;
    public CommandFactory(){
        _buf = new TreeMap<>();
        try( BufferedReader reader = new BufferedReader(new FileReader("src/helperClasses/factory.txt"))){
            String line;
            while((line = reader.readLine())!= null)
            {
                String[] words = line.split(" ");
                if (words.length != 2){
                    System.out.println("Bad factory.txt format");
                    return;
                }
                else{
                    try {
                        _buf.put(words[0], Class.forName("helperClasses.commands."+words[1]));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Command create(String name) throws Exception{
        if (!_buf.containsKey(name)){
            throw new Exception("No such command as " + name);
        }
        else{
            try {
                return (Command)_buf.get(name).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

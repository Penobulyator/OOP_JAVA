package snake.controller;

import java.io.Serializable;
import java.util.*;

public class RecordTable implements Serializable {
    private final int CAPACITY = 3;

    Map<String, Integer> buffer = new TreeMap<>();

    public void addRecord(String name, int score){
        if (buffer.containsKey(name)) {
            //if player already has some record
            if (buffer.get(name) < score)
                buffer.replace(name, score);
        }
        else{
            buffer.put(name, score);
            if (buffer.size() > CAPACITY)
                deleteLowest();
        }
    }
    private void deleteLowest(){
        String lowestElem = null;
        int min = Integer.MAX_VALUE;

        //find lowest element
        for (Map.Entry<String, Integer> record : buffer.entrySet()) {
            if (record.getValue() < min){
                min = record.getValue();
                lowestElem = record.getKey();
            }
        }
        //delete lowest element
        if (lowestElem != null)
            buffer.remove(lowestElem);
    }

    public ArrayList<Map.Entry<String, Integer>> getSortedRecords() {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(buffer.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        return list;
    }
}

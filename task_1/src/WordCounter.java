import com.sun.istack.internal.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class WordCounter {
    private Map<String, Integer> _words = new TreeMap<>();
    private int _wordCount = 0;
    private Writer _writer;
    private Reader _reader;

    public WordCounter(String fileIn, String fileOut) {
        try {
            this._reader = new FileReader(fileIn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            this._writer = new FileWriter(fileOut);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public WordCounter() {
        this._reader = new InputStreamReader(System.in);
        this._writer = new OutputStreamWriter(System.out);
    }

    private void processLine(String line) {
        StringBuilder cur_word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isLetterOrDigit(line.codePointAt(i))) {
                if (cur_word.length() != 0) {
                    _words.putIfAbsent(cur_word.toString(), 0);
                    _words.replace(cur_word.toString(), _words.get(cur_word.toString()) + 1);
                    cur_word = new StringBuilder();
                    _wordCount++;
                }
            } else {
                cur_word.append(line.charAt(i));
            }
        }
    }

    public void count() {
        BufferedReader br = new BufferedReader(_reader);
        String s = "";
        while (true) {
            try {
                s = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (s == null) {
                try {
                    _reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            } else {
                processLine(s);
            }
        }
    }

    public void print() {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(_words.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        BufferedWriter bw = new BufferedWriter(_writer);
        for (Map.Entry<String, Integer> a : list) {
            try
            {
                bw.write(a.getKey()
                        + ", " + a.getValue()
                        + ", " + String.format("%.2f", (a.getValue().doubleValue() / _wordCount)*100).replace(',','.') + "%"
                        + "\r\n");
                bw.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            _writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordCounter wc;
        if (args.length == 2)
            wc = new WordCounter(args[0], args[1]);
        else
            wc = new WordCounter();
        wc.count();
        wc.print();
    }
}
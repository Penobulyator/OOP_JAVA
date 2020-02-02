import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.*;
public class WordCounter
{
    private String file_in;
    private String file_out;
    private Map<String, Integer> words;
    private int word_count = 0;
    private Writer writer;
    private Reader reader;

    public WordCounter(String file_in, String file_out)
    {
        try
        {
            this.reader = new InputStreamReader(new FileInputStream(file_in));
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        try
        {
            this.writer = new OutputStreamWriter(new FileOutputStream(file_out));
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    public WordCounter()
    {
        this.reader = new InputStreamReader(System.in);
        this.writer = new OutputStreamWriter(System.out);
    }
    private void ProcessLine(String line)
    {
        int last_processed_index = 0;
        for (int i=0;i<line.length();i++)
        {
            if (!Character.isLetterOrDigit(line.codePointAt(i)))
            {
                char[] cur_word = null;
                line.getChars(last_processed_index, i, cur_word, 0);
                Integer cur_count = words.get(cur_word.toString());
                words.put(cur_word.toString(), cur_count + 1);
                last_processed_index = i + 1;
                word_count++;
            }
        }
    }
    public void count()
    {
        while(true)
        {
            String s = reader.toString();
            if (s == null)
                break;
            else
            {
                ProcessLine(s);
            }
        }
    }
    public void print()
    {
        //надо дописать
    }
    public static void main(String[] args)
    {
        WordCounter wc = null;
        if (args.length > 1)
            wc = new WordCounter(args[1], args[2]);
        else
            wc = new WordCounter();
        wc.count();
    }
}

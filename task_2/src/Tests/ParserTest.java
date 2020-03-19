package Tests;

import helperClasses.Parser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
class ParserTest {
    private void fillFile(String path, String[][] words){
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            for (String[] arrays : words) {
                for (String strings: arrays) {
                    writer.write(strings + " ");
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void ParsingTest() {
        final String[][] real = {
                new String[] {"DEFINE", "a", "4"},
                new String[] {"PUSH" ,"a"},
                new String[] {"SQRT"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"parserTest.txt";
        fillFile(path, real);
        Parser parser = new Parser(path);
        parser.pars();
        for (String[] strings : real) {
            assertArrayEquals(strings, parser.getCommand());
        }
    }
}
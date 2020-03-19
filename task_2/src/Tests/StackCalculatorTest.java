package Tests;

import main.StackCalculator;
import org.junit.jupiter.api.Test;

import java.io.*;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.*;

class StackCalculatorTest {
    private ByteArrayOutputStream setOut(){
        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        return baos;
    }
    private double CheckResult(ByteArrayOutputStream baos) {

        // Put things back
        System.out.flush();
        System.setOut(System.out);
        // Show what happened
        return Double.parseDouble(baos.toString().replaceAll("\r\n", ""));
    }
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
    void sqrtTest() {
        final double num = 25;
        final String[][] words = {
                new String[] {"DEFINE", "a", Double.toString(num)},
                new String[] {"PUSH" ,"a"},
                new String[] {"SQRT"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"sqrtTest.txt";
        fillFile(path, words);
        StackCalculator stackCalculator = new StackCalculator(path);
        ByteArrayOutputStream baos = setOut();
        stackCalculator.calculate();
        assertEquals(CheckResult(baos), sqrt(num));
    }

    @Test
    void plusTest() {
        final double num1 = 12.3;
        final double num2 = 45.6;
        final String[][] words = {
                new String[] {"DEFINE", "a", Double.toString(num1)},
                new String[] {"DEFINE", "b", Double.toString(num2)},
                new String[] {"PUSH" ,"a"},
                new String[] {"PUSH" ,"b"},
                new String[] {"+"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"sqrtTest.txt";
        fillFile(path, words);
        StackCalculator stackCalculator = new StackCalculator(path);
        ByteArrayOutputStream baos = setOut();
        stackCalculator.calculate();
        assertEquals(CheckResult(baos), num1 + num2);
    }
    @Test
    void minusTest() {
        final double num1 = 12.3;
        final double num2 = 45.6;
        final String[][] words = {
                new String[] {"DEFINE", "a", Double.toString(num1)},
                new String[] {"DEFINE", "b", Double.toString(num2)},
                new String[] {"PUSH" ,"a"},
                new String[] {"PUSH" ,"b"},
                new String[] {"-"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"sqrtTest.txt";
        fillFile(path, words);
        StackCalculator stackCalculator = new StackCalculator(path);
        ByteArrayOutputStream baos = setOut();
        stackCalculator.calculate();
        assertEquals(CheckResult(baos), num2 - num1);
    }

    @Test
    void multTest() {
        final double num1 = 12.3;
        final double num2 = 45.6;
        final String[][] words = {
                new String[] {"DEFINE", "a", Double.toString(num1)},
                new String[] {"DEFINE", "b", Double.toString(num2)},
                new String[] {"PUSH" ,"a"},
                new String[] {"PUSH" ,"b"},
                new String[] {"*"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"sqrtTest.txt";
        fillFile(path, words);
        StackCalculator stackCalculator = new StackCalculator(path);
        ByteArrayOutputStream baos = setOut();
        stackCalculator.calculate();
        assertEquals(CheckResult(baos), num1 * num2);
    }

    @Test
    void divTest() {
        final double num1 = 12.3;
        final double num2 = 45.6;
        final String[][] words = {
                new String[] {"DEFINE", "a", Double.toString(num1)},
                new String[] {"DEFINE", "b", Double.toString(num2)},
                new String[] {"PUSH" ,"a"},
                new String[] {"PUSH" ,"b"},
                new String[] {"/"},
                new String[] {"PRINT"},
        };
        final String path = "src"+ File.separator + "Tests" + File.separator +"sqrtTest.txt";
        fillFile(path, words);
        StackCalculator stackCalculator = new StackCalculator(path);
        ByteArrayOutputStream baos = setOut();
        stackCalculator.calculate();
        assertEquals(CheckResult(baos), num2 / num1);
    }
}
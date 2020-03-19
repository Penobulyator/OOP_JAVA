package main;

import exceptions.CommandException;
import helperClasses.CommandFactory;
import helperClasses.Context;
import helperClasses.Parser;
import helperClasses.commands.Command;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StackCalculator {
    private Logger _logger = Logger.getLogger(StackCalculator.class.getName());
    private Parser _parser;
    private CommandFactory _factory = new CommandFactory();
    private Context _context = new Context();
    public StackCalculator(String fileIn){

        _parser = new Parser(fileIn);
        _parser.pars();
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("StackCalculator.log");
            _logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public void calculate(){
        String[] line;
        while ((line = _parser.getCommand()) != null)
        {
            Command command = null;
            try {
                command = _factory.create(line[0]);
            } catch (Exception e) {
                _logger.log(Level.WARNING, "Exception: ", e);
            }
            try {
                String[] arguments = Arrays.copyOfRange(line, 1, line.length);
                command.execute(_context, arguments);
                _logger.log(Level.INFO, "Command executed: " + command.getClass().getName() + " with arguments " + Arrays.toString(arguments));
            } catch (CommandException e) {
                _logger.log(Level.WARNING, "Exception: ", e);
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        StackCalculator calculator = new StackCalculator(args[0]);
        calculator.calculate();
    }
}

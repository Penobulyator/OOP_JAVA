package main;

import exceptions.CommandException;
import helperClasses.CommandFactory;
import helperClasses.Context;
import helperClasses.Parser;
import helperClasses.commands.Command;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StackCalculator {
    private Logger _logger = Logger.getLogger(StackCalculator.class.getName());
    private Parser _parser;
    private CommandFactory _factory = new CommandFactory();
    private Context _context = new Context();
    StackCalculator(String fileIn){
        _parser = new Parser(fileIn);
        _parser.pars();
    }
    void calculate(){
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

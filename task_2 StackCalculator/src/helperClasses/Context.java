package helperClasses;
import exceptions.CommandException;
import exceptions.ContextException;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Context {
    private Map<String, Double> _definitions = new TreeMap<String, Double>();
    private Stack<String> _stack = new Stack<String>();
    public void push(String s){
        _stack.push(s);
    }
    public void push(double s){
        _stack.push(Double.toString(s));
    };
    public String pop() throws ContextException {
        if (_stack.empty())
            throw new ContextException("Stack is empty");
        return _stack.pop();
    }
    public void define(String name, double value){
        _definitions.put(name, value);
    }
    private double getDefinition(String var) throws ContextException
    {
        if (!_definitions.containsKey(var))
            throw new ContextException("Variable " + var + " in unidentified");
        return _definitions.getOrDefault(var, null);
    }
    public double popNumber() throws ContextException{
        String expression;
        expression = pop();
        double number;
        try {
            //if expression is a number
            number = Double.parseDouble(expression);
        } catch (NumberFormatException e) {
            //if expression isn't a number
            number = getDefinition(expression);
        }
        return number;
    }
}

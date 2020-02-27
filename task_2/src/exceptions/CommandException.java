package exceptions;

public class CommandException extends Exception{
    public CommandException(String s){
        super(s);
    }
    public CommandException(ContextException e){
        super(e.getMessage());
    }
}

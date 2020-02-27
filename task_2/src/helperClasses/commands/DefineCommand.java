package helperClasses.commands;
import exceptions.CommandException;
import helperClasses.Context;

public class DefineCommand implements Command{
    private static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        if (arguments.length < 2)
            throw new CommandException("Too few arguments");
        else if (arguments.length > 2)
            throw new CommandException("Too many arguments");
        else
        {
            context.define(arguments[0], Double.parseDouble(arguments[1]));
        }
    }
}

package helperClasses.commands;

import exceptions.CommandException;
import helperClasses.Context;

public class PushCommand implements Command {
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        if (arguments.length == 0)
            throw new CommandException("Too few arguments");
        else if (arguments.length > 1)
            throw new CommandException("Too many arguments");
        else
            context.push(arguments[0]);
    }
}

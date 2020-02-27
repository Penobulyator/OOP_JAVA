package helperClasses.commands;

import exceptions.CommandException;
import exceptions.ContextException;
import helperClasses.Context;


public class SqrtCommand implements Command {
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        double number = 0;
        try {
            number = context.popNumber();
        } catch (ContextException e) {
            throw new CommandException(e);
        }
        if (number < 0)
            throw new CommandException("Can't take a sqrt of negative number");
        else
            context.push(Math.sqrt(number));
    }
}

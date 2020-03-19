package helperClasses.commands;

import exceptions.CommandException;
import exceptions.ContextException;
import helperClasses.Context;

public class MultCommand implements Command{
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        try {
            double num1 = context.popNumber();
            double num2 = context.popNumber();
            context.push(num1 * num2);
        } catch (ContextException e) {
            throw new CommandException(e);
        }
    }
}

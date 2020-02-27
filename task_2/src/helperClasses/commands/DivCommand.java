package helperClasses.commands;

import exceptions.CommandException;
import exceptions.ContextException;
import helperClasses.Context;

public class DivCommand implements Command{
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        try {
            double num1 = context.popNumber();
            double num2 = context.popNumber();
            if (num2 == 0)
                throw new CommandException("Can't divide by zero");
            context.push(num1 / num2);
        } catch (ContextException e) {
            throw new CommandException(e);
        }
    }
}

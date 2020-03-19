package helperClasses.commands;

import exceptions.CommandException;
import exceptions.ContextException;
import helperClasses.Context;

public class PrintCommand implements Command{
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {
        try {
            double num = context.popNumber();
            System.out.println(num);
            context.push(num);
        } catch (ContextException e) {
            throw new CommandException(e);
        }
    }
}

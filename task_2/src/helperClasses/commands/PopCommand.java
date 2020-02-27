package helperClasses.commands;

import exceptions.ContextException;
import helperClasses.Context;
import exceptions.CommandException;

public class PopCommand implements Command {
    @Override
    public void execute(Context context, String[] arguments) throws CommandException {

        try {
            context.pop();
        } catch (ContextException e) {
            throw new CommandException(e);
        }
    }
}

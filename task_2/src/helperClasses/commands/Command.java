package helperClasses.commands;
import exceptions.CommandException;
import helperClasses.Context;
public interface Command {
    void execute(Context context, String[] arguments) throws CommandException;
}

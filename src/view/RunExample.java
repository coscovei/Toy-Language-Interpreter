package view;

import controller.Controller;
import exception.MyException;
import exception.RepoException;
import model.PrgState;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RunExample extends Command {
    private final Controller ctr;
    private final String logFilePath;

    public RunExample(String key, String desc, Controller ctr, String logFilePath) {
        super(key, desc);
        this.ctr = ctr;
        this.logFilePath = logFilePath;
    }

    @Override
    public void execute() throws MyException, FileNotFoundException, RepoException {
        try {
            PrgState prg = ctr.getPrgState();
            ctr.executeAllSteps();
            System.out.println("\n*** Program finished successfully ***");
            System.out.println("Final Output: " + prg.getOut().toString());

        } catch (RepoException e) {
            System.err.println("Repository Error: " + e.getMessage());
        } catch (MyException e) {
            System.err.println("Execution Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
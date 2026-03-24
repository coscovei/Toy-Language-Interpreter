package controller;

import exception.MyException;
import exception.RepoException;
import model.PrgState;

import java.io.FileNotFoundException;

public interface IController {
    void executeOneStep() throws FileNotFoundException, RepoException, MyException;
    void executeAllSteps() throws FileNotFoundException, RepoException, MyException;

    PrgState getPrgState();

    void addPrgState(PrgState prgState);

    void clearPrgState();
}

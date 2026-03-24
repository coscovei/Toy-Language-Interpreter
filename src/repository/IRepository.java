package repository;

import exception.RepoException;
import model.PrgState;

import java.util.List;

public interface IRepository {
    // A5: repository stores a list of program states
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);

    // convenience (kept for older view code)
    PrgState getPrgState();

    void addPrgState(PrgState prgState);

    // A5: log takes the program state to log
    void logPrgStateExec(PrgState prgState) throws RepoException;

    void clearPrgState();
}

package repository;

import exception.RepoException;
import model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgStates;
    private final String logFilePath;

    public Repository(PrgState initialProgram, String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(initialProgram);
        this.logFilePath = logFilePath;
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgStates = prgList;
    }

    @Override
    public PrgState getPrgState() {
        if (prgStates == null || prgStates.isEmpty()) {
            return null;
        }
        return prgStates.get(0);
    }

    @Override
    public void addPrgState(PrgState prgState) {
        prgStates.add(prgState);
    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws RepoException {
        if (prgState == null) return;

        try (PrintWriter ps = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            ps.write(prgState.toString());
        } catch (IOException e) {
            throw new RepoException("Log file write error: " + e.getMessage());
        }
    }

    @Override
    public void clearPrgState() {
        prgStates.clear();
    }
}

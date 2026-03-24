package controller;

import exception.MyException;
import exception.RepoException;
import model.PrgState;
import model.adt.MyIHeap;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;
import repository.Repository;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller implements IController {
    private final IRepository repository;
    private ExecutorService executor;

    public Controller(PrgState initialPrgState, String logFilePath) {
        this.repository = new Repository(initialPrgState, logFilePath);
        this.executor = null;
    }

    // ---- GUI helper: expose list of program states ----
    public List<PrgState> getPrgList() {
        return repository.getPrgList();
    }

    @Override
    public void executeOneStep() throws FileNotFoundException, RepoException, MyException {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(2);
        }

        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        if (prgList.isEmpty()) return;

        runGarbageCollector(prgList);
        oneStepForAllPrg(prgList);

        repository.setPrgList(removeCompletedPrg(repository.getPrgList()));
    }

    @Override
    public void executeAllSteps() throws FileNotFoundException, RepoException, MyException {
        executor = Executors.newFixedThreadPool(2);

        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while (!prgList.isEmpty()) {
            runGarbageCollector(prgList);
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();
        repository.setPrgList(prgList);
        executor = null;
    }

    @Override
    public PrgState getPrgState() {
        return repository.getPrgState();
    }

    @Override
    public void addPrgState(PrgState prgState) {
        repository.addPrgState(prgState);
    }

    @Override
    public void clearPrgState() {
        repository.clearPrgState();
    }

    // -------- A5 core methods --------

    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(Objects::nonNull)
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    private void oneStepForAllPrg(List<PrgState> prgList) {
        // log BEFORE
        prgList.forEach(prg -> {
            try { repository.logPrgStateExec(prg); }
            catch (Exception ignored) {}
        });

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) p::oneStep)
                .collect(Collectors.toList());

        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try { return future.get(); }
                        catch (Exception e) { return null; }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            prgList.addAll(newPrgList);

        } catch (InterruptedException ignored) {}

        // log AFTER
        prgList.forEach(prg -> {
            try { repository.logPrgStateExec(prg); }
            catch (Exception ignored) {}
        });

        repository.setPrgList(prgList);
    }

    // -------- Garbage Collector (safe, transitive; roots from ALL prg states) --------

    private void runGarbageCollector(List<PrgState> prgList) {
        if (prgList.isEmpty()) return;

        MyIHeap<IValue> heap = prgList.get(0).getHeap(); // shared heap

        List<Integer> rootAddresses = prgList.stream()
                .flatMap(p -> getAddresses(p.getSymTable().getContent().values()).stream())
                .collect(Collectors.toList());

        Map<Integer, IValue> newHeap = safeGarbageCollector(rootAddresses, heap.getContent());
        heap.setContent(newHeap);
    }

    private List<Integer> getAddresses(Collection<IValue> values) {
        return values.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        Set<Integer> addresses = new HashSet<>(symTableAddr);
        boolean changed = true;

        while (changed) {
            List<IValue> reachableHeapValues = heap.entrySet().stream()
                    .filter(e -> addresses.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            List<Integer> newAddrs = getAddresses(reachableHeapValues);
            changed = addresses.addAll(newAddrs);
        }

        return heap.entrySet().stream()
                .filter(e -> addresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

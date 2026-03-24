package view;

import controller.Controller;
import exception.MyException;
import exception.RepoException;
import model.PrgState;
import model.adt.*;
import model.statements.IStmt;
import model.types.IType;
import model.values.IValue;
import model.values.StringValue;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class GuiMain extends Application {

    private Stage stage;

    // current running controller (selected program)
    private Controller controller;

    // UI components for main window
    private TextField nrPrgStatesField;

    private TableView<HeapEntry> heapTable;
    private ListView<String> outList;
    private ListView<String> fileTableList;

    private ListView<Integer> prgStateIdsList;

    private TableView<SymEntry> symTable;
    private ListView<String> exeStackList;

    private Button runOneStepButton;

    // ---- NEW: keep last state so UI doesn't disappear when program completes ----
    private List<PrgState> lastSnapshot = new ArrayList<>();
    private List<PrgState> displayedPrgStates = new ArrayList<>();

    // ----- JavaFX launch -----
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Interpreter GUI (A7)");

        showProgramSelectScene();
        stage.show();
    }

    // ----- Scene 1: select program -----
    private void showProgramSelectScene() {
        List<IStmt> programs = Arrays.asList(
                Examples.example1(),
                Examples.example2(),
                Examples.example3(),
                Examples.example4_FileTest(),
                Examples.example5_heapSimple(),
                Examples.example6_nestedRefs(),
                Examples.example7_while(),
                Examples.example8_forkSharedHeap()
        );

        ObservableList<String> programStrings = FXCollections.observableArrayList(
                programs.stream().map(Object::toString).collect(Collectors.toList())
        );

        ListView<String> listView = new ListView<>(programStrings);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();

        Button startBtn = new Button("Start selected program");
        startBtn.setOnAction(e -> {
            int idx = listView.getSelectionModel().getSelectedIndex();
            if (idx < 0) return;

            IStmt selected = programs.get(idx);

            try {
                PrgState prg = createProgramStateWithTypecheck(selected);
                controller = new Controller(prg, "log_gui.txt");

                // reset snapshots when starting a new program
                lastSnapshot = new ArrayList<>();
                displayedPrgStates = new ArrayList<>();

                showMainScene();
                refreshAll();
            } catch (MyException ex) {
                showAlert("Typecheck failed", ex.getMessage());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                new Label("Select a program to execute:"),
                listView,
                startBtn
        );
        root.setPadding(new Insets(12));

        stage.setScene(new Scene(root, 900, 600));
    }

    // ----- Scene 2: main execution window -----
    private void showMainScene() {
        nrPrgStatesField = new TextField();
        nrPrgStatesField.setEditable(false);

        // heap table
        heapTable = new TableView<>();
        TableColumn<HeapEntry, String> heapAddrCol = new TableColumn<>("Address");
        heapAddrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        heapAddrCol.setPrefWidth(120);

        TableColumn<HeapEntry, String> heapValCol = new TableColumn<>("Value");
        heapValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        heapValCol.setPrefWidth(320);

        heapTable.getColumns().addAll(heapAddrCol, heapValCol);

        // out list
        outList = new ListView<>();

        // file table list
        fileTableList = new ListView<>();

        // prg ids list
        prgStateIdsList = new ListView<>();
        prgStateIdsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        prgStateIdsList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            refreshSelectedPrgDetails();
        });

        // sym table
        symTable = new TableView<>();
        TableColumn<SymEntry, String> symNameCol = new TableColumn<>("Variable");
        symNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        symNameCol.setPrefWidth(160);

        TableColumn<SymEntry, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        symValCol.setPrefWidth(280);

        symTable.getColumns().addAll(symNameCol, symValCol);

        // exe stack list
        exeStackList = new ListView<>();

        // run button
        runOneStepButton = new Button("Run one step");
        runOneStepButton.setOnAction(e -> {
            try {
                controller.executeOneStep(); // runs one step for all PrgStates (A5)
                refreshAll();
            } catch (FileNotFoundException | RepoException | MyException ex) {
                showAlert("Execution error", ex.getMessage());
            } catch (Exception ex) {
                showAlert("Unexpected error", ex.getMessage());
            }
        });

        Button backBtn = new Button("Back to program selection");
        backBtn.setOnAction(e -> showProgramSelectScene());

        HBox topBar = new HBox(10,
                new Label("Number of active PrgStates:"),
                nrPrgStatesField,
                runOneStepButton,
                backBtn
        );
        topBar.setPadding(new Insets(10));

        // Left panel: Prg IDs + ExeStack
        VBox left = new VBox(8,
                new Label("PrgState IDs"),
                prgStateIdsList,
                new Label("ExeStack (top first)"),
                exeStackList
        );
        left.setPadding(new Insets(10));
        left.setPrefWidth(280);
        VBox.setVgrow(prgStateIdsList, Priority.ALWAYS);
        VBox.setVgrow(exeStackList, Priority.ALWAYS);

        // Center: SymTable
        VBox center = new VBox(8,
                new Label("SymTable (selected PrgState)"),
                symTable
        );
        center.setPadding(new Insets(10));
        VBox.setVgrow(symTable, Priority.ALWAYS);

        // Right: Heap + Out + FileTable
        VBox right = new VBox(8,
                new Label("Heap"),
                heapTable,
                new Label("Out"),
                outList,
                new Label("FileTable"),
                fileTableList
        );
        right.setPadding(new Insets(10));
        right.setPrefWidth(460);
        VBox.setVgrow(heapTable, Priority.ALWAYS);
        VBox.setVgrow(outList, Priority.ALWAYS);
        VBox.setVgrow(fileTableList, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(left);
        root.setCenter(center);
        root.setRight(right);

        stage.setScene(new Scene(root, 1200, 700));
    }

    // ----- Refresh logic -----

    private void refreshAll() {
        if (controller == null) return;

        List<PrgState> prgList = controller.getPrgList();
        if (prgList == null) prgList = new ArrayList<>();

        // Save snapshot when we still have running prg states
        if (!prgList.isEmpty()) {
            lastSnapshot = new ArrayList<>(prgList);
            displayedPrgStates = prgList;
        } else {
            // No active programs: keep displaying the last snapshot (final state)
            displayedPrgStates = lastSnapshot;
        }

        // active count = real prgList size (0 when completed)
        nrPrgStatesField.setText(String.valueOf(prgList.size()));

        // Disable stepping when no active programs
        runOneStepButton.setDisable(prgList.isEmpty());

        // update heap/out/file table using first displayed prg (shared structures in A5)
        if (displayedPrgStates != null && !displayedPrgStates.isEmpty()) {
            PrgState first = displayedPrgStates.get(0);

            refreshHeap(first);
            refreshOut(first);
            refreshFileTable(first);
        }

        // update prg IDs list from displayedPrgStates
        if (displayedPrgStates != null) {
            ObservableList<Integer> ids = FXCollections.observableArrayList(
                    displayedPrgStates.stream().map(PrgState::getId).collect(Collectors.toList())
            );

            Integer previouslySelected = prgStateIdsList.getSelectionModel().getSelectedItem();
            prgStateIdsList.setItems(ids);

            // restore selection if possible
            if (previouslySelected != null && ids.contains(previouslySelected)) {
                prgStateIdsList.getSelectionModel().select(previouslySelected);
            } else if (!ids.isEmpty()) {
                prgStateIdsList.getSelectionModel().selectFirst();
            }
        }

        refreshSelectedPrgDetails();
    }

    private void refreshHeap(PrgState prg) {
        Map<Integer, IValue> heapContent = prg.getHeap().getContent();
        List<HeapEntry> data = heapContent.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new HeapEntry(String.valueOf(e.getKey()), e.getValue().toString()))
                .collect(Collectors.toList());
        heapTable.setItems(FXCollections.observableArrayList(data));
    }

    private void refreshOut(PrgState prg) {
        List<IValue> out = prg.getOut().getContent();
        List<String> data = out.stream().map(Object::toString).collect(Collectors.toList());
        outList.setItems(FXCollections.observableArrayList(data));
    }

    private void refreshFileTable(PrgState prg) {
        MyIDictionary<StringValue, BufferedReader> ft = prg.getFileTable();
        List<String> data = ft.getContent().keySet().stream()
                .map(StringValue::toString)
                .collect(Collectors.toList());
        fileTableList.setItems(FXCollections.observableArrayList(data));
    }

    private void refreshSelectedPrgDetails() {
        Integer selectedId = prgStateIdsList.getSelectionModel().getSelectedItem();
        if (selectedId == null || displayedPrgStates == null) {
            symTable.setItems(FXCollections.observableArrayList());
            exeStackList.setItems(FXCollections.observableArrayList());
            return;
        }

        PrgState prg = displayedPrgStates.stream()
                .filter(p -> p.getId() == selectedId)
                .findFirst()
                .orElse(null);

        if (prg == null) {
            symTable.setItems(FXCollections.observableArrayList());
            exeStackList.setItems(FXCollections.observableArrayList());
            return;
        }

        // SymTable
        List<SymEntry> symData = prg.getSymTable().getContent().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new SymEntry(e.getKey(), e.getValue().toString()))
                .collect(Collectors.toList());
        symTable.setItems(FXCollections.observableArrayList(symData));

        // ExeStack (top first) - should be empty at end, which is fine
        List<String> stackData = prg.getExeStack().getReversed().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        exeStackList.setItems(FXCollections.observableArrayList(stackData));
    }

    // ----- Build PrgState with A6 typecheck -----
    private PrgState createProgramStateWithTypecheck(IStmt program) throws MyException {
        MyIDictionary<String, IType> typeEnv = new MyDictionary<>();
        program.typecheck(typeEnv);

        return new PrgState(
                new MyDictionary<>(), // FileTable
                new MyStack<>(),      // ExeStack
                new MyDictionary<>(), // SymTable
                new MyList<>(),       // Out
                new MyHeap<>(),       // Heap
                program
        );
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    // ----- Small table models -----
    public static class HeapEntry {
        private final SimpleStringProperty address;
        private final SimpleStringProperty value;

        public HeapEntry(String address, String value) {
            this.address = new SimpleStringProperty(address);
            this.value = new SimpleStringProperty(value);
        }

        public String getAddress() { return address.get(); }
        public String getValue() { return value.get(); }
    }

    public static class SymEntry {
        private final SimpleStringProperty name;
        private final SimpleStringProperty value;

        public SymEntry(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName() { return name.get(); }
        public String getValue() { return value.get(); }
    }
}

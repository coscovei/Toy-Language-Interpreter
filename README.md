# Toy Language Interpreter

A Java-based educational interpreter for a small typed language with heap operations, file I/O, and fork-based concurrency.

This project implements a toy language interpreter in Java using a layered architecture. Programs are represented as an AST (abstract syntax tree) and executed through a runtime model built from custom data structures.

The repository currently includes both:

- a **CLI interpreter**
- a **JavaFX GUI interpreter**

## Features

The interpreter supports:

- variable declarations and assignments
- arithmetic and relational expressions
- conditional statements
- `while` loops
- file operations
- heap allocation and heap reads/writes
- reference types
- `fork`-based concurrent execution with a shared heap
- runtime logging of program states

## Repository Layout

The repository contains both a legacy-style `src/` tree and a Maven-style `src/main/java/` tree.

### Main interpreter source tree

```text
src/main/java/
├── controller/
├── exception/
├── model/
├── repository/
├── view/
├── com/example/demo/
└── module-info.java
```

### Other important files

```text
pom.xml
mvnw
mvnw.cmd
log.txt
log_gui.txt
test.in
```

## Main Packages

- `controller/`  
  Coordinates program execution, one-step execution, concurrency, and garbage collection.

- `exception/`  
  Contains custom exception classes used across the interpreter.

- `model/`  
  Contains the language model, including statements, expressions, types, values, ADTs, heap, and program state.

- `repository/`  
  Stores and logs program states.

- `view/`  
  Contains both interpreter entry points:
  - `Interpreter.java` for the CLI version
  - `GuiMain.java` for the JavaFX GUI version

## Included Example Programs

The interpreter registers 8 predefined example programs:

1. Simple variable declaration / assignment / print
2. Arithmetic expressions
3. Conditional statement
4. File example using `test.in`
5. Simple heap usage
6. Nested references
7. While loop
8. Fork with shared heap

## How to Run

There are 2 ways to run the interpreter:

### 1. CLI Interpreter

Run:

```text
src/main/java/view/Interpreter.java
```

This starts the text-based menu where you can choose one of the predefined example programs.

### 2. JavaFX GUI Interpreter

Run:

```text
src/main/java/view/GuiMain.java
```

This starts the graphical debugger-style interface for selecting and executing the predefined programs step by step.

## Input and Log Files

### Input file

```text
test.in
```

Used by the file-handling example program.

### Log files

```text
log.txt
log_gui.txt
```

- `log.txt` is used by the CLI interpreter examples.
- `log_gui.txt` is used by the JavaFX GUI interpreter.

## Educational Goals

This project is a good example of how to build a small interpreter with:

- an AST-based execution model
- static type checking
- custom runtime data structures
- heap semantics
- file table management
- shared-heap concurrency with `fork`
- step-by-step execution
- JavaFX-based visualization of runtime state

## Notes

- The toy programs are predefined in Java and constructed directly from statement and expression objects.
- The GUI version allows selecting a predefined program and executing it one step at a time.
- The GUI displays runtime structures such as:
  - heap table
  - output list
  - file table
  - active program state IDs
  - symbol table
  - execution stack
- In the fork example, output order may vary because of concurrent execution.

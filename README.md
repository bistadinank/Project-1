# Project-1
Delphi using Pascal Grammar and Java Interpreter

Delphi Language Interpreter - README
Project Overview
This project implements a basic version of the Delphi language by defining its grammar in pascal.g4, generating ANTLR code, and testing it using various test cases. The interpreter is written in Java and runs within IntelliJ IDEA. The project demonstrates class definitions, access specifiers (private, protected, public), constructors, destructors, and encapsulation through test cases.

Setup and Running Instructions
Prerequisites
IntelliJ IDEA (or any Java-supported IDE)
Java (JDK 11+)
ANTLR4 installed and configured
ANTLR4 Plugin (optional, but recommended)
Steps to Run the Project
Generate ANTLR Code

Open the project in IntelliJ IDEA.
Locate the pascal.g4 file (contains the Delphi grammar).
Generate ANTLR code from pascal.g4 by running the ANTLR generator.
Test the ANTLR Grammar

Use grammarchecker.txt to check the parse tree.
Ensure the grammar is correctly generated.
Move Generated Files

Move all generated ANTLR files into the src directory.
Set Up the Interpreter

Copy PascalInterpreter.java into the src directory.
Run the Interpreter

Open PascalInterpreter.java in IntelliJ.
Locate line 365, which contains the file path for test cases.
Replace the file path with the test case you want to run.
Test the Delphi Grammar

Run PascalInterpreter.java to test the grammar using different test cases.
Test Cases Overview
The project includes five test cases demonstrating different aspects of the Delphi language implementation:

Test Case	Description
test.pas	Checks class definition, constructor, destructor, and creation of private/public variables.
test1.pas	Ensures that objects can be created successfully.
test2.pas	Verifies access to a public variable from an object.
test3.pas	Demonstrates that private/protected variables cannot be accessed directly from an object but can be accessed within the class.
test4.pas	Implements a getter function for a private variable and shows that it can be accessed through the getter.

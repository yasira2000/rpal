import java.io.File;

import CSEMachine.CSEMachine;
import Parser.Parser;
import Scanner.Scanner;
import abstractSyntaxTree.AST;

public class App {

    public static void main(String[] args) throws Exception {
        File inFile = null;
        if (0 < args.length) {
            inFile = new File(args[0]);
            Scanner s = new Scanner(inFile.getName());
            Parser p = new Parser(s);
            AST ast = p.buildAST();
            ast.standardize();
            CSEMachine cse = new CSEMachine(ast);
            cse.evaluateProgram();

        }
        else {
            System.out.println("Please enter the file name in command line");
        }

    }
}

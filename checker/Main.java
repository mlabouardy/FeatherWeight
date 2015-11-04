package checker;

import java.util.List;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.BufferedReader;
import java.io.FileReader;

import checker.node.*;
import checker.lexer.Lexer;
import checker.lexer.LexerException;
import checker.parser.Parser;
import checker.parser.ParserException;
import checker.analysis.DepthFirstAdapter;
import checker.typecheck.ClassTable;
import checker.model.*;
import checker.passes.BuildModel;

public class Main {

    public static final String delim =
	" -------------------------------------- ";
    
    public static void main(String[] args)
	throws IOException, LexerException, ParserException {
	System.out.println(Main.delim);
	System.out.println(" -- FeatherWeight Java Lexer and Parser");
	System.out.println("    Original code from T. Allwood");
	System.out.println("    Available from : http://www.doc.ic.ac.uk/~tora/");
	if (args.length != 1) {
	    System.out.println(" !! Needs exactly one source file");
	    System.exit(1);
	}
	
	System.out.println(" -- Handling : " + args[0]);
	System.out.println(Main.delim);

	// Parse file
	Lexer l = new Lexer (new PushbackReader (
			     new BufferedReader(
			     new FileReader(args[0]))));
	Parser p = new Parser (l);
	Start start = p.parse ();
	System.out.println (start.toString());
	System.out.println(Main.delim);

	// Traverse AST and print declarations to stdout
	DepthFirstAdapter ad = new DepthFirstAdapter() {
		public void defaultIn(Node node) { }
		public void inAClassDecl(AClassDecl node) {
		    System.out.println("- Class Decl for " +
				       node.getClassname() + " : " + node);
		}
		public void inAConstructorDecl(AConstructorDecl node) {
		    System.out.println("  * Constructor Decl for " +
				       node.getIdentifier() + " : " + node);
		}
		public void inAMethodDecl(AMethodDecl node) {
		    System.out.println("  * Method Decl for " +
				       node.getMethodname() + " : " + node);
		}
	    };
	start.apply(ad);
	System.out.println(Main.delim);

	try {
	    // Build a Model from the code
	    BuildModel bm = new BuildModel();
	    start.apply(bm);
	    List<ClassDecl> classes = bm.getClasses();
	    
	    ClassTable ct = new ClassTable();
	    for(ClassDecl cd : classes) {
		System.out.println(cd);
		ct.addClassDefinition(cd);
	    }
	    System.out.println(Main.delim);
	    // TypeCheck the class table
	    ct.validateClassTable();
	    
	} catch(Throwable t) {
	    // t.printStackTrace();
	    System.out.println("Error : " + t.getMessage());
	}
    }
}

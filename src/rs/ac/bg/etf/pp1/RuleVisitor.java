package rs.ac.bg.etf.pp1;
import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class RuleVisitor extends VisitorAdaptor{
	int printCallCount = 0;
	
	Logger log = Logger.getLogger(getClass());
	
	public void visit(PrintStatement printStm) {
		printCallCount++;
		log.info("Procitao Print stm");
	}
}
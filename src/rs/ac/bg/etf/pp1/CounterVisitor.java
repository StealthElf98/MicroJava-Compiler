package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormParamDeclArr;
import rs.ac.bg.etf.pp1.ast.FormParamDeclVar;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor {

		@Override
		public void visit(FormParamDeclVar formParamDecl1) {
			count++;
		}		
		
		@Override
		public void visit(FormParamDeclArr formParamDecl1) {
			count++;
		}	
	}
	
	public static class VarCounter extends CounterVisitor {		
		@Override
		public void visit(VarDecl VarDecl) {
			count++;
		}
	}
}

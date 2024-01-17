package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FactorInt;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.NonVoidMethodType;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethodType;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	
	private int varCount;
	
	private int paramCnt;
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(PrintStatement printStatement) {
		if(printStatement.getExpr().struct == Tab.intType) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(FactorInt factorInt) {
		Obj con = Tab.insert(Obj.Con, "$", factorInt.struct);
		con.setLevel(0);
		con.setAdr(factorInt.getN1());
	
		Code.load(con);
	}
	
	public void visit(VoidMethodType voidMethodType) {
		if("main".equalsIgnoreCase(voidMethodType.getMethodName())) {
			mainPc = Code.pc;
		}
		
		voidMethodType.obj.setAdr(Code.pc);
	}
	
	
}

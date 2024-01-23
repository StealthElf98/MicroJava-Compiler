package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddopExpr;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.DesigStatementAssign;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementDec;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementInc;
import rs.ac.bg.etf.pp1.ast.Designatorr;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorInt;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MulFacList;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethodType;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	public void visit(VoidMethodType voidMethodType) {
		if ("main".equalsIgnoreCase(voidMethodType.getMethodName())) {
			mainPc = Code.pc;
		}

		voidMethodType.obj.setAdr(Code.pc);
		SyntaxNode methodNode = voidMethodType.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);

		Code.put(Code.enter);
		Code.put(0);
		Code.put(varCnt.getCount());
	}

	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(PrintStatement printStatement) {
		Code.put(Code.const_5);
		Code.put(Code.print);
	}

	public void visit(FactorInt factorInt) {
		Code.loadConst(factorInt.getN1());
	}

	public void visit(FactorChar factorChar) {
		Code.loadConst(factorChar.getC1());
	}
	
	public void visit(FactorBool factorBool) {
		int val = factorBool.getB1() ? 1 : 0;
		Code.loadConst(val);
	}
	
	public void visit(DesigStatementAssign desig) {
		Code.store(desig.getDesignator().obj);
	}
	
	
	
//	public void visit(DesignatorStatementInc desig) {
//		Code.store(desig.getDesignator().obj);
//	}
//	
//	public void visit(DesignatorStatementDec desig) {
//		Code.store(desig.getDesignator().obj);
//	}
	
	public void visit(AddopExpr expr) {
		if(expr.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}
	
	public void visit(MulFacList mulFacList) {
		if(mulFacList.getMulop() instanceof MulopMul) {
			Code.put(Code.mul);
		} else if(mulFacList.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}
	
	public void visit(Designatorr designator) {
		SyntaxNode parent = designator.getParent();
		
		if(parent.getClass() != DesigStatementAssign.class) {
			Code.load(designator.obj);
		}
	}
}

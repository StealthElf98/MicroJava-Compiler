package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddopExpr;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.DesigStatementAssign;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementDec;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementInc;
import rs.ac.bg.etf.pp1.ast.FactDesig;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorInt;
import rs.ac.bg.etf.pp1.ast.FactorNewExpr;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MinusExpr;
//import rs.ac.bg.etf.pp1.ast.MulFacList;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.NestingArray;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.PrintStatementNum;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Termm;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethodType;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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

		Code.put(Code.enter);
		Code.put(0);
		Code.put(voidMethodType.obj.getLocalSymbols().size());
	}

	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(PrintStatement printStatement) {
		Code.loadConst(1);
		Code.put(Code.print);
	}
	
	public void visit(PrintStatementNum printStatement) {
		Code.loadConst(printStatement.getN2());
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
	
	public void visit(FactDesig factDesig) {
		Code.load(factDesig.getDesignator().obj);
	}
	
	public void visit(FactorNewExpr factorNewExpr) {
		Code.put(Code.newarray);
		
		if (factorNewExpr.getType().struct == Tab.charType) 
			Code.put(0);
		else
			Code.put(1);
	}
	
	public void visit(DesigStatementAssign desig) {
		Code.store(desig.getDesignator().obj);
	}
	
	public void visit(NestingArray nArray) {
		Code.load(nArray.getDesignatorName().obj);
	}
	
	public void visit(DesignatorStatementInc desig) {
		if(isElemInArray(desig.getDesignator().obj)) {
			Code.put(Code.dup2);
		}
		
		Code.load(desig.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(desig.getDesignator().obj);
	}
	
	public void visit(DesignatorStatementDec desig) {
		if(isElemInArray(desig.getDesignator().obj)) {
			Code.put(Code.dup2);
		}
		
		Code.load(desig.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(desig.getDesignator().obj);
	}
	
	public void visit(AddopExpr expr) {
		if(expr.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}
	
	public void visit(Termm term) {
		if(term.getMulop() instanceof MulopMul) {
			Code.put(Code.mul);
		} else if(term.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}

	public void visit(MinusExpr minusExpr) {
		 Code.put(Code.neg);
	}
	
	private boolean isElemInArray(Obj obj) {
		return obj.getKind() == Obj.Elem;
	}
}

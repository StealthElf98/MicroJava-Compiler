package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddopExpr;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.BoolValue;
import rs.ac.bg.etf.pp1.ast.CharValue;
import rs.ac.bg.etf.pp1.ast.ConstDeclExtensionn;
import rs.ac.bg.etf.pp1.ast.ConstDecll;
import rs.ac.bg.etf.pp1.ast.DesigStatementAssign;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorName;
import rs.ac.bg.etf.pp1.ast.DesignatorNameNoNs;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementDec;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementInc;
import rs.ac.bg.etf.pp1.ast.ExprTerm;
import rs.ac.bg.etf.pp1.ast.FactDesig;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorInt;
import rs.ac.bg.etf.pp1.ast.FactorMatt;
import rs.ac.bg.etf.pp1.ast.FactorNewExpr;
import rs.ac.bg.etf.pp1.ast.IntValue;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MinusExpr;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.PrintStatementNum;
import rs.ac.bg.etf.pp1.ast.ReadStatement;
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
		//System.out.println("main");
		if ("main".equalsIgnoreCase(voidMethodType.getMethodName())) {
			mainPc = Code.pc;
		}

		voidMethodType.obj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(0);
		Code.put(voidMethodType.obj.getLocalSymbols().size());
	}

	public void visit(MethodDecl methodDecl) {
		//System.out.println("MethodDecl ");
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ConstDeclExtensionn constDecl) {
		//System.out.println("ConstDeclExtensionn " + getConstValue(constDecl.getRValue()));
		constDecl.obj.setAdr(getConstValue(constDecl.getRValue()));
		Code.load(constDecl.obj);
	}
	
	public void visit(FactorInt factorInt) {
		//System.out.println("factorInt ");
		Code.loadConst(factorInt.getN1());
	}

	public void visit(FactorChar factorChar) {
		Code.loadConst(factorChar.getC1());
	}
	
	public void visit(FactorBool factorBool) {
		Code.loadConst(factorBool.getB1() ? 1 : 0);
	}
	
	public void visit(FactDesig factDesig) {
		//System.out.println("factDesig " + factDesig.getDesignator().obj.getName());
		Code.load(factDesig.getDesignator().obj);
	}
	
	public void visit(FactorNewExpr factorNewExpr) {
		if(factorNewExpr.getFactorMat() instanceof FactorMatt) {
			Code.put(Code.enter);
			Code.put(2);
			Code.put(3);
			
			Code.put(Code.load_n);
			Code.put(Code.newarray);
			Code.put(1);
			
			int whileAddress = Code.pc;
			Code.put(Code.load_2);
			Code.put(Code.load_n);
			Code.putFalseJump(Code.lt, 0);
			int addressZero = Code.pc-2;
			
			//while
			Code.put(Code.dup);
			Code.put(Code.load_2);
			Code.put(Code.load_1);
			Code.put(Code.newarray);
			if(factorNewExpr.getType().struct == Tab.charType) {
				Code.put(0);
			}
			else {
				Code.put(1);
			}
			Code.put(Code.astore);
			
			Code.put(Code.load_2);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.store_2);
			
			Code.putJump(whileAddress);
			Code.fixup(addressZero);
			
			Code.put(Code.exit);
			
			return;
		}
		
		Code.put(Code.newarray);
		
		if (factorNewExpr.getType().struct == Tab.charType) 
			Code.put(0);
		else
			Code.put(1);
	}

	public void visit(DesigStatementAssign desig) {	
		Code.store(desig.getDesignator().obj);
	}
	
	public void visit(DesignatorName designatorName) {
		Code.load(designatorName.getDesignator().obj);
	}
		
	public void visit(DesignatorStatementInc desig) {
		//System.out.println("INc");
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
	
	public void visit(IntValue intValue) {
		//System.out.println("Vrednost je: " + intValue.getValue());
		Code.loadConst(intValue.getValue());
	}
	
	public void visit(CharValue charValue) {
		Code.loadConst(charValue.getValue());
	}
	
	public void visit(BoolValue boolValue) {
		Code.loadConst(boolValue.getValue() ? 1 : 0);
	}
	
	public void visit(PrintStatement printStatement) {
		//System.out.println("print");
		Code.loadConst(0);
		
		if (printStatement.getExpr().struct == Tab.charType)
			Code.put(Code.bprint);
		else 
			Code.put(Code.print);
	}
	
	public void visit(PrintStatementNum printStatement) {
		Code.loadConst(printStatement.getN2());
		
		if (printStatement.getExpr().struct == Tab.charType)
			Code.put(Code.bprint);
		else 
			Code.put(Code.print);
	}
	
	public void visit(ReadStatement readStm) {
		if (readStm.getDesignator().obj.getType() == Tab.charType)
			Code.put(Code.bread);
		else
			Code.put(Code.read);
		
		Code.store(readStm.getDesignator().obj);
	}
	
	private boolean isElemInArray(Obj obj) {
		return obj.getKind() == Obj.Elem;
	}
	
	private int getConstValue(SyntaxNode node) {
		if(node instanceof IntValue)
			return ((IntValue)node).getValue();
		else if(node instanceof CharValue)
			return ((CharValue)node).getValue();
		else if(node instanceof BoolValue)
			if(((BoolValue)node).getValue().equals(true))
				return 1;
			else
				return 0;
		return -1;
	}
}

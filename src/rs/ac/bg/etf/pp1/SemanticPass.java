package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Struct currentType = null;
	Obj currentMethod = null;
	String currentNamespace = null;
	boolean isNamespace = false;
	int methodParamCnt = 0;
	int nVars;

	Struct boolType = new Struct(5);
	Obj boolObj = new Obj(Obj.Type, "bool", boolType);

	public SemanticPass() {
		Tab.currentScope().addToLocals(boolObj);
	}

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" line ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" line ").append(line);
		log.info(msg.toString());
	}

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Type " + type.getTypeName() + " not found in symbol table!", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				currentType = typeNode.getType();
				type.struct = currentType;
			} else {
				report_error("ERROR: Name " + type.getTypeName() + " is not a type!", null);
				type.struct = Tab.noType;
			}
		}
	}

	// Program-----------------------------------------------------------------------------------------------

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		
		Obj main = Tab.find("main");
		if(main == Tab.noObj) {
			report_error("ERROR: Main method not defined!", null);
		} else if (main.getLevel() != 0) {
			report_error("ERROR: Main method must be in global scope!", null);
		} else if(main.getKind() != Obj.Meth) {
			report_error("ERROR: Main can only be of type method!", null);
		} else if(main.getType() != Tab.noType) {
			report_error("ERROR: Main must have void return type!", null);
		} else {
			report_info("Main method exists! ", null);
		}
		
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();
	}

	// Namespace-----------------------------------------------------------------------------------------------

	public void visit(Namespace namespace) {
		isNamespace = false;
	}

	public void visit(NamespaceName nsName) {
		nsName.obj = Tab.insert(Obj.Type, nsName.getSpaceName(), new Struct(Struct.None));
		currentNamespace = nsName.getSpaceName();
		isNamespace = true;
	}

	// VarDecl-----------------------------------------------------------------------------------------------
	public void visit(VarType varType) {
		currentType = varType.getType().struct;
	}

	public void visit(VarDeclListArr varDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + varDecla.getVName();
		else
			name = varDecla.getVName();

		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace : "[" + varDecla.getVName() + "]";
			report_info("Detected new variable " + temp + " on", varDecla);
		}
	}

	public void visit(VarDeclListVar varDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + varDecla.getVName();
		else
			name = varDecla.getVName();

		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, currentType);
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace : "[" + varDecla.getVName() + "]";
			report_info("Detected new variable " + temp + " on", varDecla);
		}
	}

	public void visit(VarDeclListOneVar varDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + varDecla.getVName();
		else
			name = varDecla.getVName();

		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, currentType);
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace : "[" + varDecla.getVName() + "]";
			report_info("Detected new variable " + temp + " on", varDecla);
		}
	}

	public void visit(VarDeclListOneArr varDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + varDecla.getVName();
		else
			name = varDecla.getVName();

		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace : "[" + varDecla.getVName() + "]";
			report_info("Detected new variable " + temp + " on", varDecla);
		}
	}

	// ConstDecl-----------------------------------------------------------------------------------------------------------

	public void visit(ConstType constType) {
		currentType = constType.getType().struct;
	}

	public void visit(IntValue intValue) {
		intValue.struct = Tab.intType;
	}

	public void visit(CharValue charValue) {
		charValue.struct = Tab.charType;
	}

	public void visit(BoolValue boolValue) {
		boolValue.struct = boolType;
	}

	public void visit(ConstDecll constDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + constDecla.getCName();
		else
			name = constDecla.getCName();

		if (!symbolInSameScope(name, constDecla)) {
			if (okRValue(constDecla.getRValue().struct, constDecla)) {
				constDecla.obj = Tab.insert(Obj.Con, name, currentType);
				String temp = isNamespace ? "[" + constDecla.getCName() + "]" + " in namespace:" + currentNamespace
						: "[" + constDecla.getCName() + "]";
				report_info("Detected new constant " + temp + " on", constDecla);
			}
		}
	}

	public void visit(ConstDeclExtensionn constDecla) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + constDecla.getCName();
		else
			name = constDecla.getCName();

		if (!symbolInSameScope(name, constDecla)) {
			constDecla.obj = Tab.insert(Obj.Con, name, currentType);
			String temp = isNamespace ? "[" + constDecla.getCName() + "]" + " in namespace:" + currentNamespace
					: "[" + constDecla.getCName() + "]";
			report_info("Detected new constant " + temp + " on", constDecla);
		}
	}

	// MethodDecl-----------------------------------------------------------------------------------------------

	public void visit(NonVoidMethodType methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), methodTypeName.getType().struct);
		methodTypeName.obj = currentMethod;
		Tab.openScope();
	}

	public void visit(VoidMethodType methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), Tab.noType);
		methodTypeName.obj = currentMethod;
		Tab.openScope();
	}

	public void visit(MethodDecl methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		methodDecl.obj = currentMethod;
		methodParamCnt = 0;
	}

//	public void visit(FormParamDeclVar formPar) {
//		Obj elem = Tab.insert(Obj.Var, formPar.getPName(), formPar.getType().struct);
//		methodParamCnt++;
//	}
//
//	public void visit(FormParamDeclArr formPar) {
//		Obj elem = Tab.insert(Obj.Var, formPar.getPName(), new Struct(Struct.Array, formPar.getType().struct));
//		methodParamCnt++;
//	}

	public void visit(MethodVarDecl methodVarDecl) {
		currentType = methodVarDecl.getType().struct;
	}

	public void visit(MethodVarr methodVarr) {
		if (!symbolInSameScope(methodVarr.getVName(), methodVarr)) {
			Obj elem = Tab.insert(Obj.Var, methodVarr.getVName(), currentType);
		}
	}

	public void visit(MethodVarArr methodVarr) {
		if (!symbolInSameScope(methodVarr.getVName(), methodVarr)) {
			Obj elem = Tab.insert(Obj.Var, methodVarr.getVName(), new Struct(Struct.Array, currentType));
		}
	}

	// Designator -------------------------------------------------------------------------------------
	public void visit(Designatorr designator) {
		Obj temp = designator.getDesignatorName().obj;
		Obj newTemp = Tab.find(temp.getName());
		if (newTemp != Tab.noObj) {
			String array = (designator.getDesignatorList() instanceof DesignatorListExpr) ? "array" : "";
			if (newTemp.getKind() == Obj.Con) {
				report_info("Accessing const " + array + "[" + newTemp.getName() + "]" + " on", designator);
				designator.obj = new Obj(Obj.Con, newTemp.getName(), Tab.intType);
			} else {
				report_info("Accessing variable " + array + "[" + newTemp.getName() + "]" + " on", designator);
				designator.obj = new Obj(Obj.Var, newTemp.getName(), Tab.intType);
			}
		} else {
			report_info("Var/Con " + temp.getName() + " not found!", designator);
		}
	}

	public void visit(DesignatorListExpr designator) {
		// Tab.intType = new Struct(Struct.Int) nije isto ovde ??????????????
		if (designator.getExpr().struct != Tab.intType) {
			report_error("ERROR: Value inside [] must be int!", designator);
		}
	}

	public void visit(DesignatorNameNoNs designator) {
		designator.obj = new Obj(Obj.Var, designator.getDName(), new Struct(Struct.None));
	}

	public void visit(DesignatorNameNs designator) {
		designator.obj = new Obj(Obj.Var, designator.getNsName() + "::" + designator.getDName(),
				new Struct(Struct.None));
	}

	public void visit(DesigStatementAssign desigStmAssign) {
		Obj leftOp = desigStmAssign.getDesignator().obj;
		Struct rightOp = desigStmAssign.getExpr().struct;
		
		if(leftOp.getKind() == Obj.Con) {
			report_error("ERROR: Left operand is const value, can't assigne value!", null);
		} else if(leftOp.getType() != Tab.intType &&  rightOp != Tab.intType) {
			report_error("ERROR: Bad types", desigStmAssign);
		} else {
			report_info("Assigned new value to variable [" + desigStmAssign.getDesignator().obj.getName() + "] on", desigStmAssign);
		}
	}

	public void visit(DesignatorStatementInc designator) {
		if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("ERROR: Incremented variable must be int type!", designator);
		} else if(designator.getDesignator().obj.getKind() == Obj.Con) {
			report_error("ERROR: Can't increment const type!", designator);
		} else {
			report_info("Incremented variable [" + designator.getDesignator().obj.getName() + "] on", designator);
		}
	}

	public void visit(DesignatorStatementDec designator) {
		if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("ERROR: Decremented variable must be int type!", designator);
		} else if(designator.getDesignator().obj.getKind() == Obj.Con) {
			report_error("ERROR: Can't decremente const type!", designator);
		} else {
			report_info("Decremented variable [" + designator.getDesignator().obj.getName() + "] on", designator);
		}
	}

	// Expr -------------------------------------------------------------------------------------------------------
	
	public void visit(ExprTerm expr) {
		expr.struct = expr.getTerm().struct;
	}

	public void visit(MinusExpr expr) {
		if (expr.getTerm().struct != Tab.intType) {
			report_error("ERROR: Operand must be of type int!", expr);
			expr.struct = Tab.noType;
		} else {
			expr.struct = expr.getTerm().struct;			
		}
	}

	public void visit(AddopExpr expr) {
		if (expr.getExpr().struct != Tab.intType && expr.getTerm().struct != Tab.intType) {
			report_error("ERROR: Operands must be of type int!", expr);
			
			expr.struct = Tab.noType;
		} else {
			expr.struct = Tab.intType;
		}
	}

	// Term -------------------------------------------------------------------------------------------------------
	public void visit(Termm term) {
		term.struct = term.getMulFactorList().struct;
	}

	// Factor -----------------------------------------------------------------------------------------------------
	
	public void visit(MulFacList mulFacList) {
		if (mulFacList.getMulFactorList().struct != mulFacList.getFactor().struct) {
//			System.out.println("MulFacList " + mulFacList.struct);
//			System.out.println("Factor " + mulFacList.getFactor().struct);
			report_error("ERROR: Operands are different type!", mulFacList);
			mulFacList.struct = Tab.noType;
		} else if (mulFacList.struct != Tab.intType && mulFacList.getFactor().struct != Tab.intType) {
			report_error("ERROR: Operands must be of type int!", mulFacList);
			mulFacList.struct = Tab.noType;
		} else {
			mulFacList.struct = Tab.intType;			
		}
	}
	
	public void visit(MulFacListOne mulFacListOne) {
		mulFacListOne.struct = mulFacListOne.getFactor().struct;
	}

	public void visit(FactorInt factorInt) {
		factorInt.struct = Tab.intType;
	}

	public void visit(FactorChar factorChar) {
		factorChar.struct = Tab.charType;
	}

	public void visit(FactorBool factorBool) {
		factorBool.struct = boolType;
	}

	public void visit(FactorNewExpr factorNewExpr) {
		// ??????????
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactDesig dactDesig) {
		dactDesig.struct = dactDesig.getDesignator().obj.getType();
	}
	
	// Print ------------------------------------------------------------------------------------------
	
	public void visit(PrintStatementNum printStatementNum) {
		
	}
	
	// Help Methods -----------------------------------------------------------------------------------

	private boolean okRValue(Struct value, SyntaxNode node) {
		if (value == currentType)
			return true;

		report_error("ERROR: Const is wrong type!", node);
		return true;
	}

	private boolean symbolInSameScope(String name, SyntaxNode node) {
		if (Tab.currentScope.findSymbol(name) == null)
			return false;

		report_error("ERROR: Symbol with this name already exists in this scope " + name, node);
		return true;
	}
	
}

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
		if (main == Tab.noObj) {
			report_error("ERROR: Main method not defined!", null);
		} else if (main.getLevel() != 0) {
			report_error("ERROR: Main method must be in global scope!", null);
		} else if (main.getKind() != Obj.Meth) {
			report_error("ERROR: Main can only be of type method!", null);
		} else if (main.getType() != Tab.noType) {
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
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace
					: "[" + varDecla.getVName() + "]";
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
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace
					: "[" + varDecla.getVName() + "]";
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
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace
					: "[" + varDecla.getVName() + "]";
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
			String temp = isNamespace ? "[" + varDecla.getVName() + "]" + " in namespace:" + currentNamespace
					: "[" + varDecla.getVName() + "]";
			report_info("Detected new variable " + temp + " on", varDecla);
		}
	}
	
	public void visit(VarDeclListOneMat varDecla) {
		String name = varDecla.getVName();
		
		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
			report_info("Detected new matrix variable " + "[" + varDecla.getVName() + "]" + " on", varDecla);
		}
	}
	
	public void visit(VarDeclListMat varDecla) {
		String name = varDecla.getVName();
		
		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
			report_info("Detected new matrix variable " + "[" + varDecla.getVName() + "]" + " on", varDecla);
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
				
				if(constDecla.getRValue() instanceof IntValue) {
					IntValue temp = (IntValue)constDecla.getRValue();
					constDecla.obj.setAdr(temp.getValue());
				} else if(constDecla.getRValue() instanceof CharValue) {
					CharValue temp = (CharValue)constDecla.getRValue();
					constDecla.obj.setAdr(temp.getValue());
				} else {
					BoolValue temp = (BoolValue)constDecla.getRValue();
					constDecla.obj.setAdr(temp.getValue() ? 1 : 0);
				}
				
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
			
			if(constDecla.getRValue() instanceof IntValue) {
				IntValue temp = (IntValue)constDecla.getRValue();
				constDecla.obj.setAdr(temp.getValue());
			} else if(constDecla.getRValue() instanceof CharValue) {
				CharValue temp = (CharValue)constDecla.getRValue();
				constDecla.obj.setAdr(temp.getValue());
			} else {
				BoolValue temp = (BoolValue)constDecla.getRValue();
				constDecla.obj.setAdr(temp.getValue() ? 1 : 0);
			}
			
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
			report_info("Detected new method variable [" + methodVarr.getVName() + "] in [" + currentMethod.getName() + "] on", methodVarr);
		}		
	}

	public void visit(MethodVarArr methodVarr) {
		if (!symbolInSameScope(methodVarr.getVName(), methodVarr)) {
			Obj elem = Tab.insert(Obj.Var, methodVarr.getVName(), new Struct(Struct.Array, currentType));
			report_info("Detected new method array [" + methodVarr.getVName()+ "] in [" + currentMethod.getName() + "] on", methodVarr);
		}
	}

	// Designator
	// -------------------------------------------------------------------------------------
	public void visit(Designatorr designator) {
		Obj temp = designator.getDesignatorName().obj;
		Obj newTemp = Tab.find(temp.getName());
		if (newTemp != Tab.noObj) {
			if (newTemp.getKind() == Obj.Con) {
				report_info("Accessing const [" + newTemp.getName() + "]" + " on", designator);
			} else {
				report_info("Accessing variable [" + newTemp.getName() + "]" + " on", designator);
			}
			designator.obj = designator.getDesignatorName().obj;
		} else {
			report_info("Var/Con " + temp.getName() + " not found!", designator);
		}
	}
	
	
	public void visit(DesignatorArray designator) {
		if (designator.getExpr().struct.getKind() == Struct.Array) {
			if(designator.getExpr().struct.getElemType() != Tab.intType)
				report_error("ERROR: Value of array element must be int,", designator);
		} else if(designator.getExpr().struct != Tab.intType) {
			report_error("ERROR: Value inside [] must be int,", designator);
		}
		
		designator.obj = new Obj(Obj.Elem, designator.getNestingArray().obj.getName(), designator.getExpr().struct);
	}

		
	public void visit(NestingArray nArray) {
		nArray.obj = nArray.getDesignatorName().obj;
	}
	
	public void visit(DesignatorNameNoNs designator) {
		Obj temp = Tab.find(designator.getDName());
		if(temp != Tab.noObj) {
			designator.obj = new Obj(Obj.Var, designator.getDName(), temp.getType());			
		}
	}

	public void visit(DesignatorNameNs designator) {
		designator.obj = new Obj(Obj.Var, designator.getNsName() + "::" + designator.getDName(),
				new Struct(Struct.None));
	}

	public void visit(DesigStatementAssign desigStmAssign) {
		Obj leftOp = desigStmAssign.getDesignator().obj;
		Struct rightOp = desigStmAssign.getExpr().struct;
 
		if (leftOp.getKind() == Obj.Con) {
			report_error("ERROR: Left operand is const value, can't assigne value!", null);
		// OVAJ FUNKCIJA MORA DA SE ISPRAVI
//		} else if (!canAssign(leftOp.getType(),rightOp)) {
//			report_error("ERROR: Bad types", desigStmAssign);
		} else {
			report_info("Assigned new value to variable [" + desigStmAssign.getDesignator().obj.getName() + "] on",
					desigStmAssign);
		}
	}

	public void visit(DesignatorStatementInc designator) {
		Struct temp;
		temp = designator.getDesignator().obj.getType().getElemType();
		if(temp == null) {
			temp = new Struct(designator.getDesignator().obj.getKind());
		}
		
		if (temp.getKind() != Tab.intType.getKind()) {
			report_error("ERROR: Incremented variable must be int type!", designator);
		} else if (designator.getDesignator().obj.getKind() == Obj.Con) {
			report_error("ERROR: Can't increment const type!", designator);
		} else {
			report_info("Incremented variable [" + designator.getDesignator().obj.getName() + "] on", designator);
		}
	}

	public void visit(DesignatorStatementDec designator) {
		Struct temp;
		temp = designator.getDesignator().obj.getType().getElemType();
		if(temp == null) {
			temp = designator.getDesignator().obj.getType();
		}
		
		if (temp != Tab.intType) {
			report_error("ERROR: Decremented variable must be int type!", designator);
		} else if (designator.getDesignator().obj.getKind() == Obj.Con) {
			report_error("ERROR: Can't decremente const type!", designator);
		} else {
			report_info("Decremented variable [" + designator.getDesignator().obj.getName() + "] on", designator);
		}
	}

	// Expr
	// -------------------------------------------------------------------------------------------------------

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

	// Term
	// -------------------------------------------------------------------------------------------------------
	public void visit(Termm term) {
		if(term.getTerm().struct !=  term.getFactor().struct) 
			report_error("ERROR: Operands are different type!", term);	
		
		term.struct = term.getTerm().struct;
	}

	// Factor 	
	// -------------------------------------------------------------------------------------------------------

//	public void visit(MulFacList mulFacList) {
//		if (mulFacList.getMulFactorList().struct != mulFacList.getFactor().struct) {
//			System.out.println("MulFacList " + mulFacList.struct);
//			System.out.println("Factor " + mulFacList.getFactor().struct.getKind());
//			report_error("ERROR: Operands are different type!", mulFacList);
//			mulFacList.struct = Tab.noType;
//		} 
//			else if (mulFacList.struct != Tab.intType && mulFacList.getFactor().struct != Tab.intType) {
//			report_error("ERROR: Operands must be of type int!", mulFacList);
//			mulFacList.struct = Tab.noType;
//		} else {
//			mulFacList.struct = mulFacList.getMulFactorList().struct;
//		}
//	}

//	public void visit(MulFacListOne mulFacListOne) {
//		mulFacListOne.struct = mulFacListOne.getFactor().struct;
//	}
	
	public void visit(TermOne term) {
		term.struct = term.getFactor().struct;
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
		if(factorNewExpr.getExpr().struct != Tab.intType) {
			report_error("ERROR: Array length must be int value!", factorNewExpr);
		} else {
			factorNewExpr.struct = new Struct(Struct.Array, factorNewExpr.getType().struct);
			report_info("Created new array!", factorNewExpr);			
		}
		
//		if(factorNewExpr.getType().struct != Tab.intType && factorNewExpr.getType().struct != Tab.charType && factorNewExpr.getType().struct != boolType) {
//			//OSTALO DA SE PROVERI TIPOVI OVDE
//			report_error("ERROR: Elements of array must be of type: int, char or bool!", factorNewExpr);
//		} else if (factorNewExpr.getExpr().struct != Tab.intType ) {
//			
//		} else {
//			factorNewExpr.struct = new Struct(Struct.Array, factorNewExpr.getType().struct);
//			report_info("Created new array!", factorNewExpr);
//		}
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactDesig dactDesig) {
		dactDesig.struct = dactDesig.getDesignator().obj.getType();
	}

	// Print
	// ------------------------------------------------------------------------------------------

	public void visit(PrintStatementNum printStatementNum) {

	}

	// Help Methods
	// -----------------------------------------------------------------------------------

	private boolean checkIfArray(Obj obj, SyntaxNode node) {
		if(obj.getType().getKind() == Struct.Array)
			return true;
		
		report_error("ERROR: Can't assigne value to non element from array!", node);
		return false;
	}
	
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

	private boolean canAssign(Struct left, Struct right) {
		if (left.getKind() == Struct.Array && right.getKind() == Struct.Array) {
			left = left.getElemType();
			right = right.getElemType();
		} else if(left.getKind() == Struct.Array && right.getKind() != Struct.Array) {
			left = left.getElemType();
		}
		
		if(left == right) {
			return true;
		} 
		
		return false;
	}
	
	public boolean passed() {
		return !errorDetected;
	}

}

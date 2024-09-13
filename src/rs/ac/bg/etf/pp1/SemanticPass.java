package rs.ac.bg.etf.pp1;

import java.util.HashMap;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int globalCnt = 0;
	int localCnt = 0;
	int constCnt = 0;
	boolean errorDetected = false;
	Struct currentType = null;
	Obj currentMethod = null;
	String currentNamespace = null;
	boolean isNamespace = false;
	int nVars;
	
	HashMap<Obj, DesigStatementAssign> map = new HashMap<>();

	Struct boolType = new Struct(5);
	Obj boolObj = new Obj(Obj.Type, "bool", boolType);
	Logger log = Logger.getLogger(getClass());
	
	SemanticPass() {
		Tab.currentScope().addToLocals(boolObj);
	}

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
			report_error("Error: Type " + type.getTypeName() + " not found in symbol table!", type);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				currentType = typeNode.getType();
				type.struct = currentType;
			} else {
				report_error("ERROR: Name " + type.getTypeName() + " is not a type!", type);
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

			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
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
			
			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
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
			
			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
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
			
			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
		}
	}
	
	public void visit(VarDeclListOneMat varDecla) {
		String name = varDecla.getVName();
		
		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
//			System.out.println("Tip je: " + varDecla.obj.getType().getKind());
//			System.out.println("Tip tipa je: " + varDecla.obj.getType().getElemType().getKind());
//			System.out.println("Elem je: " + varDecla.obj.getType().getElemType().getElemType().getKind());
			report_info("Detected new matrix variable " + "[" + varDecla.getVName() + "]" + " on", varDecla);
			
			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
		}
	}
	
	public void visit(VarDeclListMat varDecla) {
		String name = varDecla.getVName();
		
		if (!symbolInSameScope(name, varDecla)) {
			varDecla.obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
			report_info("Detected new matrix variable " + "[" + varDecla.getVName() + "]" + " on", varDecla);
			
			if (currentMethod != null) 
				localCnt++;
			else 
				globalCnt++;
		}
	}

	// ConstDecl-----------------------------------------------------------------------------------------------------------

	public void visit(ConstType constType) {
		currentType = constType.getType().struct;
	}

	public void visit(IntValue intValue) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + intValue.getName();
		else
			name = intValue.getName();

		if (!symbolInSameScope(name, intValue)) {
			if(intValue.getValue() instanceof Integer) {
				Obj obj = Tab.insert(Obj.Con, name, currentType);
				
				obj.setAdr(intValue.getValue());
					
				String temp = isNamespace ? "[" + intValue.getName() + "]" + " in namespace:" + currentNamespace
						: "[" + intValue.getName() + "]";
				report_info("Detected new constant " + temp + " on", intValue);
				constCnt++;
			}
		}
	}

	public void visit(CharValue charValue) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + charValue.getName();
		else
			name = charValue.getName();

		if (!symbolInSameScope(name, charValue)) {
			if(charValue.getValue() instanceof Character) {
				Obj obj = Tab.insert(Obj.Con, name, currentType);
				
				obj.setAdr(charValue.getValue());
					
				String temp = isNamespace ? "[" + charValue.getName() + "]" + " in namespace:" + currentNamespace
						: "[" + charValue.getName() + "]";
				report_info("Detected new constant " + temp + " on", charValue);
				constCnt++;
			}
		}
	}

	public void visit(BoolValue boolValue) {
		String name;
		if (isNamespace)
			name = currentNamespace + "::" + boolValue.getName();
		else
			name = boolValue.getName();

		if (!symbolInSameScope(name, boolValue)) {
			if(boolValue.getValue() instanceof Boolean) {
				Obj obj = Tab.insert(Obj.Con, name, currentType);
				
				obj.setAdr(boolValue.getValue() ? 1 : 0);
					
				String temp = isNamespace ? "[" + boolValue.getName() + "]" + " in namespace:" + currentNamespace
						: "[" + boolValue.getName() + "]";
				report_info("Detected new constant " + temp + " on", boolValue);
				constCnt++;
			}
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
	}

	public void visit(MethodVarDecl methodVarDecl) {
		currentType = methodVarDecl.getType().struct;
	}

	public void visit(MethodVarr methodVarr) {
		if (!symbolInSameScope(methodVarr.getVName(), methodVarr)) {
			Obj elem = Tab.insert(Obj.Var, methodVarr.getVName(), currentType);
			report_info("Detected new method variable [" + methodVarr.getVName() + "] in [" + currentMethod.getName() + "] on", methodVarr);
			localCnt++;
		}		
	}

	public void visit(MethodVarArr methodVarr) {
		if (!symbolInSameScope(methodVarr.getVName(), methodVarr)) {
			Obj elem = Tab.insert(Obj.Var, methodVarr.getVName(), new Struct(Struct.Array, currentType));
			report_info("Detected new method array [" + methodVarr.getVName()+ "] in [" + currentMethod.getName() + "] on", methodVarr);
			localCnt++;
		}
	}

	// Designator
	// -------------------------------------------------------------------------------------
//	public void visit(Designatorr designator) {		
//		if (Tab.find(designator.getDesignatorName().obj.getName()) == Tab.noObj) {
//			report_error("ERROR: Var/Arr " + designator.getDesignatorName().obj.getName() + " not found!", designator);
//		}
//		
//		designator.obj = designator.getDesignatorName().obj;
//	}
//	
//	public void visit(DesignatorrArr designator) {
//		designator.obj = designator.getDesignatorName().obj;
//		
//		if (Tab.find(designator.getDesignatorName().obj.getName()) == Tab.noObj) {
//			report_error("ERROR: Array " + designator.getDesignatorName().obj.getName() + " not found!", designator);
//		}
//		
//		designator.obj = new Obj(Obj.Elem, "arrayElem", designator.obj.getType().getElemType());
//	}
//
//	public void visit(DesignatorrMat designator) {
//		Obj desig = Tab.find(designator.getDesignatorName().obj.getName());
//
//		if (desig == Tab.noObj) {
//			report_error("ERROR: Matrix " + designator.getDesignatorName().obj.getName() + " not found!", designator);
//		} else if(desig.getType().getElemType().getKind() != Struct.Array) {
//			report_error("ERROR: " + designator.getDesignatorName().obj.getName() + " isn't matrix.", designator);
//		} else if(designator.getExpr().struct != Tab.intType || designator.getExpr1().struct != Tab.intType) {
//			report_error("ERROR: Matrix rows and columns must be int type.", designator);
//		}
//		
//		designator.obj = new Obj(Obj.Elem, "", desig.getType().getElemType().getElemType());
//	}
		
	public void visit(DesignatorNameNoNs desig){
		Obj obj = Tab.find(desig.getName());
		if (obj == Tab.noObj)
			report_error("ERROR: This var/con "+ desig.getName() + " doesn't exist!", desig);
		else {
			String gloloc = obj.getLevel() == 0 ? "local" : "global";
			if(obj.getKind() == Obj.Con)
				report_info("Using " + gloloc + " constant [" + obj.getName() + "]", desig);
			
			if( obj.getKind() == Obj.Var) 
				report_info("Using " + gloloc + " variable [" + obj.getName() + "]", desig);
			
			if(desig.getParent() instanceof DesignatorStatementDec || desig.getParent() instanceof DesignatorStatementInc || desig.getParent() instanceof FactDesig) {
				DesigStatementAssign desigStatementAssign = map.get(obj);
				if (desigStatementAssign != null)
					desigStatementAssign.obj = obj;
			}
		}
		desig.obj = obj;
	}
	
	public void visit(DesignatorNameNs designator) {
		designator.obj = new Obj(Obj.Var, designator.getNsName() + "::" + designator.getName(), new Struct(Struct.None));
	}
	
	public void visit(DesignatorArrayMatrix desig){
		Obj arrayObj = desig.getDesignatorName().getDesignator().obj;
		desig.obj = new Obj(Obj.Elem, "", arrayObj.getType().getElemType());
		
		if(arrayObj.getType().getKind() != Struct.Array ) {
			report_error("ERROR: Must be array", desig);
			desig.obj = Tab.noObj;
		}

		if(desig.getExpr().struct != Tab.intType){
			report_error("ERROR: Expr in [] must be of type int", desig);
			desig.obj = Tab.noObj;
		}
		
		DesigStatementAssign desigStatementAssign = map.get(arrayObj);
		if (desigStatementAssign != null) {
			desigStatementAssign.obj = arrayObj;
		}
	}
	
//	public void visit(DesignatorNameNoNs designator) {
//		Obj temp = Tab.find(designator.getDName());
//		
//		if(temp != Tab.noObj) {
//			designator.obj = temp;
//		} else {
//			report_error("ERROR: Var/Arr " + designator.getDName() + " not found!", designator);
//		}
//	}
//	
//	public void visit(DesignatorNameNs designator) {
//		designator.obj = new Obj(Obj.Var, designator.getNsName() + "::" + designator.getDName(),
//				new Struct(Struct.None));
//	}
	
	
	public void visit(DesigStatementAssign desigStmAssign) {
		Obj leftOp = desigStmAssign.getDesignator().obj;
		Struct rightOp = desigStmAssign.getExpr().struct;
 
//		System.out.println("Left: " + leftOp.getType().getKind());
//		System.out.println("Right: " + rightOp.getKind());
		if (leftOp.getKind() == Obj.Con) {
			report_error("ERROR: Left operand is const value, can't assigne value!", desigStmAssign);
		} else if (!canAssign(leftOp.getType(), rightOp)) {
			report_error("ERROR: Different types for assign", desigStmAssign);
		} else {
			report_info("Assigned new value to [" + desigStmAssign.getDesignator().obj.getName() + "] on",
					desigStmAssign);
		}
		
		map.put(leftOp, desigStmAssign);
	}

	public void visit(DesignatorStatementInc designator) {
		Struct temp;
		temp = designator.getDesignator().obj.getType().getElemType();
		if(temp == null)
			temp = Tab.intType;
		
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
		if(temp == null)
			temp = Tab.intType;
		
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
		Struct termTemp = term.getTerm().struct;
		Struct factTemp = term.getFactor().struct;

		if(termTemp.getKind() == Struct.Array) {
			termTemp = termTemp.getElemType();
			if(termTemp.getKind() == Struct.Array)
				termTemp = termTemp.getElemType();
		}
		
		if(factTemp.getKind() == Struct.Array) {
			factTemp = factTemp.getElemType();
			if(factTemp.getKind() == Struct.Array)
				factTemp = factTemp.getElemType();
		}
		
		if(termTemp != factTemp) 
			report_error("ERROR: Operands are different type!", term);	
		
		term.struct = termTemp;
	}

	public void visit(TermOne term) {
		term.struct = term.getFactor().struct;
	}

	// Factor 	
	// -------------------------------------------------------------------------------------------------------
	
	
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
			String temp = factorNewExpr.getFactorMat() instanceof FactorMatt ? "Matrix" : "Array";
			report_error("ERROR: " + temp + " length must be int value!", factorNewExpr);
		}
		
		if(factorNewExpr.getFactorMat() instanceof FactorMatt)
			factorNewExpr.struct = new Struct(Struct.Array, new Struct(Struct.Array, factorNewExpr.getType().struct));
		else
			factorNewExpr.struct = new Struct(Struct.Array, factorNewExpr.getType().struct);
	}
	
//	public void visit(FactorNewMat factorNewMat) {
//		if(factorNewMat.getExpr().struct != Tab.intType || factorNewMat.getExpr1().struct != Tab.intType) {
//			report_error("ERROR: Matrix row/column number must be int value!", factorNewMat);
//		}
//		
//		factorNewMat.struct = new Struct(Struct.Array, new Struct(Struct.Array, currentType));
//	}
	
	public void visit(FactorMatt factorMat) {
		if(factorMat.getExpr().struct != Tab.intType)
			report_error("ERROR: Matrix column number must be int value!", factorMat);
	
		factorMat.struct = factorMat.getExpr().struct;
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactDesig dactDesig) {
		dactDesig.struct = dactDesig.getDesignator().obj.getType();
	}
	
	public void visit(ReadStatement readStm) {
		Obj obj = readStm.getDesignator().obj;
		
		if(obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem) {
			report_error("ERROR: Object in read must be variable", readStm);
		}
		
		if(obj.getType() != Tab.intType && obj.getType() != Tab.charType)
			report_error("Error: Type can't be read!", readStm);
	}

	// Print
	// ------------------------------------------------------------------------------------------

	public void visit(PrintStatementNum printStatementNum) {
		Struct temp = printStatementNum.getExpr().struct;
		if(temp.getKind() == Struct.Array) {
			temp = temp.getElemType();
			if(temp.getKind() == Struct.Array)
				temp = temp.getElemType();
		}
		
		if (temp != Tab.intType && temp != Tab.charType && temp.getKind() != boolType.getKind())
			report_error("ERORR: Can't print this type", printStatementNum);
	}

	public void visit(PrintStatement printStatement) {
		Struct temp = printStatement.getExpr().struct;
		if(temp.getKind() == Struct.Array) {
			temp = temp.getElemType();
			if(temp.getKind() == Struct.Array)
				temp = temp.getElemType();
		}
		
		if (temp != Tab.intType && temp != Tab.charType && temp.getKind() != boolType.getKind())
			report_error("ERORR: Can't print this type", printStatement);
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
		if(left.getKind() == Struct.Array) {
			left = left.getElemType();
			if(left.getKind() == Struct.Array)
				left = left.getElemType();
		}
			
		if(right.getKind() == Struct.Array) {
			right = right.getElemType();
			if(right.getKind() == Struct.Array)
				right = right.getElemType();
		}
		
		if(left.getKind() == right.getKind()) {
			return true;
		} 
		
		return false;
	}
	
	public boolean passed() {
		return !errorDetected;
	}
}

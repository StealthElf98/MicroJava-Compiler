// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Unmatched Unmatched);
    public void visit(FactorEnd FactorEnd);
    public void visit(Mulop Mulop);
    public void visit(Matched Matched);
    public void visit(Relop Relop);
    public void visit(MethodVarList MethodVarList);
    public void visit(FactorMat FactorMat);
    public void visit(FormParamDecl FormParamDecl);
    public void visit(StatementList StatementList);
    public void visit(NamespaceList NamespaceList);
    public void visit(Addop Addop);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(DeclList DeclList);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(Condition Condition);
    public void visit(ConstDeclListEnd ConstDeclListEnd);
    public void visit(MethodVar MethodVar);
    public void visit(ExprList ExprList);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(VarDeclEnd VarDeclEnd);
    public void visit(ActPars ActPars);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(FormParamList FormParamList);
    public void visit(Decl Decl);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(RValue RValue);
    public void visit(ConstDecl ConstDecl);
    public void visit(StaticInitializer StaticInitializer);
    public void visit(CondFact CondFact);
    public void visit(MethodVarDeclList MethodVarDeclList);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(FormPars FormPars);
    public void visit(ConstDeclExtension ConstDeclExtension);
    public void visit(MulopMod MulopMod);
    public void visit(MulopDiv MulopDiv);
    public void visit(MulopMul MulopMul);
    public void visit(AddopMinus AddopMinus);
    public void visit(AddopPlus AddopPlus);
    public void visit(RelopLessEqual RelopLessEqual);
    public void visit(RelopLess RelopLess);
    public void visit(RelopGreaterEqual RelopGreaterEqual);
    public void visit(RelopGreater RelopGreater);
    public void visit(RelopNotEqual RelopNotEqual);
    public void visit(RelopEquals RelopEquals);
    public void visit(Assignop Assignop);
    public void visit(Label Label);
    public void visit(FactorEndd FactorEndd);
    public void visit(NoFactorMatt NoFactorMatt);
    public void visit(FactorMatt FactorMatt);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorNewActPars FactorNewActPars);
    public void visit(FactorNewExpr FactorNewExpr);
    public void visit(FactorBool FactorBool);
    public void visit(FactorChar FactorChar);
    public void visit(FactorInt FactorInt);
    public void visit(FactorActPars FactorActPars);
    public void visit(FactDesig FactDesig);
    public void visit(TermOne TermOne);
    public void visit(Termm Termm);
    public void visit(AddopExpr AddopExpr);
    public void visit(MinusExpr MinusExpr);
    public void visit(ExprTerm ExprTerm);
    public void visit(RelopCondFact RelopCondFact);
    public void visit(CondFactt CondFactt);
    public void visit(CondTermm CondTermm);
    public void visit(AndCondTerm AndCondTerm);
    public void visit(Conditionn Conditionn);
    public void visit(OrCondition OrCondition);
    public void visit(ExprListMore ExprListMore);
    public void visit(ExprListOne ExprListOne);
    public void visit(NoActPars NoActPars);
    public void visit(ActParss ActParss);
    public void visit(DesignatorName DesignatorName);
    public void visit(DesignatorNameNs DesignatorNameNs);
    public void visit(DesignatorNameNoNs DesignatorNameNoNs);
    public void visit(DesignatorArrayMatrix DesignatorArrayMatrix);
    public void visit(DesignatorStatementDec DesignatorStatementDec);
    public void visit(DesignatorStatementInc DesignatorStatementInc);
    public void visit(DesignatorStatementFunctionCall DesignatorStatementFunctionCall);
    public void visit(ErrorDesigStatementAssign ErrorDesigStatementAssign);
    public void visit(DesigStatementAssign DesigStatementAssign);
    public void visit(MultipleStatemnets MultipleStatemnets);
    public void visit(PrintStatementNum PrintStatementNum);
    public void visit(PrintStatement PrintStatement);
    public void visit(ReadStatement ReadStatement);
    public void visit(ReturnExprStatement ReturnExprStatement);
    public void visit(ReturnEmptyStatement ReturnEmptyStatement);
    public void visit(ContinueStatement ContinueStatement);
    public void visit(BreakStatement BreakStatement);
    public void visit(IfStatement IfStatement);
    public void visit(DesigStatement DesigStatement);
    public void visit(UnmatchedElse UnmatchedElse);
    public void visit(UnmatchedIf UnmatchedIf);
    public void visit(UnmatchedStm UnmatchedStm);
    public void visit(MatchedStm MatchedStm);
    public void visit(EmptyStatementList EmptyStatementList);
    public void visit(StmList StmList);
    public void visit(FormParamDeclArr FormParamDeclArr);
    public void visit(FormParamDeclVar FormParamDeclVar);
    public void visit(FormParamListOne FormParamListOne);
    public void visit(FormParamListMore FormParamListMore);
    public void visit(NoFormParss NoFormParss);
    public void visit(FormParss FormParss);
    public void visit(MethodVarArr MethodVarArr);
    public void visit(MethodVarr MethodVarr);
    public void visit(SignleMethodVarList SignleMethodVarList);
    public void visit(MethodVarListt MethodVarListt);
    public void visit(MethodVarDecl MethodVarDecl);
    public void visit(NoMethodVarDeclListt NoMethodVarDeclListt);
    public void visit(MethodVarDeclListt MethodVarDeclListt);
    public void visit(VoidMethodType VoidMethodType);
    public void visit(NonVoidMethodType NonVoidMethodType);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDeclListt NoMethodDeclListt);
    public void visit(MethodDeclListt MethodDeclListt);
    public void visit(StaticInitializerDerived1 StaticInitializerDerived1);
    public void visit(Type Type);
    public void visit(VarDeclEndDerived1 VarDeclEndDerived1);
    public void visit(VarDeclListOneMat VarDeclListOneMat);
    public void visit(VarDeclListOneArr VarDeclListOneArr);
    public void visit(VarDeclListOneVar VarDeclListOneVar);
    public void visit(VarDeclListVar VarDeclListVar);
    public void visit(VarDeclListArr VarDeclListArr);
    public void visit(VarDeclListMat VarDeclListMat);
    public void visit(VarType VarType);
    public void visit(ErrorVarDecl ErrorVarDecl);
    public void visit(VarDecll VarDecll);
    public void visit(CharValue CharValue);
    public void visit(BoolValue BoolValue);
    public void visit(IntValue IntValue);
    public void visit(ConstDeclListEndd ConstDeclListEndd);
    public void visit(ConstDeclExtensionnOne ConstDeclExtensionnOne);
    public void visit(ConstDeclExtensionn ConstDeclExtensionn);
    public void visit(ConstType ConstType);
    public void visit(ConstDecll ConstDecll);
    public void visit(DeclVarr DeclVarr);
    public void visit(DeclConstt DeclConstt);
    public void visit(NoDeclListt NoDeclListt);
    public void visit(DeclListt DeclListt);
    public void visit(NamespaceName NamespaceName);
    public void visit(Namespace Namespace);
    public void visit(NoNamespaceListt NoNamespaceListt);
    public void visit(NamespaceListt NamespaceListt);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}

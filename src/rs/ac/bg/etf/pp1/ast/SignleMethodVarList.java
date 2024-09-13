// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class SignleMethodVarList extends MethodVarList {

    private MethodVar MethodVar;

    public SignleMethodVarList (MethodVar MethodVar) {
        this.MethodVar=MethodVar;
        if(MethodVar!=null) MethodVar.setParent(this);
    }

    public MethodVar getMethodVar() {
        return MethodVar;
    }

    public void setMethodVar(MethodVar MethodVar) {
        this.MethodVar=MethodVar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodVar!=null) MethodVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodVar!=null) MethodVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodVar!=null) MethodVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SignleMethodVarList(\n");

        if(MethodVar!=null)
            buffer.append(MethodVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SignleMethodVarList]");
        return buffer.toString();
    }
}

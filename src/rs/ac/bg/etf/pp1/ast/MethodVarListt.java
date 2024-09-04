// generated with ast extension for cup
// version 0.8
// 4/8/2024 13:23:10


package rs.ac.bg.etf.pp1.ast;

public class MethodVarListt extends MethodVarList {

    private MethodVarList MethodVarList;
    private MethodVar MethodVar;

    public MethodVarListt (MethodVarList MethodVarList, MethodVar MethodVar) {
        this.MethodVarList=MethodVarList;
        if(MethodVarList!=null) MethodVarList.setParent(this);
        this.MethodVar=MethodVar;
        if(MethodVar!=null) MethodVar.setParent(this);
    }

    public MethodVarList getMethodVarList() {
        return MethodVarList;
    }

    public void setMethodVarList(MethodVarList MethodVarList) {
        this.MethodVarList=MethodVarList;
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
        if(MethodVarList!=null) MethodVarList.accept(visitor);
        if(MethodVar!=null) MethodVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodVarList!=null) MethodVarList.traverseTopDown(visitor);
        if(MethodVar!=null) MethodVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodVarList!=null) MethodVarList.traverseBottomUp(visitor);
        if(MethodVar!=null) MethodVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodVarListt(\n");

        if(MethodVarList!=null)
            buffer.append(MethodVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodVar!=null)
            buffer.append(MethodVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodVarListt]");
        return buffer.toString();
    }
}

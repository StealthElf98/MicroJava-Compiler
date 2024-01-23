// generated with ast extension for cup
// version 0.8
// 22/0/2024 19:59:34


package rs.ac.bg.etf.pp1.ast;

public class MethodVarr extends MethodVar {

    private String vName;

    public MethodVarr (String vName) {
        this.vName=vName;
    }

    public String getVName() {
        return vName;
    }

    public void setVName(String vName) {
        this.vName=vName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodVarr(\n");

        buffer.append(" "+tab+vName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodVarr]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 15/0/2024 18:46:1


package rs.ac.bg.etf.pp1.ast;

public class NoMethodVarDeclListt extends MethodVarDeclList {

    public NoMethodVarDeclListt () {
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
        buffer.append("NoMethodVarDeclListt(\n");

        buffer.append(tab);
        buffer.append(") [NoMethodVarDeclListt]");
        return buffer.toString();
    }
}

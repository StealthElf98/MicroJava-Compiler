// generated with ast extension for cup
// version 0.8
// 4/8/2024 13:23:10


package rs.ac.bg.etf.pp1.ast;

public class NoDeclListt extends DeclList {

    public NoDeclListt () {
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
        buffer.append("NoDeclListt(\n");

        buffer.append(tab);
        buffer.append(") [NoDeclListt]");
        return buffer.toString();
    }
}

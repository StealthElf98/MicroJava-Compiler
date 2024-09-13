// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclExtensionnOne extends ConstDeclExtension {

    private RValue RValue;

    public ConstDeclExtensionnOne (RValue RValue) {
        this.RValue=RValue;
        if(RValue!=null) RValue.setParent(this);
    }

    public RValue getRValue() {
        return RValue;
    }

    public void setRValue(RValue RValue) {
        this.RValue=RValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RValue!=null) RValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RValue!=null) RValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RValue!=null) RValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclExtensionnOne(\n");

        if(RValue!=null)
            buffer.append(RValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclExtensionnOne]");
        return buffer.toString();
    }
}

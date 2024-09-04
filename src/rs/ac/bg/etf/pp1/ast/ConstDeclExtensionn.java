// generated with ast extension for cup
// version 0.8
// 4/8/2024 13:23:10


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclExtensionn extends ConstDeclExtension {

    private ConstDeclExtension ConstDeclExtension;
    private String cName;
    private RValue RValue;

    public ConstDeclExtensionn (ConstDeclExtension ConstDeclExtension, String cName, RValue RValue) {
        this.ConstDeclExtension=ConstDeclExtension;
        if(ConstDeclExtension!=null) ConstDeclExtension.setParent(this);
        this.cName=cName;
        this.RValue=RValue;
        if(RValue!=null) RValue.setParent(this);
    }

    public ConstDeclExtension getConstDeclExtension() {
        return ConstDeclExtension;
    }

    public void setConstDeclExtension(ConstDeclExtension ConstDeclExtension) {
        this.ConstDeclExtension=ConstDeclExtension;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName=cName;
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
        if(ConstDeclExtension!=null) ConstDeclExtension.accept(visitor);
        if(RValue!=null) RValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclExtension!=null) ConstDeclExtension.traverseTopDown(visitor);
        if(RValue!=null) RValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclExtension!=null) ConstDeclExtension.traverseBottomUp(visitor);
        if(RValue!=null) RValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclExtensionn(\n");

        if(ConstDeclExtension!=null)
            buffer.append(ConstDeclExtension.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+cName);
        buffer.append("\n");

        if(RValue!=null)
            buffer.append(RValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclExtensionn]");
        return buffer.toString();
    }
}

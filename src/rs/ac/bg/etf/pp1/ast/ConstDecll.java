// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class ConstDecll extends ConstDecl {

    private ConstType ConstType;
    private ConstDeclExtension ConstDeclExtension;
    private ConstDeclListEnd ConstDeclListEnd;

    public ConstDecll (ConstType ConstType, ConstDeclExtension ConstDeclExtension, ConstDeclListEnd ConstDeclListEnd) {
        this.ConstType=ConstType;
        if(ConstType!=null) ConstType.setParent(this);
        this.ConstDeclExtension=ConstDeclExtension;
        if(ConstDeclExtension!=null) ConstDeclExtension.setParent(this);
        this.ConstDeclListEnd=ConstDeclListEnd;
        if(ConstDeclListEnd!=null) ConstDeclListEnd.setParent(this);
    }

    public ConstType getConstType() {
        return ConstType;
    }

    public void setConstType(ConstType ConstType) {
        this.ConstType=ConstType;
    }

    public ConstDeclExtension getConstDeclExtension() {
        return ConstDeclExtension;
    }

    public void setConstDeclExtension(ConstDeclExtension ConstDeclExtension) {
        this.ConstDeclExtension=ConstDeclExtension;
    }

    public ConstDeclListEnd getConstDeclListEnd() {
        return ConstDeclListEnd;
    }

    public void setConstDeclListEnd(ConstDeclListEnd ConstDeclListEnd) {
        this.ConstDeclListEnd=ConstDeclListEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstType!=null) ConstType.accept(visitor);
        if(ConstDeclExtension!=null) ConstDeclExtension.accept(visitor);
        if(ConstDeclListEnd!=null) ConstDeclListEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstType!=null) ConstType.traverseTopDown(visitor);
        if(ConstDeclExtension!=null) ConstDeclExtension.traverseTopDown(visitor);
        if(ConstDeclListEnd!=null) ConstDeclListEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstType!=null) ConstType.traverseBottomUp(visitor);
        if(ConstDeclExtension!=null) ConstDeclExtension.traverseBottomUp(visitor);
        if(ConstDeclListEnd!=null) ConstDeclListEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecll(\n");

        if(ConstType!=null)
            buffer.append(ConstType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclExtension!=null)
            buffer.append(ConstDeclExtension.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclListEnd!=null)
            buffer.append(ConstDeclListEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecll]");
        return buffer.toString();
    }
}

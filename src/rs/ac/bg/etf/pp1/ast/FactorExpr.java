// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class FactorExpr extends Factor {

    private Expr Expr;
    private FactorEnd FactorEnd;

    public FactorExpr (Expr Expr, FactorEnd FactorEnd) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.FactorEnd=FactorEnd;
        if(FactorEnd!=null) FactorEnd.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public FactorEnd getFactorEnd() {
        return FactorEnd;
    }

    public void setFactorEnd(FactorEnd FactorEnd) {
        this.FactorEnd=FactorEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(FactorEnd!=null) FactorEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(FactorEnd!=null) FactorEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(FactorEnd!=null) FactorEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorExpr(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorEnd!=null)
            buffer.append(FactorEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorExpr]");
        return buffer.toString();
    }
}

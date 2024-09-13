// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class FormParamListOne extends FormParamList {

    private FormParamDecl FormParamDecl;

    public FormParamListOne (FormParamDecl FormParamDecl) {
        this.FormParamDecl=FormParamDecl;
        if(FormParamDecl!=null) FormParamDecl.setParent(this);
    }

    public FormParamDecl getFormParamDecl() {
        return FormParamDecl;
    }

    public void setFormParamDecl(FormParamDecl FormParamDecl) {
        this.FormParamDecl=FormParamDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParamDecl!=null) FormParamDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParamDecl!=null) FormParamDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParamDecl!=null) FormParamDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParamListOne(\n");

        if(FormParamDecl!=null)
            buffer.append(FormParamDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParamListOne]");
        return buffer.toString();
    }
}

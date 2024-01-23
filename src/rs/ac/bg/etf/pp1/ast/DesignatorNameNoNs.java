// generated with ast extension for cup
// version 0.8
// 22/0/2024 19:59:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorNameNoNs extends DesignatorName {

    private String dName;

    public DesignatorNameNoNs (String dName) {
        this.dName=dName;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName=dName;
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
        buffer.append("DesignatorNameNoNs(\n");

        buffer.append(" "+tab+dName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorNameNoNs]");
        return buffer.toString();
    }
}

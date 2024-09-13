// generated with ast extension for cup
// version 0.8
// 13/8/2024 13:31:47


package rs.ac.bg.etf.pp1.ast;

public class DesignatorNameNs extends Designator {

    private String nsName;
    private String name;

    public DesignatorNameNs (String nsName, String name) {
        this.nsName=nsName;
        this.name=name;
    }

    public String getNsName() {
        return nsName;
    }

    public void setNsName(String nsName) {
        this.nsName=nsName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
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
        buffer.append("DesignatorNameNs(\n");

        buffer.append(" "+tab+nsName);
        buffer.append("\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorNameNs]");
        return buffer.toString();
    }
}

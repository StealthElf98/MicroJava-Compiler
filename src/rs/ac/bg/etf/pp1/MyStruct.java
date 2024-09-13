package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Struct;

public class MyStruct extends Struct {
	public static final int Bool = 5;
	public static final int Matrix = 6;
	private Struct elemType;
	
	public MyStruct(int kind) {
		super(kind);
		// TODO Auto-generated constructor stub
	}

	public MyStruct(int kind, Struct elemType) {
		super(kind);
		this.elemType = elemType;
	}
}

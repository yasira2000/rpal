package abstractSyntaxTree;


 // Abstract syntax tree node type
public enum ASTNodeType{
  IDENTIFIER("<ID:%s>"),
  STRING("<STR:'%s'>"),
  INTEGER("<INT:%s>"),

  LET("let"),
  LAMBDA("lambda"),
  WHERE("where"),

  TAU("tau"),
  AUG("aug"),
  CONDITIONAL("->"),

  OR("or"),
  AND("&"),
  NOT("not"),
  GR("gr"),
  GE("ge"),
  LS("ls"),
  LE("le"),
  EQ("eq"),
  NE("ne"),

  PLUS("+"),
  MINUS("-"),
  NEG("neg"),
  MULT("*"),
  DIV("/"),
  EXP("**"),
  AT("@"),

  GAMMA("gamma"),
  TRUE("<true>"),
  FALSE("<false>"),
  NIL("<nil>"),
  DUMMY("<dummy>"),

  WITHIN("within"),
  SIMULTDEF("and"),
  REC("rec"),
  EQUAL("="),
  FCNFORM("function_form"),

  PAREN("<()>"),
  COMMA(","),

  YSTAR("<Y*>"),

  BETA(""),
  DELTA(""),
  ETA(""),
  TUPLE("");
  
  private String printName;
  
  private ASTNodeType(String name){
    printName = name;
  }

  public String getPrintName(){
    return printName;
  }
}

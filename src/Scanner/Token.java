package Scanner;



 //The scanner provides tokens to the parser.
 //A token consists of both a type and a value, with the value being insignificant.

public class Token{
  private TokenType type;
  private String value;
  private int sourceLineNumber;
  
  public TokenType getType(){
    return type;
  }
  
  public void setType(TokenType type){
    this.type = type;
  }
  
  public String getValue(){
    return value;
  }
  
  public void setValue(String value){
    this.value = value;
  }

  public int getSourceLineNumber(){
    return sourceLineNumber;
  }

  public void setSourceLineNumber(int sourceLineNumber){
    this.sourceLineNumber = sourceLineNumber;
  }
}

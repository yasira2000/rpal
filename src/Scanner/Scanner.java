package Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

//Scanner(Lexical analysis)
public class Scanner{
  private BufferedReader buffer;
  private String extraCharRead;
  private final List<String> reservedIdentifiers = Arrays.asList(new String[]{"let","in","within","fn","where","aug","or",
                                                                              "not","gr","ge","ls","le","eq","ne","true",
                                                                              "false","nil","dummy","rec","and"});
  private int sourceLineNumber;
  
  public Scanner(String inputFile) throws IOException{
    sourceLineNumber = 1;
    buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
  }
  
  //The function returns the next token from the input file and returns null if the file has reached its end.

  public Token readNextToken(){
    Token nextToken = null;
    String nextChar;
    if(extraCharRead!=null){
      nextChar = extraCharRead;
      extraCharRead = null;
    } else
      nextChar = readNextChar();
    if(nextChar!=null)
      nextToken = buildToken(nextChar);
    return nextToken;
  }

  private String readNextChar(){
    String nextChar = null;
    try{
      int c = buffer.read();
      if(c!=-1){
        nextChar = Character.toString((char)c);
        if(nextChar.equals("\n")) sourceLineNumber++;
      } else
          buffer.close();
    }catch(IOException e){
    }
    return nextChar;
  }


  //This function constructs the next token from the input, using the character currentChar that is currently being processed.
  //It returns the token that was built

  private Token buildToken(String currentChar){
    Token nextToken = null;
    if(LexicalPatterns.LetterPattern.matcher(currentChar).matches()){
      nextToken = buildIdentifierToken(currentChar);
    }
    else if(LexicalPatterns.DigitPattern.matcher(currentChar).matches()){
      nextToken = buildIntegerToken(currentChar);
    }
    else if(LexicalPatterns.OpSymbolPattern.matcher(currentChar).matches()){ //comment tokens are also entered from here
      nextToken = buildOperatorToken(currentChar);
    }
    else if(currentChar.equals("\'")){
      nextToken = buildStringToken(currentChar);
    }
    else if(LexicalPatterns.SpacePattern.matcher(currentChar).matches()){
      nextToken = buildSpaceToken(currentChar);
    }
    else if(LexicalPatterns.PunctuationPattern.matcher(currentChar).matches()){
      nextToken = buildPunctuationPattern(currentChar);
    }
    return nextToken;
  }

  // This function constructs an Identifier token.
  //  An Identifier follows the pattern: Letter (Letter | Digit | '_')*
  //  It uses the character `currentChar` that is currently being processed.
  // It returns the token that was built, representing the Identifier.
  private Token buildIdentifierToken(String currentChar){
    Token identifierToken = new Token();
    identifierToken.setType(TokenType.IDENTIFIER);
    identifierToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalPatterns.IdentifierPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    String value = sBuilder.toString();
    if(reservedIdentifiers.contains(value))
      identifierToken.setType(TokenType.RESERVED);
    
    identifierToken.setValue(value);
    return identifierToken;
  }

  // This function constructs an Integer token.
  // An Integer consists of one or more digits: Digit+
  // It uses the character `currentChar` that is currently being processed.
  // It returns the token that was built, representing the Integer.
  private Token buildIntegerToken(String currentChar){
    Token integerToken = new Token();
    integerToken.setType(TokenType.INTEGER);
    integerToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalPatterns.DigitPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    integerToken.setValue(sBuilder.toString());
    return integerToken;
  }

 // This function constructs an Operator token.
 // An Operator is composed of one or more Operator_symbol characters.
 //It uses the character `currentChar` that is currently being processed.
 // It returns the token that was built, representing the Operator.
  private Token buildOperatorToken(String currentChar){
    Token opSymbolToken = new Token();
    opSymbolToken.setType(TokenType.OPERATOR);
    opSymbolToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    
    if(currentChar.equals("/") && nextChar.equals("/"))
      return buildCommentToken(currentChar+nextChar);
    
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalPatterns.OpSymbolPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    opSymbolToken.setValue(sBuilder.toString());
    return opSymbolToken;
  }

  // This function constructs a String token.
  // A String is enclosed within four single quotes (''').
  // The content of the String can include various characters, including escape sequences like '\t', '\n', '\\', '\'', '(', ')', ';', ',', '', Letters, Digits, and Operator_symbols.
  // It uses the character `currentChar` that is currently being processed.
  // It returns the token that was built, representing the String.
  private Token buildStringToken(String currentChar){
    Token stringToken = new Token();
    stringToken.setType(TokenType.STRING);
    stringToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder("");
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(nextChar.equals("\'")){ //we just used up the last char we read, hence no need to set extraCharRead
        //sBuilder.append(nextChar);
        stringToken.setValue(sBuilder.toString());
        return stringToken;
      }
      else if(LexicalPatterns.StringPattern.matcher(nextChar).matches()){ //match Letter | Digit | Operator_symbol
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
    }
    
    return null;
  }
  
  private Token buildSpaceToken(String currentChar){
    Token deleteToken = new Token();
    deleteToken.setType(TokenType.DELETE);
    deleteToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalPatterns.SpacePattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else{
        extraCharRead = nextChar;
        break;
      }
    }
    
    deleteToken.setValue(sBuilder.toString());
    return deleteToken;
  }
  
  private Token buildCommentToken(String currentChar){
    Token commentToken = new Token();
    commentToken.setType(TokenType.DELETE);
    commentToken.setSourceLineNumber(sourceLineNumber);
    StringBuilder sBuilder = new StringBuilder(currentChar);
    
    String nextChar = readNextChar();
    while(nextChar!=null){ //null indicates the file ended
      if(LexicalPatterns.CommentPattern.matcher(nextChar).matches()){
        sBuilder.append(nextChar);
        nextChar = readNextChar();
      }
      else if(nextChar.equals("\n"))
        break;
    }
    
    commentToken.setValue(sBuilder.toString());
    return commentToken;
  }

  private Token buildPunctuationPattern(String currentChar){
    Token punctuationToken = new Token();
    punctuationToken.setSourceLineNumber(sourceLineNumber);
    punctuationToken.setValue(currentChar);
    if(currentChar.equals("("))
      punctuationToken.setType(TokenType.L_PAREN);
    else if(currentChar.equals(")"))
      punctuationToken.setType(TokenType.R_PAREN);
    else if(currentChar.equals(";"))
      punctuationToken.setType(TokenType.SEMICOLON);
    else if(currentChar.equals(","))
      punctuationToken.setType(TokenType.COMMA);
    
    return punctuationToken;
  }
}


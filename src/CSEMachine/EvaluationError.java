package CSEMachine;



public class EvaluationError{
  
  public static void printError(int sourceLineNumber, String message){
    System.out.println(":"+sourceLineNumber+": "+message); //P2.filename()+
    System.exit(1);
  }

}

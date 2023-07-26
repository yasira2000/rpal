package CSEMachine;

import java.util.HashMap;
import java.util.Map;

import abstractSyntaxTree.ASTNode;

public class Environment{
  private Environment parent;
  private Map<String, ASTNode> nameValueMap;
  
  public Environment(){
    nameValueMap = new HashMap<String, ASTNode>();
  }

  public Environment getParent(){
    return parent;
  }

  public void setParent(Environment parent){
    this.parent = parent;
  }
  
// Attempts to locate the binding of the provided key within the mappings of this Environment's inheritance hierarchy, starting from the Environment where this method is called.
// Parameters: key - The key for which to find the mapping within the hierarchy.
// Returns: The ASTNode that corresponds to the mapping of the given key passed as an argument, or null if no mapping was found.
  public ASTNode lookup(String key){
    ASTNode retValue = null;
    Map<String, ASTNode> map = nameValueMap;
    
    retValue = map.get(key);
    
    if(retValue!=null)
      return retValue.accept(new NodeCopier());
    
    if(parent!=null)
      return parent.lookup(key);
    else
      return null;
  }
  
  public void addMapping(String key, ASTNode value){
    nameValueMap.put(key, value);
  }
}

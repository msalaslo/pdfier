import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class ListEngineFactoryDemo {

  public static void main(String[] args) {
    // create ScriptEngineManager
    ScriptEngineManager manager = new ScriptEngineManager();
    List<ScriptEngineFactory> factoryList = manager.getEngineFactories();
    for (ScriptEngineFactory factory : factoryList) {
      System.out.println(factory.getEngineName());
      System.out.println(factory.getLanguageName());
    }
  }
}
//Mozilla Rhino
//ECMAScript

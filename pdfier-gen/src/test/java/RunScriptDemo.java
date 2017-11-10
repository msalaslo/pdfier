import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RunScriptDemo {

    public static void main (String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager ();
        ScriptEngine engine = manager.getEngineByName ("js");
        String script = "print ('www.java2s.com')";
        try {
            engine.eval (script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
//www.java2s.com
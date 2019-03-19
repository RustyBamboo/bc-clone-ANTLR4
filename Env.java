import java.util.HashMap;
import java.util.Map;

public class Env {

    private Env parent;
    private Map<String, Double> envVars;

    Env() {
        this(null);
    }

    Env(Env p) {
        parent = p;
        envVars = new HashMap<>();
    }
    
    public void assignParam(String Id, Double value) {
    	envVars.put(Id, value);
    }
    
    public void assign(String Id, Double value) {
        if(resolve(Id) != null)
            this.reAssign(Id, value);
        else
            envVars.put(Id, value);
    }

    private boolean isGlobalEnv() {
        return parent == null;
    }

    public Env parent() {
        return parent;
    }

    private void reAssign(String Id, Double value) {
        if(envVars.containsKey(Id))
            envVars.put(Id, value);
        else if(parent != null)
            parent.reAssign(Id, value);
    }

    public Double resolve(String Id) {
        Double value = envVars.get(Id);
        if(value != null)
            return value;
        else if(!isGlobalEnv())
            return parent.resolve(Id);
        else
            return null;
    }
}

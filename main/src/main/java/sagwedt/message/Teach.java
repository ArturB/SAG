package sagwedt.message;

public class Teach {
    private String agentPath;
    private Learn learnTask;

    public Teach(String agentPath, Learn learnTask) {
        this.agentPath = agentPath;
        this.learnTask = learnTask;
    }

    public String getAgentPath() {
        return agentPath;
    }

    public void setAgentPath(String agentPath) {
        this.agentPath = agentPath;
    }

    public Learn getLearnTask() {
        return learnTask;
    }

    public void setLearnTask(Learn learnTask) {
        this.learnTask = learnTask;
    }
}

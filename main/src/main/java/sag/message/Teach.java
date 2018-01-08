package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany do nauczyciela. Zawiera zlecenie wykonania uczenia przez określonego adresem agenta.
 */
public class Teach implements Serializable {
    private String agentPath;
    private Learn learnTask;
    private static final long serialVersionUID = 594059395443953L;

    /**
     *
     * @param agentPath Adres agenta który powinien wykonać uczenie.
     * @param learnTask Zadanie uczenia - ścieżki do baz danych oraz inne parametry.
     */
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

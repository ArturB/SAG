package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez klasyfikator do nauczyciela w odpowiedzi na komunikat {@link sag.message.Learn}. Zawiera informacje nt. efektu uczenia - sukcesie bądź błędzie.
 */
public class LearnReply implements Serializable {
    private Boolean success;
    private String msg;
    private static final long serialVersionUID = 48309483980520L;

    /**
     * @param success Czy uczenie zakończono sukcesem?
     * @param msg Jeśli nie, komunikat o przyczynie błędu.
     */
    public LearnReply(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

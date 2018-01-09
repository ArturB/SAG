package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez serwer do requestera, jeśli w systemie nie ma aktualnie żadnych uruchomionych agentów.
 */
public class NoAgents implements Serializable {
    private static final long serialVersionUID = 4830948398450520L;
}

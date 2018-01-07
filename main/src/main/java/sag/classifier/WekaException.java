package sag.classifier;

public class WekaException extends Exception
{
	private static final long serialVersionUID = 3014252059706722343L;

	public WekaException () {
    }

    public WekaException(String message) {
        super(message);
    }

    public WekaException(Throwable cause) {
        super(cause);
    }

    public WekaException(String message, Throwable cause) {
        super(message, cause);
    }
}

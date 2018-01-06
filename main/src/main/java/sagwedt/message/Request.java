package sagwedt.message;

/**
 * Komunikat typu Request. Wysyłany przez Mastera do wszystkich klasyfikatorów, zawierający tekst do sklasyfikowania. Klasyfikator po otrzymaniu zlecenia wysyła odpowiedź albo {@code Reponse} jeśli jest w stanie wykonać zadanie, albo {@code Untrained} jeśli nie jest jeszcze nauczony.
 */
public class Request {
    private String textToClassify;

    private void checkTextToClassify() throws Exception {
        String noTextException = "No text to classify!";
        if(textToClassify == null)
            throw new Exception(noTextException);
    }

    /**
     * Domyślny konstruktor. Jeśli którykolwiek z parametrów jest null, rzuca wyjątek.
     * @param textToClassify tekst do sklasyfikowania w formie prostego Stringa.
     * @throws Exception Jeśli którykolwiek z podanych parametrów jest null.
     */
    public Request(String textToClassify) throws Exception {
        this.textToClassify = textToClassify;
        checkTextToClassify();
    }

    /**
     * Getter dla tekstu do sklasyfikowania.
     * @return Tekst do sklasyfikowania.
     */
    public String getTextToClassify() {
        return textToClassify;
    }

    /**
     * Setter dla tekstu do sklasyfikowania.
     * @param textToClassify Tekst do klasyfikacji. Jeśli null, rzuca wyjątek.
     */
    public void setTextToClassify(String textToClassify) throws Exception{
        this.textToClassify = textToClassify;
        checkTextToClassify();
    }


}

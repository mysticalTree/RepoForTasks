package Files.VigenereFactory;

public class AlphabetFactory {

    /**
     * Создает объект алфавита для указанного языка.
     *
     * @param language язык алфавита (например, "russian" или "english").
     * @return объект алфавита для выбранного языка.
     * @throws IllegalArgumentException если язык не поддерживается.
     */
    public static Alphabet createAlphabet(String language) {
        switch (language.toLowerCase()) {
            case "russian":
                return new RussianAlphabet(false);
            case "english":
                return new EnglishAlphabet();
            default:
                throw new IllegalArgumentException("Неподдерживаемый язык: " + language);
        }
    }

    /**
     * Создает объект русского алфавита с опцией включения буквы "Ё".
     *
     * @param includeYo флаг, указывающий, нужно ли включать букву "Ё" в
     * алфавит.
     * @return объект русского алфавита.
     */
    public static Alphabet createRussianAlphabet(boolean includeYo) {
        return new RussianAlphabet(includeYo);
    }
}

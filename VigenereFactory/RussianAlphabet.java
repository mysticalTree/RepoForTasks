package Files.VigenereFactory;

/**
 * Реализация алфавита для русского языка, с возможностью включения буквы "Ё".
 */
public class RussianAlphabet implements Alphabet {

    private final char[] alphabet;

    /**
     * Конструктор, создающий русский алфавит с опцией включения буквы "Ё".
     *
     * @param includeYo если true, буква "Ё" будет включена в алфавит.
     */
    public RussianAlphabet(boolean includeYo) {

        if (includeYo) {
            this.alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray();
        } else {
            this.alphabet = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray();
        }
    }

    @Override
    public char getBaseChar(char c) {
        return Character.isUpperCase(c) ? 'А' : 'а';
    }

    @Override
    public int getShift(char c) {
        char upperC = Character.toUpperCase(c);
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == upperC) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getAlphabetSize() {
        return alphabet.length;
    }

    @Override
    public char getCharAt(int index, boolean isUpperCase) {
        char c = alphabet[index % alphabet.length];
        return isUpperCase ? c : Character.toLowerCase(c);
    }
}

package Files.VigenereFactory;

/**
 * Реализация алфавита для английского языка.
 */
public class EnglishAlphabet implements Alphabet {

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public char getBaseChar(char c) {
        return Character.isUpperCase(c) ? 'A' : 'a';
    }

    @Override
    public int getShift(char c) {
        char upperC = Character.toUpperCase(c);
        for (int i = 0; i < ALPHABET.length; i++) {
            if (ALPHABET[i] == upperC) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getAlphabetSize() {
        return ALPHABET.length;
    }

    @Override
    public char getCharAt(int index, boolean isUpperCase) {
        char c = ALPHABET[index % ALPHABET.length];
        return isUpperCase ? c : Character.toLowerCase(c);
    }
}

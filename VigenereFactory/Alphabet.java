package Files.VigenereFactory;

/**
 * Интерфейс, представляющий общий алфавит для шифрования. Содержит методы для
 * работы с символами алфавита, их сдвигом и размером.
 */
public interface Alphabet {

    char getBaseChar(char c);

    int getShift(char c);

    int getAlphabetSize();

    char getCharAt(int index, boolean isUpperCase);

}

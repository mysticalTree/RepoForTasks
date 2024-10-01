package Files;

import static Files.FileUtils.*;
import Files.VigenereFactory.Alphabet;
import Files.VigenereFactory.AlphabetFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileMethods {

    public static void main(String[] args) throws IOException {
        //create("C://Direct//Books.txt");
//        read("C://Direct//notes.txt");
        //copy("C://Direct//notes.txt", "C://Direct//notes_copy.txt");
        //cloned("C://Direct//notes.txt", "C://Direct//notes_clone.txt");
//        searchSymbol("C://Direct//notes.txt", 'H');
//        write("C://Direct//notes.txt");
//        countWords("C://Direct//emails.txt");
        //Alphabet englishAlphabet = AlphabetFactory.createAlphabet("english");
        //encryptVigenere("C://Direct//en_vigenere.txt", "C://Direct//en_encrypted.txt", "keyword", englishAlphabet);
        //decryptVigenere("C://Direct//en_encrypted.txt", "C://Direct//en_decrypted.txt", "keyword", englishAlphabet);

        //Alphabet russianAlphabetWithYo = AlphabetFactory.createRussianAlphabet(true);
        //encryptVigenere("C://Direct//ru_vigenere.txt", "C://Direct//ru_encrypted.txt", "жижа", russianAlphabetWithYo);
        //decryptVigenere("C://Direct//ru_encrypted.txt", "C://Direct//ru_decrypted.txt", "жижа", russianAlphabetWithYo);
    }
}

class FileUtils {

    /**
     * Читает содержимое файла построчно и выводит его в консоль. Если файл не
     * найден или возникли ошибки при чтении, выводится соответствующее
     * сообщение.
     *
     * @param path путь к файлу для чтения
     */
    public static void read(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + path);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + path);
        }
    }

    /**
     * Создает новый файл по указанному пути. Если файл с таким именем уже
     * существует, то создается его резервная копия (с помощью перегруженного
     * метода cloned). Если возникли ошибки при создании файла, выводится
     * соответствующее сообщение.
     *
     * @param path путь к файлу, который нужно создать
     */
    public static void create(String path) {
        File file = new File(path);
        File backupFile = new File(file.getParent(), file.getName().replace(".txt", "_copy.txt"));

        if (file.exists()) {
            System.out.println("Файл " + file.getName() + " уже существует. Создание резервной копии...");
            cloned(file, backupFile);
        } else {
            try {
                System.out.println(file.createNewFile() ? "Файл " + file.getName() + " успешно создан" : "Не удалось создать файл " + file.getName());
            } catch (IOException ex) {
                System.out.println("Ошибка создания файла: " + ex.getMessage());
            }
        }
    }

    /**
     * Копирует файл из одного пути в другой с проверкой существования файлов и
     * дополнительной обработкой. Метод проверяет, существует ли исходный файл и
     * доступен ли для чтения. Также проверяется, можно ли записать файл в
     * указанное место назначения.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу, в который будет скопировано
     * содержимое исходного
     */
    public static void copy(String sourcePath, String destinationPath) {
        Path src = Paths.get(sourcePath);
        Path dst = Paths.get(destinationPath);

        if (Files.notExists(src)) {
            System.out.println("Ошибка: Исходный файл не существует.");
            return;
        }
        if (!Files.isReadable(src)) {
            System.out.println("Ошибка: Исходный файл недоступен для чтения.");
            return;
        }

        try (FileInputStream inStream = new FileInputStream(src.toFile()); FileOutputStream outStream = new FileOutputStream(dst.toFile())) {
            copyFileContents(inStream, outStream);
            System.out.println("Файл успешно скопирован: " + destinationPath);

        } catch (IOException ex) {
            System.out.println("Ошибка при копировании файла: " + ex.getMessage());
        }
    }

    /**
     * Клонирует содержимое исходного файла в новый файл и добавляет метку
     * времени в формате "yyyy-MM-dd HH:mm:ss". Если возникли ошибки при
     * клонировании файла, выводится соответствующее сообщение.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу, в который будет склонировано
     * содержимое исходного
     */
    public static void cloned(String sourcePath, String destinationPath) {
        try (FileInputStream inStream = new FileInputStream(sourcePath); FileOutputStream outStream = new FileOutputStream(destinationPath)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String header = "Клонирован: " + timestamp + System.lineSeparator();
            outStream.write(header.getBytes());

            copyFileContents(inStream, outStream);
            System.out.println("Файл успешно клонирован с добавлением метки времени.");
        } catch (IOException ex) {
            System.out.println("Ошибка при клонировании файла: " + ex.getMessage());
        }
    }

    /**
     * Перегруженный метод cloned, используемый в методе create для создания
     * резервной копии. Если возникли ошибки при создании резервной копии,
     * выводится соответствующее сообщение.
     *
     * @param sourceFile путь к исходному файлу
     * @param backupFile путь к резервному файлу, в который будет скопировано
     * содержимое исходного
     */
    public static void cloned(File sourceFile, File backupFile) {
        try (FileInputStream inStream = new FileInputStream(sourceFile); FileOutputStream outStream = new FileOutputStream(backupFile)) {
            copyFileContents(inStream, outStream);
            System.out.println("Создана резервная копия: " + backupFile.getName());
        } catch (IOException ex) {
            System.out.println("Ошибка при создании резервной копии: " + ex.getMessage());
        }
    }

    /**
     * Общий метод для копирования содержимого из входного потока в выходной.
     * Если возникли ошибки при работе метода, выводится соответствующее
     * сообщение.
     *
     * @param inStream входной поток файла
     * @param outStream выходной поток файла
     */
    private static void copyFileContents(FileInputStream inStream, FileOutputStream outStream) {
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при копировании содержимого файла: " + ex.getMessage());
        }
    }

    /**
     * Метод для поиска и подсчета количества вхождений заданного символа в
     * файле. Если возникли ошибки во время подсчета символов в файле, выводится
     * соответствующее сообщение.
     *
     * @param sourcePath путь к исходному файлу
     * @param searchChar символ для поиска
     */
    public static void searchSymbol(String sourcePath, char searchChar) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(sourcePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (c == searchChar) {
                        count++;
                    }
                }
            }
            System.out.println("Символ " + searchChar + " найден " + count + " раз(а)");
        } catch (FileNotFoundException ex) {
            System.out.println("Файл " + sourcePath + " не найден. ");
        } catch (IOException ex) {
            System.out.println("Ошибка при поиске символа в файле: " + ex.getMessage());
        }
    }

    /**
     * Запись в файл осуществляется из консоли, чтение происходит построчно.
     * Остановка записи происходит при вводе ключевого слова "ESC". Файл
     * перезаписывается при каждом вызове. Если возникли ошибки при записи в
     * файл, выводится соответствующее сообщение.
     *
     * @param sourcepath путь к исходному файлу для записи
     */
    public static void write(String sourcePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); BufferedWriter bw = new BufferedWriter(new FileWriter(sourcePath))) {
            String text;
            while (!(text = br.readLine()).equals("ESC")) {

                bw.write(text + "\n");
                bw.flush();
            }
        } catch (IOException ex) {

            System.out.println("Ошибка при записи в файл: " + ex.getMessage());
        }
    }

    /**
     * Считает слова в файле. Также проверяется, что слово содержит только
     * буквы, или слово только с одним знаком препинания в конце. Также
     * правильно подсчитывает слова через дефис, слэш, а также слова в скобках.
     * Если возникла ошибка при подсчете слов, выводится соответствующее
     * сообщение.
     *
     * @param sourcePath путь к файлу, слова в котором надо подсчитать
     */
    public static void countWords(String sourcePath) {

        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath))) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("[/\\-]");

                for (String part : parts) {

                    String[] words = part.split("\\s+");

                    for (String word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }

                        if (word.matches("[a-zA-Zа-яА-ЯёЁ()]+[.,!?;:]?")) {
                            wordCount++;
                        }
                    }
                }
            }

            System.out.println("Количество слов в файле: " + wordCount);

        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла: " + ex.getMessage());
        }
    }

    /**
     * Шифрует содержимое файла с использованием шифра Виженера и сохраняет
     * результат в указанный файл.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу для сохранения зашифрованного текста
     * @param key ключ шифрования
     * @param alphabet объект алфавита, используемый для шифрования
     */
    public static void encryptVigenere(String sourcePath, String destinationPath, String key, Alphabet alphabet) {
        processVigenere(sourcePath, destinationPath, key, alphabet, true);
    }

    /**
     * Расшифровывает содержимое файла с использованием шифра Виженера и
     * сохраняет результат в указанный файл.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу для сохранения расшифрованного текста
     * @param key ключ расшифровки
     * @param alphabet объект алфавита, используемый для расшифровки
     */
    public static void decryptVigenere(String sourcePath, String destinationPath, String key, Alphabet alphabet) {
        processVigenere(sourcePath, destinationPath, key, alphabet, false);
    }

    /**
     * Обрабатывает содержимое файла с использованием шифра Виженера и сохраняет
     * результат в указанный файл. Может быть использован для шифрования или
     * расшифровки в зависимости от флага isEncrypt. Если возникла ошибка при
     * обработке файла, выводится соответствующее сообщение.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу для сохранения результата
     * @param key ключ шифрования или расшифровки
     * @param alphabet объект алфавита, используемый для обработки текста
     * @param isEncrypt флаг, определяющий шифрование (true) или расшифровку
     * (false)
     */
    private static void processVigenere(String sourcePath, String destinationPath, String key, Alphabet alphabet, boolean isEncrypt) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath)); BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath))) {

            StringBuilder resultText = new StringBuilder();
            String line;
            int keyIndex = 0;

            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
                        boolean isUpperCase = Character.isUpperCase(c);
                        int charIndex = alphabet.getShift(c);

                        if (charIndex != -1) {
                            char keyChar = key.charAt(keyIndex % key.length());
                            int keyShift = alphabet.getShift(keyChar);

                            if (keyShift != -1) {
                                int shiftDirection = isEncrypt ? keyShift : -keyShift;
                                int newIndex = (charIndex + shiftDirection + alphabet.getAlphabetSize()) % alphabet.getAlphabetSize();
                                char newChar = alphabet.getCharAt(newIndex, isUpperCase);

                                resultText.append(newChar);
                                keyIndex++;
                            }
                        } else {
                            resultText.append(c);
                        }
                    } else {
                        resultText.append(c);
                    }
                }
                resultText.append(System.lineSeparator());
            }
            writer.write(resultText.toString());
            System.out.println((isEncrypt ? "Текст зашифрован" : "Текст расшифрован") + " и сохранен в " + destinationPath);

        } catch (IOException ex) {
            System.out.println("Ошибка при обработке файла: " + ex.getMessage());
        }
    }
}

package Files;

import static Files.FileUtils.*;
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
//        create("C://Direct//Books.txt");
//        read("C://Direct//notes.txt");
//        copy("C://Direct//notes.txt", "C://Direct//notes_copy.txt");
//        cloned("C://Direct//notes.txt", "C://Direct//notes_clone.txt");
//        searchSymbol("C://Direct//notes.txt", 'H');
//        write("C://Direct//notes.txt");
//        encryptVigenere("C://Direct//notes.txt", "C://Direct//notes_encrypted.txt", "keyword");
//        countWords("C://Direct//emails.txt");
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

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

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

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
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
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.out.println("Создана резервная копия: " + backupFile.getName());
        } catch (IOException ex) {
            System.out.println("Ошибка при создании резервной копии: " + ex.getMessage());
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
     * Шифрует содержимое файла с использованием шифра Виженера и сохраняет
     * текст в новый файл. Если возникли ошибки при шифровании, выводится
     * соответствующее сообщение.
     *
     * @param sourcePath путь к исходному файлу
     * @param destinationPath путь к файлу для сохранения зашифрованного текста
     * @param key ключ шифрования
     */
    public static void encryptVigenere(String sourcePath, String destinationPath, String key) {

        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath)); BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath))) {

            StringBuilder encryptedText = new StringBuilder();
            String line;
            int keyIndex = 0;

            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
                        char base = Character.isUpperCase(c) ? 'A' : 'a';
                        char keyChar = key.charAt(keyIndex % key.length());
                        int shift = Character.isUpperCase(keyChar) ? keyChar - 'A' : keyChar - 'a';
                        char encryptedChar = (char) ((c - base + shift) % 26 + base);
                        encryptedText.append(encryptedChar);

                        keyIndex++;
                    } else {
                        encryptedText.append(c);
                    }
                }
                encryptedText.append(System.lineSeparator());
            }
            writer.write(encryptedText.toString());
            System.out.println("Текст успешно зашифрован и сохранен в " + destinationPath);

        } catch (IOException ex) {
            System.out.println("Ошибка при шифровании содержимого файла: " + ex.getMessage());
        }
    }

    /**
     * Расшифровывает содержимое файла с использованием шифра Виженера и
     * сохраняет текст в новый файл. Если возникли ошибки при расшифровке,
     * выводится соответствующее сообщение.
     *
     * @param sourcePath Путь к исходному файлу
     * @param destinationPath Путь к файлу для сохранения расшифрованного текста
     * @param key Ключ расшифровки
     */
    public static void decryptVigenere(String sourcePath, String destinationPath, String key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath)); BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath))) {
            StringBuilder decryptedText = new StringBuilder();
            String line;
            int keyIndex = 0;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
                        char base = Character.isUpperCase(c) ? 'A' : 'a';
                        char keyChar = key.charAt(keyIndex % key.length());
                        int shift = Character.isUpperCase(keyChar) ? keyChar - 'A' : keyChar - 'a';
                        char decryptedChar = (char) ((c - base - shift + 26) % 26 + base);
                        decryptedText.append(decryptedChar);
                        keyIndex++;
                    } else {
                        decryptedText.append(c);
                    }
                }
                decryptedText.append(System.lineSeparator());
            }
            writer.write(decryptedText.toString());
            System.out.println("Текст успешно расшифрован и сохранен в " + destinationPath);
        } catch (IOException ex) {
            System.out.println("Ошибка при расшифровке содержимого файла: " + ex.getMessage());
        }
    }

    /**
     * Считает слова в файле. Также проверяется, что слово содержит только
     * буквы, или слово только с одним знаком препинания в конце. Также
     * подсчитывает слова, например: "квартира/дом" или "вот-вот". Если возникли
     * ошибки при подсчете слов, выводится соответствующее сообщение.
     *
     * @param sourcePath путь к файлу, слова в котором надо подсчитать
     */
    public static void countWords(String sourcePath) {

        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {

                    if (word.isEmpty()) {
                        continue;
                    }

                    if (word.matches("[a-zA-Zа-яА-Я]+([-/][a-zA-Zа-яА-Я]+)*[.,!?;:]?")) {
                        wordCount++;
                    }
                }
            }

            System.out.println("Количество слов в файле: " + wordCount);

        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла: " + ex.getMessage());
        }
    }
}
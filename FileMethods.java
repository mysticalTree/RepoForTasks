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
//        create("C://Direct//emails.txt");
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
     * Читает содержимое файла построчно и выводит его в консоль.
     *
     * @param path путь к файлу для чтения
     *
     * Метод открывает файл по указанному пути и считывает его построчно. Если
     * файл не найден или возникли ошибки при чтении, выводится соответствующее
     * сообщение. Закрытие потока происходит автоматически с помощью
     * try-with-resources.
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
     * Создает новый файл по указанному пути. Если файл уже существует, то
     * создается его резервная копия с таким же содержимым. Старый файл
     * удаляется перед созданием нового.
     *
     * @param path путь к файлу, который нужно создать
     *
     * Метод сначала проверяет, существует ли файл. Если файл существует, то
     * создается его резервная копия с суффиксом "_copy". Содержимое копируется
     * с помощью буферного чтения и записи. После создания резервной копии,
     * старый файл удаляется, а затем создается новый файл с исходным именем. В
     * случае возникновения ошибок (например, невозможности создать файл или
     * резервную копию) выводится соответствующее сообщение.
     */
    public static void create(String path) {
        File file = new File(path);
        File backupFile = new File(file.getParent(), file.getName().replace(".txt", "_copy.txt"));

        if (file.exists()) {
            System.out.println("Файл " + file.getName() + " уже существует. Создание резервной копии...");

            try (FileInputStream inStream = new FileInputStream(file); FileOutputStream outStream = new FileOutputStream(backupFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }
                System.out.println("Резервная копия успешно создана: " + backupFile.getName());
            } catch (IOException ex) {
                System.out.println("Ошибка при создании резервной копии: " + ex.getMessage());
            }
            System.out.println(file.delete() ? "Старый файл " + file.getName() + " удален." : "Не удалось удалить старый файл " + file.getName());
        }

        try {
            System.out.println(file.createNewFile() ? "Файл " + file.getName() + " успешно создан" : "Не удалось создать файл " + file.getName());
        } catch (IOException ex) {
            System.out.println("Ошибка создания файла: " + ex.getMessage());
        }
    }

    /**
     * Копирует файл из одного пути в другой с проверкой существования файлов и
     * дополнительной обработкой.
     *
     * @param sourcePath путь к исходному файлу для копирования
     * @param destinationPath путь, куда нужно скопировать файл
     *
     * Метод проверяет, существует ли исходный файл и доступен ли для чтения.
     * Также проверяется, можно ли записать файл в указанное место назначения.
     *
     * @throws IOException Если возникает ошибка при работе с файлами.
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
            System.out.println("Ошибка при копировании файла.");
        }
    }

    /**
     * Клонирует содержимое исходного файла в новый файл и добавляет метку
     * времени в начале содержимого.
     *
     * @param sourcePath путь к исходному файлу, который нужно клонировать
     * @param destinationPath путь к новому файлу, куда будет скопировано
     * содержимое
     *
     * Метод использует поток ввода-вывода для копирования содержимого исходного
     * файла. В начало нового файла добавляется строка с текущей датой и
     * временем клонирования в формате "yyyy-MM-dd HH:mm:ss".
     *
     * @throws IOException Если возникает ошибка ввода-вывода при работе с
     * файлами.
     */
    public static void cloned(String sourcePath, String destinationPath) {
        Path src = Paths.get(sourcePath);
        Path dst = Paths.get(destinationPath);

        try (FileInputStream inStream = new FileInputStream(src.toFile()); FileOutputStream outStream = new FileOutputStream(dst.toFile())) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String header = "Клонирован: " + timestamp + System.lineSeparator();
            outStream.write(header.getBytes());

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.out.println("Файл успешно клонирован с добавлением метки времени в содержимое");
        } catch (IOException ex) {
            System.out.println("Ошибка при клонировании файла");
        }
    }

    /**
     * Метод для поиска и подсчета количества вхождений заданного символа в
     * файле.
     *
     * @param sourcePath путь к исходному файлу.
     * @param searchChar символ для поиска.
     * @throws IOException Если возникает ошибка ввода-вывода при работе с
     * файлами.
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
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + sourcePath);
        } catch (IOException e) {
            System.out.println("Ошибка при поиске символа в файле: " + sourcePath);
        }
    }

    /**
     * Запись в файл осуществляется из консоли, чтение происходит построчно.
     * Остановка записи происходит при вводе ключевого слова "ESC".
     *
     * @param sourcepath Путь к исходному файлу, в который нужно записывать.
     * @throws IOException Если возникает ошибка ввода-вывода при работе с
     * файлами.
     */
    public static void write(String sourcepath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); BufferedWriter bw = new BufferedWriter(new FileWriter(sourcepath))) {
            String text;
            while (!(text = br.readLine()).equals("ESC")) {

                bw.write(text + "\n");
                bw.flush();
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    /**
     * Шифрует содержимое файла с использованием шифра Виженера и сохраняет
     * зашифрованный текст в новый файл. После этого расшифровывает
     * зашифрованный текст и записывает его в тот же файл на следующей строке.
     *
     * @param sourcePath Путь к исходному файлу, который нужно зашифровать.
     * @param destinationPath Путь к файлу, в который будет сохранен
     * зашифрованный и расшифрованный текст.
     * @param key Ключ шифрования для шифра Виженера.
     *
     * @throws IOException Если возникает ошибка ввода-вывода при работе с
     * файлами.
     */
    public static void encryptVigenere(String sourcePath, String destinationPath, String key) {
        Path src = Paths.get(sourcePath);
        Path dst = Paths.get(destinationPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(src.toFile())); FileOutputStream outStream = new FileOutputStream(dst.toFile())) {

            StringBuilder encryptedText = new StringBuilder();
            StringBuilder decryptedText = new StringBuilder();
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

                        char decryptedChar = (char) ((encryptedChar - base - shift + 26) % 26 + base);
                        decryptedText.append(decryptedChar);

                        keyIndex++;
                    } else {
                        encryptedText.append(c);
                        decryptedText.append(c);
                    }
                }
                encryptedText.append(System.lineSeparator());
                decryptedText.append(System.lineSeparator());
            }
            outStream.write(encryptedText.toString().getBytes());
            outStream.write(decryptedText.toString().getBytes());
            System.out.println("Файл успешно зашифрован и расшифрован с использованием шифра Виженера");

        } catch (IOException ex) {
            System.out.println("Ошибка при шифровании файла");
        }
    }

    /**
     * Считает слова в текстовом документе. С помощью word.matches проверяется,
     * что слово содержит только буквы, или слово только с одним знаком
     * препинания в конце.
     *
     * @param sourcePath Путь к исходному файлу, слова в котором надо
     * подсчитать.
     * @throws IOException Если возникает ошибка ввода-вывода при работе с
     * файлами.
     */
    public static void countWords(String sourcePath) {
        Path src = Paths.get(sourcePath);
        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(src.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {

                    if (word.isEmpty()) {
                        continue;
                    }

                    if (word.matches("[a-zA-Zа-яА-Я]+[.,!?;:]?")) {
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

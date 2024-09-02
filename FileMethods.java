package Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileMethods {

    public static void main(String[] args) throws IOException {
        FileUtils util = new FileUtils();
        //util.create("C://Direct//emails.txt", true);
        //util.read("C://Direct//notes.txt");
        //util.copy("C://Direct//notes.txt", "C://Direct//notes_copy.txt");
        //util.clone("C://Direct//notes.txt", "C://Direct//notes_clone.txt");
        //util.searchSymbol("C://Direct//notes.txt", 'H');
        //util.write("C://Direct//notes.txt");
        //util.encryptVigenere("C://Direct//notes.txt", "C://Direct//notes_encrypted.txt", "keyword");
        //util.countWords("C://Direct//emails.txt");
    }
}

class FileUtils {

    public static void read(String path) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + path);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + path);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии файла: " + path);
            }
        }
    }

    public static void create(String path, boolean overwrite) {
        File file = new File(path);
        if (file.exists()) {
            if (overwrite) {
                file.delete();
                System.out.println("Существующий файл удален");
            } else {
                System.out.println("Создание резервной копии...");
            }
        }
        try {
            System.out.println(file.createNewFile() ? "Файл " + file.getName() + " создан" : "Файл " + file.getName() + " уже существует");
        } catch (IOException ex) {
            System.out.println("Ошибка создания файла");
        }
    }

    public static void copy(String sourcePath, String destinationPath) {
        Path src = Paths.get(sourcePath);
        Path dst = Paths.get(destinationPath);

        try {
            Files.copy(src, dst);
            System.out.println("Файл успешно скопирован");
        } catch (IOException ex) {
            System.out.println("Ошибка при копировании файла");
        }
    }

    public static void clone(String sourcePath, String destinationPath) {
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

    public static void searchSymbol(String sourcePath, char searchChar) {
        int count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(sourcePath));
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
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии файла: " + sourcePath);
            }
        }
    }

    public static void write(String sourcepath) {
        Path filePath = Paths.get(sourcepath);
        try {
            Files.write(filePath, "This text is good".getBytes());
            System.out.println("Текст записан в файл");
        } catch (IOException ex) {
            System.out.println("Ошибка при работе с файлом");
        }
    }

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

    public static void countWords(String sourcePath) {
        Path src = Paths.get(sourcePath);
        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(src.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                wordCount += words.length;
            }

            System.out.println("Количество слов в файле: " + wordCount);

        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
        }
    }
}

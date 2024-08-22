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

public class FileMethods {

    static String sourcepath;
    static String destinationPath;
    static char searchChar;

    public static void main(String[] args) throws IOException {
        createFile("C://Direct//NOTEd.txt");
        //readFile("C://Direct//notes.txt");
        //copyFile("C://Direct//notes.txt", "C://Direct//notes_copy.txt");
        //cloneFile("C://Direct//notes.txt", "C://Direct//notes_clone.txt");
        //searchSymbolInFile("C://Direct//notes.txt", 'H');
        //writeToFile("C://Direct//notes.txt");
    }

    public static void readFile(String path) throws IOException {
        FileMethods.sourcepath = path;
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void createFile(String path) throws IOException {
        FileMethods.sourcepath = path;
        File file = new File(path);
        System.out.println(file.createNewFile() ? "Файл " + file.getName() + " создан" : "Файл " + file.getName() + " уже существует");
    }

    public static void copyFile(String sourcePath, String destinationPath) {
        FileMethods.sourcepath = sourcePath;
        FileMethods.destinationPath = destinationPath;
        Path src = Paths.get(sourcePath);
        Path dst = Paths.get(destinationPath);

        try {
            Files.copy(src, dst);
            System.out.println("Файл успешно скопирован");
        } catch (IOException ex) {
            System.out.println("Ошибка при копировании файла");
        }
    }

    public static void cloneFile(String sourcePath, String destinationPath) throws FileNotFoundException {
        FileMethods.sourcepath = sourcePath;
        FileMethods.destinationPath = destinationPath;
        FileInputStream inStream = new FileInputStream(sourcePath);
        FileOutputStream outStream = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.out.println("Файл успешно клонирован");
        } catch (IOException ex) {
            System.out.println("Ошибка при клонировании файла");
        }
    }

    public static void searchSymbolInFile(String sourcePath, char searchChar) throws FileNotFoundException {
        FileMethods.searchChar = searchChar;
        int count = 0;
        BufferedReader br = new BufferedReader(new FileReader(sourcePath));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (c == searchChar) {
                        count++;
                    }
                }
            }
            System.out.println("Символ " + searchChar + " найден " + count + " раз(а)");
        } catch (IOException ex) {
            System.out.println("Ошибка при поиска символа в файле");
        }

    }

    public static void writeToFile(String sourcepath) {
        FileMethods.sourcepath = sourcepath;
        Path filePath = Paths.get(sourcepath);
        try {
            Files.write(filePath, "This text is good".getBytes());
            System.out.println("Текст записан в файл");
        } catch (IOException ex) {
            System.out.println("Ошибка работы с файлом");
        }
    }
}

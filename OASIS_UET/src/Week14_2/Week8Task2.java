import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Week8Task2 {
    // Throw NullPointerException
    public void nullPointerEx() throws NullPointerException {
        List<String> list = null;
        System.out.println(list.size());
    }

    // Catch NullPointerException
    public String nullPointerExTest() {
        try {
            nullPointerEx();
        } catch (NullPointerException e) {
            return "Lỗi Null Pointer";
        }
        return "No Error";
    }

    // Throw ArrayIndexOutOfBoundsException
    public void arrayIndexOutOfBoundsEx() throws ArrayIndexOutOfBoundsException {
        int[] arr = new int[5];
        int x = arr[5];
    }

    public String arrayIndexOutOfBoundsExTest() {
        try {
            arrayIndexOutOfBoundsEx();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Lỗi Array Index Out of Bounds";
        }
        return "No Error";
    }

    // Throw ArithmeticException
    public void arithmeticEx() throws ArithmeticException {
        int x = 5 / 0;
    }

    public String arithmeticExTest() {
        try {
            arithmeticEx();
        } catch (ArithmeticException e) {
            return "Lỗi Arithmetic";
        }
        return "No Error";
    }

    // Throw FileNotFoundException
    public void fileNotFoundEx() throws FileNotFoundException {
        String file = "file.txt";
        FileReader newFile = new FileReader(file); // This will throw FileNotFoundException if the file does not exist
    }

    public String fileNotFoundExTest() {
        try {
            fileNotFoundEx();
        } catch (FileNotFoundException e) {
            return "Lỗi File Not Found";
        }
        return "No Error";
    }

    // Throw IOException
    public void ioEx() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("nonexistentfile.txt"))) {
            String line = reader.readLine();
        }
    }

    public String ioExTest() {
        try {
            ioEx();
        } catch (IOException e) {
            return "Lỗi IO";
        }
        return "No Error";
    }
}
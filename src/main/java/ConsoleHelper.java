import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readString() {
        String result = null;
        try {
            result = reader.readLine();
        } catch (IOException e) {
            System.out.println("Что-то пошло не так, повторите ввод строки:");
            result = readString();
        }
        return result;
    }

    public static int readInt() {
        int result = 0;
        try {
            result = Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            System.out.println("В введенной Вами строке есть символы, не являющиеся цифрами. Повторите ввод числа заново:");
            result = readInt();
        }
        return result;
    }

    public static void sayGoodbye() {
        writeMessage("До скорых встреч!");
    }

    public static void writeMessage(String message) {
        System.out.println(message);
    }
}

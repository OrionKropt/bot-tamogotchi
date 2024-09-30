package orionkropt;

import java.io.*;
import java.util.Scanner;

public class Token {
    public static String readToken() {
        try {
            Scanner scanner = new Scanner(new File("../token.txt"));
            String name = scanner.nextLine();
            scanner.close();
            return name;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

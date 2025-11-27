package lld.snakesandladders;

import java.util.Scanner;

public class UserInputService {

    private final static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    public static int takeIntInputWithRetries(String message, int maxRetries) {

        for (int i = 0; i < maxRetries; i++) {
            System.out.println(message);

            String inputStr = sc.next();
            try {
                return Integer.parseInt(inputStr);
            } catch (Exception ex) {
                System.out.println("invalid input, please enter again");
            }
        }
        throw new RuntimeException("max retries attempted");
    }

    public static String takeStringInput(String message) {

        System.out.println(message);
        return sc.next();
    }
}

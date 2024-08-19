package main.task1;

import java.util.Scanner;

//question 1, b
public class secretDecoder {
    private static char rotateChar(char c, int direction) {
        if (Character.isLowerCase(c)) {
            if (direction == 1) {
                return c == 'z' ? 'a' : (char)(c + 1);
            } else {
                return c == 'a' ? 'z' : (char)(c - 1);
            }
        } else if (Character.isUpperCase(c)) {
            if (direction == 1) {
                return c == 'Z' ? 'A' : (char)(c + 1);
            } else {
                return c == 'A' ? 'Z' : (char)(c - 1);
            }
        } else {
            return c;
        }
    }

    private static String applyShifts(String s, int[][] shifts) {
        char[] message = s.toCharArray();

        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];

            for (int i = start; i <= end; i++) {
                message[i] = rotateChar(message[i], direction);
            }
        }

        return new String(message);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the message: ");
        String s = scanner.nextLine();
        System.out.print("Enter the shifts in the format [[start, end, direction], ...]: ");
        String shiftsInput = scanner.nextLine();

        int[][] shifts = parseShifts(shiftsInput);
        if (shifts == null) {
            System.out.println("Error parsing shifts input. Please enter shifts in the correct format.");
        } else {
            String result = applyShifts(s, shifts);
            System.out.println("Deciphered message: " + result);
        }

        scanner.close();
    }
    private static int[][] parseShifts(String shiftsInput) {
        try {
            shiftsInput = shiftsInput.trim().replaceAll("\\s+", "");
            if (shiftsInput.startsWith("[[") && shiftsInput.endsWith("]]")) {
                shiftsInput = shiftsInput.substring(2, shiftsInput.length() - 2);
            }
            String[] shiftStrings = shiftsInput.split("\\],\\[");
            int[][] shifts = new int[shiftStrings.length][3];
            for (int i = 0; i < shiftStrings.length; i++) {
                String[] parts = shiftStrings[i].split(",");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid shift format.");
                }
                shifts[i][0] = Integer.parseInt(parts[0]);
                shifts[i][1] = Integer.parseInt(parts[1]);
                shifts[i][2] = Integer.parseInt(parts[2]);
            }

            return shifts;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
   
}

package com.wallet.CLI;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** M1: Scanner wrapper. Basic format checks only; real business rules live in the service layer. */
public class InputHelper {
    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (!s.isEmpty()) return s;
            System.out.println("  Input cannot be empty. Try again.");
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String s = readLine(prompt);
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("  Enter a number between " + min + " and " + max + ".");
        }
    }

    public double readAmount(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try {
                double v = Double.parseDouble(s);
                if (v > 0) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("  Enter a valid amount greater than 0.");
        }
    }

    public String readPhone(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (s.matches("\\d{10}")) return s;
            System.out.println("  Phone number must be exactly 10 digits.");
        }
    }

    public String readPin(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (s.matches("\\d{4}")) return s;
            System.out.println("  PIN must be exactly 4 digits.");
        }
    }

    public String readEmail(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (s.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return s;
            System.out.println("  Enter a valid email address.");
        }
    }

    /** Returns null if the user just presses Enter (no filter). Format: yyyy-MM-dd */
    public LocalDate readDateOrBlank(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (s.isEmpty()) return null;
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("  Use format yyyy-MM-dd (e.g. 2026-10-30) or press Enter to skip.");
            }
        }
    }

    /** Returns null if blank. */
    public String readOptional(String prompt) {
        String s = readLine(prompt);
        return s.isEmpty() ? null : s;
    }
}

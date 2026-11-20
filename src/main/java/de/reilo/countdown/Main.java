package de.reilo.countdown;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CountdownService service = new CountdownService();

        if (args.length >= 2) {
            runNonInteractive(args, service);
        } else if (args.length == 1) {
            System.err.println("Usage: java -jar demo-java-app-1.0.0.jar <goal> <deadline:YYYY-MM-DD> [startDate:YYYY-MM-DD]");
            System.exit(1);
        } else {
            runInteractive(service);
        }
    }

    private static void runNonInteractive(String[] args, CountdownService service) {
        try {
            String goal = args[0];
            LocalDate deadline = service.parseDate(args[1], "Deadline");
            LocalDate startDate = args.length >= 3
                    ? service.parseDate(args[2], "Start date")
                    : LocalDate.now();

            CountdownResult result = service.calculateRemainingTime(goal, deadline, startDate, LocalDate.now());
            System.out.println(result.formatMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runInteractive(CountdownService service) {
        try (Scanner scanner = new Scanner(System.in)) {
            String goal = promptNonBlank(scanner, "Enter your goal: ");
            LocalDate deadline = promptDate(scanner, service, "Enter the deadline (YYYY-MM-DD): ", false);
            LocalDate startDate = promptDate(scanner, service, "Enter start date (YYYY-MM-DD, press Enter for today): ", true);

            LocalDate today = LocalDate.now();
            CountdownResult result = service.calculateRemainingTime(
                    goal,
                    deadline,
                    startDate != null ? startDate : today,
                    today
            );

            System.out.println("\n" + result.formatMessage());
        }
    }

    private static String promptNonBlank(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.isBlank()) {
                return input.trim();
            }
            System.out.println("Input must not be empty. Please try again.");
        }
    }

    private static LocalDate promptDate(Scanner scanner, CountdownService service, String prompt, boolean optional) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (optional && (input == null || input.isBlank())) {
                return null;
            }
            try {
                return service.parseDate(input, "Date");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Please try again.");
            }
        }
    }
}

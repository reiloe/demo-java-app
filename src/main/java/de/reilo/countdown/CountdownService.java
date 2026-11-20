package de.reilo.countdown;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CountdownService {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public LocalDate parseDate(String dateInput, String fieldName) {
        if (dateInput == null || dateInput.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
        try {
            return LocalDate.parse(dateInput.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    fieldName + " has an invalid format. Please use YYYY-MM-DD (e.g. 2027-12-31).", e
            );
        }
    }

    public CountdownResult calculateRemainingTime(
            String goal,
            LocalDate deadline,
            LocalDate startDate,
            LocalDate referenceDate
    ) {
        if (goal == null || goal.isBlank()) {
            throw new IllegalArgumentException("Goal must not be empty.");
        }
        if (deadline == null || startDate == null || referenceDate == null) {
            throw new IllegalArgumentException("Dates must not be null.");
        }

        String cleanGoal = goal.trim();
        long remainingDays = ChronoUnit.DAYS.between(referenceDate, deadline);

        if (remainingDays < 0) {
            return new CountdownResult.ExpiredCountdown(cleanGoal, Math.abs(remainingDays));
        }
        if (remainingDays == 0) {
            return new CountdownResult.TodayDeadline(cleanGoal);
        }

        Period period = Period.between(referenceDate, deadline);
        int progress = calculateProgressPercent(startDate, deadline, referenceDate);

        return new CountdownResult.ActiveCountdown(
                cleanGoal,
                period.getYears(),
                period.getMonths(),
                period.getDays(),
                remainingDays,
                progress
        );
    }

    private int calculateProgressPercent(LocalDate startDate, LocalDate deadline, LocalDate referenceDate) {
        long totalSpan = ChronoUnit.DAYS.between(startDate, deadline);
        if (totalSpan <= 0 || !referenceDate.isAfter(startDate)) {
            return 0;
        }
        long elapsed = ChronoUnit.DAYS.between(startDate, referenceDate);
        return (int) Math.min(100, Math.round((elapsed * 100.0) / totalSpan));
    }
}

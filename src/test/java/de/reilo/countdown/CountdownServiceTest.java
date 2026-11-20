package de.reilo.countdown;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CountdownServiceTest {

    private CountdownService service;

    @BeforeEach
    void setUp() {
        service = new CountdownService();
    }

    @Test
    void parseDate_validDate_returnsLocalDate() {
        LocalDate parsed = service.parseDate("2027-05-15", "Deadline");
        assertEquals(LocalDate.of(2027, 5, 15), parsed);
    }

    @Test
    void parseDate_invalidFormat_throwsExceptionWithFieldName() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.parseDate("15.05.2027", "Deadline")
        );
        assertTrue(ex.getMessage().contains("Deadline"));
    }

    @Test
    void parseDate_blankInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.parseDate("   ", "Start date"));
    }

    @Test
    void calculateRemainingTime_activeCountdownWithProgress_returnsActiveRecordAndBar() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate today = LocalDate.of(2026, 7, 3); // exakt 50% von 365 Tagen
        LocalDate deadline = LocalDate.of(2027, 1, 1);

        CountdownResult result = service.calculateRemainingTime("Master Java 21", deadline, start, today);

        assertInstanceOf(CountdownResult.ActiveCountdown.class, result);
        CountdownResult.ActiveCountdown active = (CountdownResult.ActiveCountdown) result;
        assertEquals("Master Java 21", active.goal());
        assertEquals(50, active.progressPercent());
        assertTrue(result.formatMessage().contains("[██████████░░░░░░░░░░] 50% elapsed"));
    }

    @Test
    void calculateRemainingTime_startDateInFuture_clampsProgressToZero() {
        LocalDate today = LocalDate.of(2026, 11, 20);
        LocalDate start = LocalDate.of(2026, 12, 1);
        LocalDate deadline = LocalDate.of(2027, 1, 1);

        CountdownResult result = service.calculateRemainingTime("Future Start", deadline, start, today);

        CountdownResult.ActiveCountdown active = assertInstanceOf(CountdownResult.ActiveCountdown.class, result);
        assertEquals(0, active.progressPercent());
    }

    @Test
    void calculateRemainingTime_todayDeadline_returnsTodayRecord() {
        LocalDate today = LocalDate.of(2026, 11, 20);

        CountdownResult result = service.calculateRemainingTime("Release Day", today, today.minusDays(10), today);

        assertInstanceOf(CountdownResult.TodayDeadline.class, result);
        assertEquals("Today is the deadline for 'Release Day'! [████████████████████] 100%", result.formatMessage());
    }

    @Test
    void calculateRemainingTime_pastDeadline_returnsExpiredRecord() {
        LocalDate today = LocalDate.of(2026, 11, 20);
        LocalDate deadline = LocalDate.of(2026, 11, 10);

        CountdownResult result = service.calculateRemainingTime("Old Goal", deadline, deadline.minusDays(20), today);

        CountdownResult.ExpiredCountdown expired = assertInstanceOf(CountdownResult.ExpiredCountdown.class, result);
        assertEquals(10, expired.daysOverdue());
        assertEquals("The deadline for 'Old Goal' passed 10 day(s) ago.", result.formatMessage());
    }

    @Test
    void calculateRemainingTime_emptyGoal_throwsException() {
        LocalDate today = LocalDate.of(2026, 11, 20);
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateRemainingTime("", today.plusDays(5), today, today));
    }

    @Test
    void parseDate_nullInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.parseDate(null, "Deadline"));
    }

    @Test
    void calculateRemainingTime_nullArguments_throwsException() {
        LocalDate today = LocalDate.of(2026, 11, 20);
        assertThrows(IllegalArgumentException.class, () -> service.calculateRemainingTime(null, today, today, today));
        assertThrows(IllegalArgumentException.class, () -> service.calculateRemainingTime("Goal", null, today, today));
        assertThrows(IllegalArgumentException.class, () -> service.calculateRemainingTime("Goal", today, null, today));
        assertThrows(IllegalArgumentException.class, () -> service.calculateRemainingTime("Goal", today, today, null));
    }
}

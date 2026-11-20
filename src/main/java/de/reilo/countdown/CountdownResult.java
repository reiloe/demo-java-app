package de.reilo.countdown;

public sealed interface CountdownResult permits
        CountdownResult.ActiveCountdown,
        CountdownResult.TodayDeadline,
        CountdownResult.ExpiredCountdown {

    String goal();

    record ActiveCountdown(
            String goal,
            long years,
            long months,
            long days,
            long totalDays,
            int progressPercent
    ) implements CountdownResult {}

    record TodayDeadline(String goal) implements CountdownResult {}

    record ExpiredCountdown(String goal, long daysOverdue) implements CountdownResult {}

    default String formatMessage() {
        return switch (this) {
            case ActiveCountdown a -> String.format(
                    "Remaining time until '%s': %d year(s), %d month(s), %d day(s) (%d total days).\nProgress: %s %d%% elapsed",
                    a.goal(), a.years(), a.months(), a.days(), a.totalDays(),
                    renderProgressBar(a.progressPercent(), 20), a.progressPercent()
            );
            case TodayDeadline t -> String.format(
                    "Today is the deadline for '%s'! %s 100%%",
                    t.goal(), renderProgressBar(100, 20)
            );
            case ExpiredCountdown e -> String.format(
                    "The deadline for '%s' passed %d day(s) ago.",
                    e.goal(), e.daysOverdue()
            );
        };
    }

    private static String renderProgressBar(int percent, int width) {
        int clamped = Math.clamp(percent, 0, 100);
        int filled = (int) Math.round((clamped / 100.0) * width);
        return "[" + "█".repeat(filled) + "░".repeat(width - filled) + "]";
    }
}

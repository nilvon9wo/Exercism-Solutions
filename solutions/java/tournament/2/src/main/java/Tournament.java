import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Tournament {
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");
    private final Leaderboard LEADERBOARD = new Leaderboard();

    void applyResults(String resultString) {
        if (resultString == null || resultString.isBlank()) {
            return;
        }

        Arrays.stream(NEW_LINE_PATTERN.split(resultString))
              .forEach(this::processMatch);
    }

    String printTable() {
        final StatColumn[] statColumns = StatColumn.values();
        String header = formatHeader(statColumns);
        String body = LEADERBOARD.renderSortedLeaderboard(statColumns);
        return body.isEmpty()
               ? header
               : header + body + "\n";
    }

    private String formatHeader(StatColumn[] statColumns) {
        return Arrays.stream(statColumns)
                     .map(StatColumn::formatHeaderCell)
                     .collect(Collectors.joining(" | "))
               + "\n";
    }

    private void processMatch(String line) {
        String[] parts = line.split(";");
        TeamRecord home = LEADERBOARD.getOrCreate(parts[0]);
        TeamRecord away = LEADERBOARD.getOrCreate(parts[1]);

        switch (parts[2]) {
            case "win" -> home.applyWinAgainst(away);
            case "loss" -> home.applyLossAgainst(away);
            case "draw" -> home.applyDrawAgainst(away);
        }
    }
}
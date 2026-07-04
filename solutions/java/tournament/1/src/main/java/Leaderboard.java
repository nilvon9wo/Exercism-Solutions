import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Leaderboard {
    private final Map<String, TeamRecord> teamRecordsByName = new HashMap<>();

    TeamRecord getOrCreate(String teamName) {
        return teamRecordsByName.computeIfAbsent(teamName, TeamRecord::new);
    }

    public String renderSortedLeaderboard(StatColumn[] statColumns) {
        return teamRecordsByName.values().stream()
                                .sorted(this::compareTeams)
                                .map(record -> this.formatRow(record, statColumns))
                                .collect(Collectors.joining("\n"));
    }

    private int compareTeams(TeamRecord a, TeamRecord b) {
        int pointsCompare = Integer.compare(b.points, a.points);
        return pointsCompare != 0
               ? pointsCompare
               : a.name.compareTo(b.name);
    }

    private String formatRow(TeamRecord record, StatColumn[] statColumns) {
        return Arrays.stream(statColumns)
                     .map(col -> col.formatRowCell(record))
                     .collect(Collectors.joining(" | "));
    }
}

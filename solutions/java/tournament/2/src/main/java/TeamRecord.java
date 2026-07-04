class TeamRecord {

    TeamRecord(String name) {
        this.name = name;
    }

    @StatField(StatColumn.TEAM)
    final String name;

    @StatField(StatColumn.MATCHES_PLAYED)
    int played;

    @StatField(StatColumn.WINS)
    int wins;

    @StatField(StatColumn.DRAWS)
    int draws;

    @StatField(StatColumn.LOSSES)
    int losses;

    @StatField(StatColumn.POINTS)
    int points;

    void applyWinAgainst(TeamRecord opponent) {
        this.played++;
        opponent.played++;

        this.wins++;
        this.points += 3;

        opponent.losses++;
    }

    void applyLossAgainst(TeamRecord opponent) {
        opponent.applyWinAgainst(this);
    }

    void applyDrawAgainst(TeamRecord opponent) {
        this.played++;
        opponent.played++;

        this.draws++;
        opponent.draws++;

        this.points++;
        opponent.points++;
    }
}

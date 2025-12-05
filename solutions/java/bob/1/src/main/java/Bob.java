class Bob {
    String hey(String input) {
        StatementProperties properties = new StatementProperties(input);
        return properties.isSilent()
                       ? "Fine. Be that way!"
                       : properties.isForcefulQuestion()
                                 ? "Calm down, I know what I'm doing!"
                                 : properties.isShouting()
                                           ? "Whoa, chill out!"
                                           : properties.isQuestion()
                                                     ? "Sure."
                                                     : "Whatever.";
    }
}

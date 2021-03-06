package com.epam.jwd_final.web.command;

public enum Parameter {

    ERROR("error"),
    SUCCESS("success"),
    MATCHES("matches"),
    TEAMS("teams"),
    EVENTS("events"),
    USERS("users"),
    BET_ID("betId"),
    MATCH_ID("matchId"),
    ACTIVE_BETS("placedBets"),
    PREVIOUS_BETS("previousPlacedBets"),
    FIRST_TEAM("firstTeam"),
    SECOND_TEAM("secondTeam"),
    START_TIME("startTime"),
    FIRST_TEAM_COEFFICIENT("firstTeamCoefficient"),
    SECOND_TEAM_COEFFICIENT("secondTeamCoefficient"),
    DRAW_COEFFICIENT("drawCoefficient"),
    DEPOSIT_MONEY("depositMoney"),
    WITHDRAW_MONEY("withdrawMoney"),
    BET_MONEY("betMoney"),
    RESULT("result"),
    RESULT_TYPE("resultType"),
    USER_ID("userId"),
    USER_NAME("userName"),
    USER_ROLE("userRole"),
    USER_PASSWORD("userPassword"),
    USER_BALANCE("userBalance");

    private final String value;

    Parameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

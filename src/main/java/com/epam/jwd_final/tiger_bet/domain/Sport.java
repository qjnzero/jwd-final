package com.epam.jwd_final.tiger_bet.domain;

public enum Sport implements Entity {

    FOOTBALL(1);

    private int id;

    Sport(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Sport resolveSportById(int id) {
        Sport sport;
        switch (id) {
            case 1:
                sport = Sport.FOOTBALL;
                break;
            default:
                // TODO log "no such sport"
                throw new IllegalArgumentException();
        }
        return sport;
    }
}
package com.epam.jwd_final.web.command.bookmaker;

import com.epam.jwd_final.web.command.Command;
import com.epam.jwd_final.web.command.Parameter;
import com.epam.jwd_final.web.command.RequestContext;
import com.epam.jwd_final.web.command.ResponseContext;
import com.epam.jwd_final.web.command.page.ShowBookmakerPage;
import com.epam.jwd_final.web.domain.Result;
import com.epam.jwd_final.web.exception.CommandException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.impl.EventServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum CreateEventCommand implements Command {

    INSTANCE;

    private static final String SUCCESS_MSG = "The event was created";
    private static final String ERROR_MSG = "Two teams cannot be the same";

    private final EventServiceImpl eventService;

    CreateEventCommand() {
        this.eventService = EventServiceImpl.INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext req) throws CommandException {
        try {
            final String firstTeam = req.getStringParameter(Parameter.FIRST_TEAM.getValue());
            final String secondTeam = req.getStringParameter(Parameter.SECOND_TEAM.getValue());
            final LocalDateTime start = LocalDateTime.parse(req.getStringParameter(Parameter.START_TIME.getValue()));

            EnumMap<Result, BigDecimal> coefficients = createCoefficientsMap(
                    new BigDecimal(req.getStringParameter(Parameter.FIRST_TEAM_COEFFICIENT.getValue())),
                    new BigDecimal(req.getStringParameter(Parameter.SECOND_TEAM_COEFFICIENT.getValue())),
                    new BigDecimal(req.getStringParameter(Parameter.DRAW_COEFFICIENT.getValue()))
            );

            if (eventService.createEvent(start, firstTeam, secondTeam, coefficients)) {
                req.setAttribute(Parameter.SUCCESS.getValue(), SUCCESS_MSG);
            } else {
                req.setAttribute(Parameter.ERROR.getValue(), ERROR_MSG);
            }
            return ShowBookmakerPage.INSTANCE.execute(req);
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }

    EnumMap<Result, BigDecimal> createCoefficientsMap(BigDecimal firstTeamCoefficient,
                                                  BigDecimal secondTeamCoefficient,
                                                  BigDecimal drawCoefficient) {
        EnumMap<Result, BigDecimal> resultBigDecimalEnumMap = new EnumMap<>(Result.class);

        resultBigDecimalEnumMap.put(Result.FIRST_TEAM, firstTeamCoefficient);
        resultBigDecimalEnumMap.put(Result.SECOND_TEAM, secondTeamCoefficient);
        resultBigDecimalEnumMap.put(Result.DRAW, drawCoefficient);

        return resultBigDecimalEnumMap;
    }
}

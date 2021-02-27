package com.epam.jwd_final.web.command.page;

import com.epam.jwd_final.web.command.Command;
import com.epam.jwd_final.web.command.ResponseContextResult;
import com.epam.jwd_final.web.command.Page;
import com.epam.jwd_final.web.command.Parameter;
import com.epam.jwd_final.web.command.RequestContext;
import com.epam.jwd_final.web.command.ResponseContext;
import com.epam.jwd_final.web.domain.EventDto;
import com.epam.jwd_final.web.domain.MatchDto;
import com.epam.jwd_final.web.domain.Result;
import com.epam.jwd_final.web.exception.CommandException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.MatchService;
import com.epam.jwd_final.web.service.MultiplierService;
import com.epam.jwd_final.web.service.UserService;
import com.epam.jwd_final.web.service.impl.MatchServiceImpl;
import com.epam.jwd_final.web.service.impl.MultiplierServiceImpl;
import com.epam.jwd_final.web.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum ShowMatchesPage implements Command {

    INSTANCE;

    private final MatchService matchService;
    private final MultiplierService multiplierService;

    ShowMatchesPage() {
        this.matchService = MatchServiceImpl.INSTANCE;
        this.multiplierService = MultiplierServiceImpl.INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext req) throws CommandException {
        try {
            final List<MatchDto> matchDtos = matchService.findAllByStartOfDateByResult(LocalDate.now(), Result.NO_RESULT).orElse(Collections.emptyList());
            List<EventDto> eventDtos = new ArrayList<>();
            for (MatchDto matchDto : matchDtos) {
                final Map<Result, BigDecimal> coefficients = multiplierService.findCoefficientsByMatchId(matchDto.getId());
                eventDtos.add(new EventDto(
                        matchDto, coefficients.get(Result.FIRST_TEAM),
                        coefficients.get(Result.SECOND_TEAM), coefficients.get(Result.DRAW))
                );
            }
            req.setAttribute(Parameter.EVENTS.getValue(), eventDtos);
            return ResponseContextResult.forward(Page.MATCHES.getLink());
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }
}

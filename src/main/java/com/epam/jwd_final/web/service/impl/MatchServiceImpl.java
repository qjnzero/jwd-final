package com.epam.jwd_final.web.service.impl;

import com.epam.jwd_final.web.dao.MatchDao;
import com.epam.jwd_final.web.dao.impl.MatchDaoImpl;
import com.epam.jwd_final.web.domain.Match;
import com.epam.jwd_final.web.domain.Result;
import com.epam.jwd_final.web.domain.dto.MatchDto;
import com.epam.jwd_final.web.exception.DaoException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.MatchService;
import com.epam.jwd_final.web.service.TeamService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * MatchServiceImpl.
 *
 * @author Ostapchuk Kirill
 */
public enum MatchServiceImpl implements MatchService {

    INSTANCE;

    private final MatchDao matchDao;
    private final TeamService teamService;

    MatchServiceImpl() {
        this.matchDao = MatchDaoImpl.INSTANCE;
        this.teamService = TeamServiceImpl.INSTANCE;
    }

    @Override
    public List<MatchDto> findAllUnfinishedByDateBetween(LocalDateTime from, LocalDateTime to) throws ServiceException {
        try {
            Timestamp yesterdayStart = Timestamp.valueOf(from.toLocalDate().atStartOfDay());
            Timestamp tomorrowEnd = Timestamp.valueOf(to.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1));
            return matchDao.findAllUnfinishedByDateBetween(yesterdayStart, tomorrowEnd)
                    .stream()
                    .map(this::convertToDto)
                    .collect(toList());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Match createMatch(LocalDateTime start, String firstTeam, String secondTeam) {
        return new Match(start, firstTeam, secondTeam);
    }

    @Override
    public int findIdByStartByFirstTeamBySecondTeam(LocalDateTime start, String firstTeam, String secondTeam)
            throws ServiceException {
        try {
            return matchDao.findOneByStartByFirstTeamIdBySecondTeamId(
                    Timestamp.valueOf(start),
                    teamService.findIdByName(firstTeam),
                    teamService.findIdByName(secondTeam)
            ).orElseThrow(ServiceException::new).getId();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void updateResult(int matchId, Result result) throws ServiceException {
        try {
            matchDao.updateResultId(matchId, result.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Result findResultById(int id) throws ServiceException {
        try {
            return matchDao.findOneById(id)
                    .orElseThrow(ServiceException::new)
                    .getResultType();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Match findById(int id) throws ServiceException {
        try {
            return matchDao.findOneById(id).orElseThrow(ServiceException::new);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        try {
            matchDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void save(Match match) throws ServiceException {
        try {
            matchDao.save(
                    match.getStart(),
                    teamService.findIdByName(match.getFirstTeam()),
                    teamService.findIdByName(match.getSecondTeam())
            );
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    private MatchDto convertToDto(Match match) {
        return new MatchDto(
                match.getId(),
                match.getStart(),
                match.getFirstTeam(),
                match.getSecondTeam()
        );
    }
}

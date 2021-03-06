package com.epam.jwd_final.web.service.impl;

import com.epam.jwd_final.web.dao.MultiplierDao;
import com.epam.jwd_final.web.dao.impl.MultiplierDaoImpl;
import com.epam.jwd_final.web.domain.Multiplier;
import com.epam.jwd_final.web.domain.Result;
import com.epam.jwd_final.web.exception.DaoException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.MultiplierService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * MultiplierServiceImpl.
 *
 * @author Ostapchuk Kirill
 */
public enum MultiplierServiceImpl implements MultiplierService {

    INSTANCE;

    private final MultiplierDao multiplierDao;

    MultiplierServiceImpl() {
        this.multiplierDao = MultiplierDaoImpl.INSTANCE;
    }

    @Override
    public Multiplier createMultiplier(int matchId, Result result, BigDecimal coefficient) {
        return new Multiplier(matchId, result, coefficient);
    }

    @Override
    public void saveMultiplier(Multiplier multiplier) throws ServiceException {
        try {
            multiplierDao.save(multiplier);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public BigDecimal findCoefficientByMatchIdByResult(int matchId, Result result) throws ServiceException {
        try {
            return multiplierDao.findOneByMatchIdByResultId(matchId, result.getId())
                    .orElseThrow(ServiceException::new)
                    .getCoefficient();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int findIdByMatchIdByResult(int matchId, Result result) throws ServiceException {
        try {
            return multiplierDao.findOneByMatchIdByResultId(matchId, result.getId())
                    .orElseThrow(ServiceException::new)
                    .getId();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        try {
            multiplierDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int findMatchIdByMultiplierId(int multiplierId) throws ServiceException {
        try {
            return multiplierDao.findOneById(multiplierId)
                    .orElseThrow(ServiceException::new)
                    .getMatchId();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public BigDecimal findCoefficientById(int id) throws ServiceException {
        try {
            return multiplierDao.findOneById(id)
                    .orElseThrow(ServiceException::new)
                    .getCoefficient();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Result> findResultById(int id) throws ServiceException {
        try {
            return multiplierDao.findOneById(id).map(Multiplier::getResult);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Map<Result, BigDecimal> findCoefficientsByMatchId(int matchId) throws ServiceException {
        Map<Result, BigDecimal> coefficients = new HashMap<>();
        for (Result value : Result.values()) {
            coefficients.put(value, findCoefficientByMatchIdByResult(matchId, value));
        }
        return coefficients;
    }
}

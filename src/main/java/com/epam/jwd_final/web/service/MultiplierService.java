package com.epam.jwd_final.web.service;

import com.epam.jwd_final.web.domain.Multiplier;
import com.epam.jwd_final.web.domain.Result;
import com.epam.jwd_final.web.exception.ServiceException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * MultiplierService.
 *
 * @author Ostapchuk Kirill
 */
public interface MultiplierService {

    Multiplier createMultiplier(int matchId, Result result, BigDecimal coefficient);

    void saveMultiplier(Multiplier multiplier) throws ServiceException;

    BigDecimal findCoefficientByMatchIdByResult(int matchId, Result result) throws ServiceException;

    int findIdByMatchIdByResult(int matchId, Result result) throws ServiceException;

    void deleteById(int id) throws ServiceException;

    int findMatchIdByMultiplierId(int multiplierId) throws ServiceException;

    BigDecimal findCoefficientById(int id) throws ServiceException;

    Optional<Result> findResultById(int id) throws ServiceException;

    Map<Result, BigDecimal> findCoefficientsByMatchId(int matchId) throws ServiceException;
}

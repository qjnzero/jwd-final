package com.epam.jwd_final.web.command;

import com.epam.jwd_final.web.command.page.ShowBetsPage;
import com.epam.jwd_final.web.exception.CommandException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.BetService;
import com.epam.jwd_final.web.service.UserService;
import com.epam.jwd_final.web.service.impl.BetServiceImpl;
import com.epam.jwd_final.web.service.impl.UserServiceImpl;

import java.math.BigDecimal;

public enum CancelBetCommand implements Command {

    INSTANCE;

    private final BetService betService;
    private final UserService userService;

    CancelBetCommand() {
        this.betService = BetServiceImpl.INSTANCE;
        this.userService = UserServiceImpl.INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext req) throws CommandException {
        try {
            final int betId = Integer.parseInt(String.valueOf(req.getParameter("betId")));
            final String userName = String.valueOf(req.getSession().getAttribute("userName"));
            final BigDecimal betMoney = betService.findBetMoneyById(betId);
            userService.topUpBalance(userName, betMoney);
            betService.deleteById(betId);
            final int userId = userService.findUserIdByUserName(userName);
            final BigDecimal newBalance = userService.findBalanceById(userId);
//            req.setSessionAttribute("userBalance", userService.findBalanceById(userService.findUserIdByUserName("userName")));
            return ShowBetsPage.INSTANCE.execute(req);
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }
}

package com.epam.jwd_final.web.command.page;

import com.epam.jwd_final.web.command.Command;
import com.epam.jwd_final.web.command.Page;
import com.epam.jwd_final.web.command.RequestContext;
import com.epam.jwd_final.web.command.ResponseContext;
import com.epam.jwd_final.web.command.ResponseContextResult;
import com.epam.jwd_final.web.exception.CommandException;

public enum ShowWithdrawPage implements Command {

    INSTANCE;

    @Override
    public ResponseContext execute(RequestContext req) throws CommandException {
        return ResponseContextResult.forward(Page.WITHDRAW.getLink());
    }
}

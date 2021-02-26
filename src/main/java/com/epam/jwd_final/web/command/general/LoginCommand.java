package com.epam.jwd_final.web.command.general;

import com.epam.jwd_final.web.command.Command;
import com.epam.jwd_final.web.command.Parameter;
import com.epam.jwd_final.web.command.RequestContext;
import com.epam.jwd_final.web.command.ResponseContext;
import com.epam.jwd_final.web.command.page.ShowErrorPage;
import com.epam.jwd_final.web.command.page.ShowMatchesPage;
import com.epam.jwd_final.web.domain.UserDto;
import com.epam.jwd_final.web.exception.CommandException;
import com.epam.jwd_final.web.exception.ServiceException;
import com.epam.jwd_final.web.service.impl.UserServiceImpl;

import java.util.Optional;

public enum LoginCommand implements Command {

    INSTANCE;

    private static final String ERROR_MSG = "Invalid credentials";

    private final UserServiceImpl userService;

    LoginCommand() {
        this.userService = UserServiceImpl.INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext req) throws CommandException {
        try {
            final String name = req.getStringParameter(Parameter.USER_NAME.getValue());
            final String password = req.getStringParameter(Parameter.USER_PASSWORD.getValue());
            final Optional<UserDto> userDto = userService.login(name, password);
            if (userDto.isPresent()) {
                req.setSessionAttribute(Parameter.USER_NAME.getValue(), name);
                req.setSessionAttribute(Parameter.USER_ROLE.getValue(), userDto.get().getRole());
                req.setSessionAttribute(Parameter.USER_BALANCE.getValue(), userDto.get().getBalance());
            } else {
                req.setSessionAttribute(Parameter.ERROR.getValue(), ERROR_MSG);
                // TODO: or just validate with js and write "incorrect login or password"

                return ShowErrorPage.INSTANCE.execute(req);
            }
            return ShowMatchesPage.INSTANCE.execute(req);
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }
}

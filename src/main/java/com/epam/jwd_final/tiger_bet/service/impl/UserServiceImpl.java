package com.epam.jwd_final.tiger_bet.service.impl;

import com.epam.jwd_final.tiger_bet.dao.UserDao;
import com.epam.jwd_final.tiger_bet.domain.User;
import com.epam.jwd_final.tiger_bet.domain.UserDto;
import com.epam.jwd_final.tiger_bet.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private static final String DUMMY_PASSWORD = "defaultPwd";
    private static final String HASHED_DUMMY_PASSWORD = BCrypt.hashpw(DUMMY_PASSWORD, BCrypt.gensalt());

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<UserDto> login(String name, String password) {
        final Optional<User> user = userDao.findByName(name);
        if (!user.isPresent()) {
            BCrypt.checkpw(password, HASHED_DUMMY_PASSWORD);
            return Optional.empty();
        }
        final String realPassword = user.get().getPassword();
        if (BCrypt.checkpw(password, realPassword)) {
            return user.map(this::convertToDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean save(User user) {
        return userDao.save(user);
    }

    @Override
    public boolean signup(String name, String password) {
        return save(new User(
                name,
                BCrypt.hashpw(password, BCrypt.gensalt())
        ));
    }

    @Override
    public boolean updateRole(String userName) {
        final Optional<User> userOptional = userDao.findByName(userName);
        if (userOptional.isPresent()) {
            return userDao.updateRole(userOptional.get());
        }
        return false;
    }

    @Override
    public boolean rollbackRole(String userName) {
        final Optional<User> userOptional = userDao.findByName(userName);
        if (userOptional.isPresent()) {
            return userDao.rollbackRole(userOptional.get());
        }
        return false;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getName(), user.getRole().name());
    }
}
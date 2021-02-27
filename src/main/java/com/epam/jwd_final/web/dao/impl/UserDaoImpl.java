package com.epam.jwd_final.web.dao.impl;

import com.epam.jwd_final.web.dao.AbstractDao;
import com.epam.jwd_final.web.dao.UserDao;
import com.epam.jwd_final.web.domain.Role;
import com.epam.jwd_final.web.domain.User;
import com.epam.jwd_final.web.exception.DaoException;
import com.epam.jwd_final.web.mapper.ModelMapper;
import com.epam.jwd_final.web.mapper.impl.UserModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final String FIND_ALL_SQL =
            "select id, name, password, balance, role from user";

    private static final String FIND_ONE_BY_ID_SQL =
            "select id, name, password, balance, role from user where id = ?";

    private static final String FIND_ONE_BY_NAME_SQL =
            "select id, name, password, balance, role from user where name = ?";

    private static final String SAVE_USER_SQL =
            "insert into user (name, password) values (?, ?)";

    private static final String UPDATE_ROLE_SQL =
            "update user set role = ? where id = ?";

    private static final String UPDATE_BALANCE_SQL =
            "update user set balance = ? where id = ?";

    @Override
    public Optional<List<User>> findAll() throws DaoException {
        return querySelectAll(
                FIND_ALL_SQL,
                Collections.emptyList()
        );
    }

    @Override
    public Optional<User> findOneByName(String name) throws DaoException {
        return querySelectOne(
                FIND_ONE_BY_NAME_SQL,
                Collections.singletonList(name)
        );
    }

    @Override
    public Optional<User> findOneById(int id) throws DaoException {
        return querySelectOne(
                FIND_ONE_BY_ID_SQL,
                Collections.singletonList(id)
        );
    }

    @Override
    public boolean save(User user) throws DaoException {
        return queryUpdate(
                SAVE_USER_SQL,
                Arrays.asList(user.getName(), user.getPassword())
        );
    }

//    public void updateRoleById(int id, int newRoleId) {
//        final int currentRoleId = user.getRole().getId();
//        final int newRoleId = currentRoleId - 1;
//        if (Role.CLIENT.getId() >= newRoleId && Role.ADMIN.getId() <= newRoleId) {
//            queryUpdate(
//                    UPDATE_ROLE_SQL,
//                    Arrays.asList(newRoleId, user.getName())
//            );
//        }
//    }

    @Override
    public void updateRoleById(int id, int newRoleId) throws DaoException {
        if (Role.CLIENT.getId() >= newRoleId && Role.ADMIN.getId() <= newRoleId) {
            queryUpdate(
                    UPDATE_ROLE_SQL,
                    Arrays.asList(newRoleId, id)
            );
        }
    }

    @Override
    public void rollbackRoleById(int id, int newRoleId) throws DaoException {
        if (Role.CLIENT.getId() >= newRoleId && Role.ADMIN.getId() < newRoleId) {
            queryUpdate(
                    UPDATE_ROLE_SQL,
                    Arrays.asList(newRoleId, id)
            );
        }

    }

    @Override
    public void updateBalance(int id, BigDecimal balance) throws DaoException {
        queryUpdate(
                UPDATE_BALANCE_SQL,
                Arrays.asList(balance, id)
        );
    }

    @Override
    protected ModelMapper<User> retrieveModelMapper() {
        return new UserModelMapper();
    }
}

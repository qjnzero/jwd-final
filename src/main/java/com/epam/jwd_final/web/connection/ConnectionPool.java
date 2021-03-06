package com.epam.jwd_final.web.connection;

import com.epam.jwd_final.web.context.ApplicationContext;
import com.epam.jwd_final.web.exception.ConnectionPoolException;
import com.epam.jwd_final.web.property.ConnectionPoolProperty;
import com.epam.jwd_final.web.property.DatabaseProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private static final ConnectionPoolProperty connectionPoolProperty =
            ApplicationContext.getConnectionPoolProperties();

    private static final DatabaseProperty databaseProperty =
            ApplicationContext.getDatabaseProperties();

    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private static final Lock CONNECTIONS_LOCK = new ReentrantLock();
    private static final Condition NOT_FULL = CONNECTIONS_LOCK.newCondition();
    private static final Condition NOT_EMPTY = CONNECTIONS_LOCK.newCondition();

    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean expandable = new AtomicBoolean(false);
    private static final AtomicBoolean shrinkable = new AtomicBoolean(false);
    private static int counter;

    private final Deque<ProxyConnection> availableConnections;
    private final Deque<ProxyConnection> unavailableConnections;

    private ConnectionPool() {
        availableConnections = new ArrayDeque<>();
        unavailableConnections = new ArrayDeque<>();
        counter = 0;
    }

    private static class ConnectionPoolHolder {
        private static final ConnectionPool instance = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        if (!initialized.get()) {
            try {
                INSTANCE_LOCK.lock();
                if (!initialized.get()) {
                    ConnectionPoolHolder.instance.init();
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return ConnectionPoolHolder.instance;
    }

    public Connection retrieveConnection() {
        try {
            CONNECTIONS_LOCK.lock();
            expandable.set(ConnectionPoolManager.isExpandable());
            if (expandable.get()) {
                availableConnections.addAll(ConnectionPoolManager.expandPool());
            }
            while (counter == getAllConnectionsAmount()) {
                NOT_FULL.await();
            }
            ++counter;

            ProxyConnection connection = availableConnections.pollFirst();
            unavailableConnections.add(connection);
            NOT_EMPTY.signal();
            return connection;
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e.getMessage(), e);
        } finally {
            CONNECTIONS_LOCK.unlock();
        }
    }

    public void returnConnection(Connection connection) {
        try {
            CONNECTIONS_LOCK.lock();
            while (counter == 0) {
                NOT_EMPTY.await();
            }
            if (connection == null || !unavailableConnections.contains((ProxyConnection) connection)) {
                LOGGER.error("Cannot return connection. Connection is null or not a ProxyConnection");
                return;
            }
            boolean result = availableConnections.add((ProxyConnection) connection) &&
                    unavailableConnections.remove(connection);
            --counter;
            if (result) {
                NOT_FULL.signal();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Cannot return connection. Thread was interrupted");
        } finally {
            CONNECTIONS_LOCK.unlock();
        }
    }

    private void init() {
        LOGGER.info("Initializing connection pool...");
        registerDrivers();
        availableConnections.addAll(
                ConnectionPoolManager.createConnections(connectionPoolProperty.getInitialConnections())
        );
        initialized.set(true);
        ConnectionPoolManager.createListener();
    }

    public void destroy() {
        LOGGER.info("Destroying connection pool...");
        availableConnections.forEach(ProxyConnection::closeConnection);
        unavailableConnections.forEach(ProxyConnection::closeConnection);
        deregisterDrivers();
    }

    private static void registerDrivers() {
        try {
            Class.forName(databaseProperty.getClassname());
            DriverManager.registerDriver(DriverManager.getDriver(databaseProperty.getUrl()));
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Cannot register drivers");
        }
    }


    private static void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOGGER.error("Cannot deregister drivers");
            }
        }
    }

    Deque<ProxyConnection> getAvailableConnections() {
        return availableConnections;
    }

    AtomicBoolean getIsShrinkable() {
        return shrinkable;
    }

    int getAllConnectionsAmount() {
        return availableConnections.size() + unavailableConnections.size();
    }

    int getUnavailableConnectionsAmount() {
        return unavailableConnections.size();
    }

    int getAvailableConnectionsAmount() {
        return availableConnections.size();
    }
}


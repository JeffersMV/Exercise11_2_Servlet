package com.jeffersmv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DaoAbstract<E, K> {
    private Connection connection;
    private PreparedStatement psSel;
    private PreparedStatement psSelId;
    private PreparedStatement psUpd;
    private PreparedStatement psDel;

    protected DaoAbstract(Connection connection) throws DaoException, SQLException {
        this.connection = connection;
    }

    protected abstract String getSelectQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract List<E> parseResultSet(ResultSet rs) throws DaoException;

    protected abstract void prepareStatementForUpdate(PreparedStatement ps, E object) throws DaoException;


    public List<E> getAll() throws DaoException {
        List<E> lst;
        psSel = getPrepareStatement(getSelectQuery(), psSel);
        try (ResultSet rs = psSel.executeQuery()) {
            lst = parseResultSet(rs);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return lst;
    }


    public E getEntityByK(K key) throws DaoException {
        List<E> lst;
        try {
            psSelId = getPrepareStatement(getSelectQuery() + " WHERE id = ?", psSelId);
            psSelId.setInt(1, (Integer) key);
            try (ResultSet rs = psSelId.executeQuery()) {
                lst = parseResultSet(rs);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        if (lst == null || lst.size() == 0) {
            throw new DaoException("Запись с K " + key + " не найдена.");
        } else if (lst.size() > 1) {
            throw new DaoException("Поступило более одной записи.");
        }
        return lst.iterator().next();
    }



    public void update(E object) throws DaoException {
        try {
            psUpd = getPrepareStatement(getUpdateQuery(), psUpd);
            prepareStatementForUpdate(psUpd, object);
            int count = psUpd.executeUpdate();
            if (count != 1) {
                throw new DaoException("Обновляются именения более чем на 1 запись: " + count);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void delete(K key) throws DaoException {
        try {
            psDel = getPrepareStatement(getDeleteQuery(), psDel);
            psDel.setInt(1, (Integer) key);
            int count = psDel.executeUpdate();
            if (count != 1) {
                throw new DaoException("Удаляются именения более чем на 1 запись: " + count);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    protected PreparedStatement getPrepareStatement(String sql, PreparedStatement ps) throws DaoException {
        if (ps == null) {
            try {
                ps = connection.prepareStatement(sql);
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        } else {
            try {
                ps.clearParameters();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
        return ps;
    }

}

package com.jeffersmv.sql;

import com.jeffersmv.dao.DaoAbstract;
import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dto.ObjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ObjectDAO extends DaoAbstract<ObjectDTO, Integer> {

    public ObjectDAO(Connection connection) throws DaoException, SQLException {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {return "SELECT id, nameobject FROM objects";}

    @Override
    protected String getUpdateQuery() {return "UPDATE objects SET nameobject  = ? WHERE id = ?";}

    @Override
    protected String getDeleteQuery() {return "DELETE FROM objects WHERE id = ?";}

    @Override
    protected List<ObjectDTO> parseResultSet(ResultSet rs) throws DaoException {
        List<ObjectDTO> lst = new LinkedList<>();
        try {
            while (rs.next()) {
                ObjectDTO objectDTO = new ObjectDTO();
                objectDTO.setId(rs.getInt(1));
                objectDTO.setObject(rs.getString(2));
                lst.add(objectDTO);
            }
        } catch (Exception sqlE) {
            throw new DaoException(sqlE);
        }
        return lst;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, ObjectDTO objectDTO) throws DaoException {
        try {
            ps.setString(1, objectDTO.getObject());
            ps.setInt(2, objectDTO.getId());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}

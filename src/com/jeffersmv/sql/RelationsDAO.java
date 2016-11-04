package com.jeffersmv.sql;

import com.jeffersmv.dao.DaoAbstract;
import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dto.RelationsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RelationsDAO extends DaoAbstract<RelationsDTO, Integer> {
    private PreparedStatement psCre;

    public RelationsDAO(Connection connection) throws DaoException, SQLException {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT id, idstudent, idobject FROM relations";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE relations SET idstudent  = ?, idobject = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM relations WHERE id = ?";
    }

    @Override
    protected List<RelationsDTO> parseResultSet(ResultSet rs) throws DaoException {
        LinkedList<RelationsDTO> lst = new LinkedList<>();
        try {
            while (rs.next()) {
                RelationsDTO relationsDTO = new RelationsDTO();
                relationsDTO.setId(rs.getInt(1));
                relationsDTO.setIdStudent(rs.getInt(2));
                relationsDTO.setIdObject(rs.getInt(3));
                lst.add(relationsDTO);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return lst;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, RelationsDTO object) throws DaoException {

    }

    private String getCreateQuery() {
        return "INSERT INTO relations (id, idstudent, idobject) VALUES (?,?,?)";
    }

    public void create(RelationsDTO relationsDTO) throws DaoException {
        try {
            psCre = getPrepareStatement(getCreateQuery(), psCre);
            prepareStatementForInsert(psCre, relationsDTO);
            int count = psCre.executeUpdate();
            if (count != 1) {
                throw new DaoException("Сохраняются изменения более чем на 1 запись: " + count);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void prepareStatementForInsert(PreparedStatement ps, RelationsDTO relationsDTO) throws DaoException {
        try {
            ps.setInt(1, 0);
            ps.setInt(2, relationsDTO.getIdStudent());
            ps.setInt(3, relationsDTO.getIdObject());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }



}

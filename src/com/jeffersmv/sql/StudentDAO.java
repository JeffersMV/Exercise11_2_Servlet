package com.jeffersmv.sql;

import com.jeffersmv.dao.DaoAbstract;
import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dto.StudentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class StudentDAO extends DaoAbstract<StudentDTO, Integer> {

    public StudentDAO(Connection connection) throws DaoException, SQLException {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT id, firstName, lastName FROM students";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE students SET firstName  = ?, lastName = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM students WHERE id = ?";
    }

    @Override
    protected List<StudentDTO> parseResultSet(ResultSet rs) throws DaoException {
        LinkedList<StudentDTO> lst = new LinkedList<>();
        try {
            while (rs.next()) {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(rs.getInt(1));
                studentDTO.setFirstName(rs.getString(2));
                studentDTO.setLastName(rs.getString(3));
                lst.add(studentDTO);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return lst;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, StudentDTO studentDTO) throws DaoException {
        try {
            ps.setString(1, studentDTO.getFirstName());
            ps.setString(2, studentDTO.getLastName());
            ps.setInt(3, studentDTO.getId());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}

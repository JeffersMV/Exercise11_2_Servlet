package com.jeffersmv.servlets;

import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dao.DaoFactory;
import com.jeffersmv.dto.ObjectDTO;
import com.jeffersmv.dto.StudentDTO;
import com.jeffersmv.sql.ObjectDAO;
import com.jeffersmv.sql.StudentDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ShowServlet")
public class ShowServlet extends HttpServlet {
    private StudentDAO studentDAO;
    private ObjectDAO objectDAO;

    public ShowServlet() throws DaoException, SQLException {
        new DaoFactory();
        Connection connection = DaoFactory.getConnection();
        this.studentDAO = new StudentDAO(connection);
        this.objectDAO = new ObjectDAO(connection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE HTML>");
        response.getWriter().println("<html><body>");
        response.getWriter().println(formShow());
        try {
            if (Objects.equals(request.getParameter("parameter"), "student")) {
                List<StudentDTO> lst1 = studentDAO.getAll();
                int id;
                for (StudentDTO studentDTO : lst1) {
                    id = studentDTO.getId();
                    response.getWriter().print("<p>" + studentDTO.getId() + "    " + studentDTO.getFirstName() + "     " + studentDTO.getLastName()+"     " + form(id) + "</p>");
                }
            } else if (Objects.equals(request.getParameter("parameter"), "object")) {
                List<ObjectDTO> lst2 = objectDAO.getAll();
                int id;
                for (ObjectDTO objectDTO : lst2) {
                    id = objectDTO.getId();
                    response.getWriter().print("<p>" + objectDTO.getId() + "    " + objectDTO.getObject()+"     " + form(id) + "</p>");
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        response.getWriter().println("</body></html>");
    }

    private String formShow() {
        return "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"student\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список студентов\" />\n" +
                "  </form>\n" +
                "</br>\n"+
                "  <form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"object\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список предметов\" />\n" +
                "  </form>";
    }

    private String form(int id) {


        return "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"object\">\n" +
                "    <input type=\"hidden\" name=\"id\" value="+id+">\n" +
                "    <input type=\"submit\" value=\"Редактировать\"/>\n" +
                "    <input type=\"submit\" value=\"Удалить\"/>\n" +
                "    <input type=\"submit\" value=\"Назначить предмет\"/>\n" +
                "</form>";
    }
}

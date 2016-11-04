package com.jeffersmv.servlets;

import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dao.DaoFactory;
import com.jeffersmv.dto.ObjectDTO;
import com.jeffersmv.dto.RelationsDTO;
import com.jeffersmv.dto.StudentDTO;
import com.jeffersmv.sql.ObjectDAO;
import com.jeffersmv.sql.RelationsDAO;
import com.jeffersmv.sql.StudentDAO;

import javax.servlet.ServletException;
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
    private RelationsDAO relationsDAO;

    public ShowServlet() throws DaoException, SQLException {
        new DaoFactory();
        Connection connection = DaoFactory.getConnection();
        this.studentDAO = new StudentDAO(connection);
        this.objectDAO = new ObjectDAO(connection);
        this.relationsDAO=new RelationsDAO(connection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE HTML>");
        response.getWriter().println("<html><body>");
        response.getWriter().println(formShow());
        if (Objects.equals(request.getParameter("action"), "1")) {
            response.getWriter().print("<p>Сохранение успешно выполнено!</p>");
        } else if (Objects.equals(request.getParameter("action"), "2")) {
            response.getWriter().print("<p>Удаление успешно выполнено!</p>");
        } else if (Objects.equals(request.getParameter("action"), "3")) {
            response.getWriter().print("<p>Назначение успешно выполнено!</p>");
        } else if (Objects.equals(request.getParameter("action"), "31")) {
            response.getWriter().print("<p>Назначение уже существовало!</p>");
        }
        String parameter = request.getParameter("parameter");
        try {
            if (Objects.equals(request.getParameter("edit"), "Edit")) {
                response.getWriter().println(formSave(parameter, Integer.valueOf(request.getParameter("id"))));
            } else if (Objects.equals(request.getParameter("del"), "Delete")) {
                response.getWriter().println(formDelete(parameter, Integer.valueOf(request.getParameter("id"))));
            } else if (Objects.equals(request.getParameter("ss"), "Specify subject")) {
                response.getWriter().print("<p>" + formAssignment(Integer.valueOf(request.getParameter("id"))) + "</p>");
            } else if (Objects.equals(parameter, "student")) {
                List<StudentDTO> lst1 = studentDAO.getAll();
                int id;
                for (StudentDTO studentDTO : lst1) {
                    id = studentDTO.getId();
                    response.getWriter().print("<p>" + studentDTO.getId() + "    " + studentDTO.getFirstName() + "     " + studentDTO.getLastName() + "     " + formList(parameter, id) + "</p>");
                }
            } else if (Objects.equals(parameter, "object")) {
                List<ObjectDTO> lst2 = objectDAO.getAll();
                int id;
                for (ObjectDTO objectDTO : lst2) {
                    id = objectDTO.getId();
                    response.getWriter().print("<p>" + objectDTO.getId() + "    " + objectDTO.getObject() + "     " + formList(parameter, id) + "</p>");
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
                "</br>\n" +
                "  <form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"object\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список предметов\" />\n" +
                "  </form>";
    }

    private String formList(String parameter, Integer id) {
        return "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\">\n" +
                "    <input type=\"hidden\" name=\"id\" value=" + id + ">\n" +
                "    <input type=\"submit\" name=\"edit\" value=\"Edit\"/>\n" +
                "    <input type=\"submit\" name=\"del\" value=\"Delete\"/>\n" + ((Objects.equals(parameter, "student")) ?
                "    <input type=\"submit\" name=\"ss\" value=\"Specify subject\"/>\n" : "") + "</form>";
    }



    private String formSave(String parameter, Integer id) throws DaoException {
        return "<form action=\"ShowServlet\" method=\"POST\">\n" + ((Objects.equals(parameter, "student")) ?
                "    <input type=\"text\" name=\"firstName\" value=\"" + studentDAO.getEntityByK(id).getFirstName() + "\">\n" +
                        "    <input type=\"text\" name=\"lastName\" value=\"" + studentDAO.getEntityByK(id).getLastName() + "\">\n" :
                "    <input type=\"text\" name=\"object\" value=\"" + objectDAO.getEntityByK(id).getObject() + "\">\n") +
                "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                "    <input type=\"submit\" name=\"button\" value=\"Save\"/>\n" +
                "    <input type=\"submit\" name=\"button\" value=\"Cancel\"/>\n" +
                "</form>";
    }

    private String formDelete(String parameter, Integer id) throws DaoException {
        return "<form action=\"ShowServlet\" method=\"POST\">\n" + ((Objects.equals(parameter, "student")) ?
                "    Вы уверены что хотите удалить \"" + studentDAO.getEntityByK(id).getId() + " | " + studentDAO.getEntityByK(id).getFirstName() + " " + studentDAO.getEntityByK(id).getLastName() + "\"!\n" :
                "    Вы уверены что хотите удалить \"" + objectDAO.getEntityByK(id).getId() + " | " + objectDAO.getEntityByK(id).getObject() + "\"!\n") +
                "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                "    <input type=\"submit\" name=\"button\" value=\"Delete\"/>\n" +
                "    <input type=\"submit\" name=\"button\" value=\"Cancel\"/>\n" +
                "</form>";
    }

    private String formAssignment(Integer idStudent) throws DaoException {
        String radioList = "";
        List<ObjectDTO> list = objectDAO.getAll();
        for (ObjectDTO objectDTO : list) {
            radioList += "    <input type=\"radio\" checked=\"checked\" name=\"idObject\" value=\"" + objectDTO.getId() + "\">" + objectDTO.getId() + " | " + objectDTO.getObject() + "<Br><Br>";
        }
        return "<form action=\"/ShowServlet\" method=\"POST\">\n" +
                "    <input type=\"hidden\" name=\"id\" value=\""+idStudent+"\">\n" +
                radioList +
                "    <input type=\"submit\" name=\"button\" value=\"Specify subject\" />\n" +
                "    <input type=\"submit\" name=\"button\" value=\"Cancel\" />\n" +
                "  </form>";
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = request.getParameter("parameter");
        Integer id = Integer.valueOf(request.getParameter("id"));
        if (Objects.equals(request.getParameter("button"), "Save")) {
            if (Objects.equals(parameter, "student")) {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(id);
                studentDTO.setFirstName(firstName);
                studentDTO.setLastName(lastName);
                try {
                    studentDAO.update(studentDTO);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(parameter, "object")) {
                String object = request.getParameter("object");
                ObjectDTO objectDTO = new ObjectDTO();
                objectDTO.setId(id);
                objectDTO.setObject(object);
                try {
                    objectDAO.update(objectDTO);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("/ShowServlet?action=1");
        } else if (Objects.equals(request.getParameter("button"), "Delete")) {
            if (Objects.equals(parameter, "student")) {
                try {
                    studentDAO.delete(id);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(parameter, "object")) {
                try {
                    objectDAO.delete(id);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("/ShowServlet?action=2");
        } else if (Objects.equals(request.getParameter("button"), "Specify subject")) {
            Integer idObject = Integer.valueOf(request.getParameter("idObject"));
            List<RelationsDTO> lst2;
            int cont=0;
            try {
                lst2 = relationsDAO.getAll();
                for (RelationsDTO relationsDTO : lst2) {
                    if ((relationsDTO.getIdStudent() == id) && (relationsDTO.getIdObject() == idObject)) {
                        response.sendRedirect("/ShowServlet?action=31");
                        cont++;
                        break;
                    }
                }

            } catch (DaoException e) {
                e.printStackTrace();
            }
            if (cont == 0) {
                RelationsDTO relationsDTO=new RelationsDTO();
                relationsDTO.setIdStudent(id);
                relationsDTO.setIdObject(idObject);
                try {
                    relationsDAO.create(relationsDTO);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
                response.sendRedirect("/ShowServlet?action=3");
            }
        } else if (Objects.equals(request.getParameter("button"), "Cancel")) {
            response.sendRedirect("/ShowServlet");
        }
    }
}

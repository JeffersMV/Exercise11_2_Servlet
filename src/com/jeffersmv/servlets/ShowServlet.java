package com.jeffersmv.servlets;

import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dto.ObjectDTO;
import com.jeffersmv.dto.RelationsDTO;
import com.jeffersmv.dto.StudentDTO;
import com.jeffersmv.sql.ObjectDAO;
import com.jeffersmv.sql.RelationsDAO;
import com.jeffersmv.sql.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ShowServlet extends HttpServlet{

    public ShowServlet() throws DaoException, SQLException {
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
        ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
        RelationsDAO relationsDAO = (RelationsDAO) request.getSession().getAttribute("relationsDAO");

        String parameter = request.getParameter("parameter");
        if (request.getParameter("id") != null) {
            Integer idParameter = Integer.valueOf(request.getParameter("id"));
            if (Objects.equals(request.getParameter("button"), "Save")) {
                if (Objects.equals(parameter, "student")) {
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    if (firstName == null || "".equals(firstName.trim())) {
                        response.sendRedirect("/ShowServlet?error=NotFound");
                    } else if (lastName == null || "".equals(lastName.trim())) {
                        response.sendRedirect("/ShowServlet?error=NotFound");
                    } else {
                        StudentDTO studentDTO = new StudentDTO();
                        studentDTO.setId(idParameter);
                        studentDTO.setFirstName(firstName);
                        studentDTO.setLastName(lastName);
                        try {
                            studentDAO.update(studentDTO);
                        } catch (DaoException e) {
                            response.sendRedirect("/ShowServlet?error=ErrorSaveStudent");
                        }
                        response.sendRedirect("/ShowServlet?action=save");
                    }
                } else if (Objects.equals(parameter, "object")) {
                    String object = request.getParameter("object");
                    if (object == null || "".equals(object.trim())) {
                        response.sendRedirect("/ShowServlet?error=NotFound");
                    }else {
                        ObjectDTO objectDTO = new ObjectDTO();
                        objectDTO.setId(idParameter);
                        objectDTO.setObject(object);
                        try {
                            objectDAO.update(objectDTO);
                        } catch (DaoException e) {
                            response.sendRedirect("/ShowServlet?error=ErrorSaveObject");
                        }
                        response.sendRedirect("/ShowServlet?action=save");
                    }
                }
            } else if (Objects.equals(request.getParameter("button"), "Delete")) {
                if (Objects.equals(parameter, "student")) {
                    try {
                        studentDAO.delete(idParameter);
                    } catch (DaoException e) {
                        response.sendRedirect("/ShowServlet?error=ErrorDeleteStudent");
                    }
                    response.sendRedirect("/ShowServlet?action=delete");
                } else if (Objects.equals(parameter, "object")) {
                    try {
                        objectDAO.delete(idParameter);
                    } catch (DaoException e) {
                        response.sendRedirect("/ShowServlet?error=ErrorDeleteObject");
                    }
                    response.sendRedirect("/ShowServlet?action=delete");
                }
            } else if (Objects.equals(request.getParameter("button"), "Specify subject")) {
                Integer idObject = Integer.valueOf(request.getParameter("idObject"));
                List<RelationsDTO> lst2;
                int cont = 0;
                try {
                    lst2 = relationsDAO.getAll();
                    for (RelationsDTO relationsDTO : lst2) {
                        if ((relationsDTO.getIdStudent() == idParameter) && (relationsDTO.getIdObject() == idObject)) {
                            response.sendRedirect("/ShowServlet?action=exists");
                            cont++;
                            break;
                        }
                    }
                } catch (DaoException e) {
                    response.sendRedirect("/ShowServlet?error=ErrorSearchSpecify");
                }
                if (cont == 0) {
                    RelationsDTO relationsDTO = new RelationsDTO();
                    relationsDTO.setIdStudent(idParameter);
                    relationsDTO.setIdObject(idObject);
                    try {
                        relationsDAO.create(relationsDTO);
                    } catch (DaoException e) {
                        response.sendRedirect("/ShowServlet?error=ErrorSpecifySubject");
                    }
                    response.sendRedirect("/ShowServlet?action=specifySubject");
                }
            } else if (Objects.equals(request.getParameter("button"), "Cancel")) {
                response.sendRedirect("/ShowServlet");
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE HTML><html><body>");
        response.getWriter().println("<form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"student\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список студентов\" />\n" +
                "  </form>\n" +
                "</br>\n" +
                "  <form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"object\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список предметов\" />\n" +
                "  </form><br>");

        if ((request.getParameter("error")) != null) {
            if (Objects.equals(request.getParameter("error"), "NotFound")) {
                response.getWriter().print("<p style=\"color: crimson\">Необходимо заполнить поле, поле не может быть пустым!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorSaveStudent")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка сохранения студента!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorSaveObject")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка сохранения предмета!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorDeleteStudent")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка удаления студента!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorDeleteObject")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка удаления предмета!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorSearchSpecify")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка поиска назначения на наличие!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorSpecifySubject")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка назначения предмета студенту!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorFormButton")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка создания форм редактирования!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorGetAllStudent")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка студентов!</p>");
            } else if (Objects.equals(request.getParameter("error"), "ErrorGetAllObject")) {
                response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка предметов!</p>");
            }
        } else if ((request.getParameter("action")) != null) {
            if (Objects.equals(request.getParameter("action"), "save")) {
                response.getWriter().print("<p>Сохранение успешно выполнено!</p>");
            } else if (Objects.equals(request.getParameter("action"), "delete")) {
                response.getWriter().print("<p>Удаление успешно выполнено!</p>");
            } else if (Objects.equals(request.getParameter("action"), "specifySubject")) {
                response.getWriter().print("<p>Назначение успешно выполнено!</p>");
            } else if (Objects.equals(request.getParameter("action"), "exists")) {
                response.getWriter().print("<p>Назначение уже существовало!</p>");
            }
        } else if (request.getParameter("formButton") != null) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            try {
                if (Objects.equals(request.getParameter("formButton"), "Edit")) {
                    response.getWriter().println(
                            "<form action=\"ShowServlet\" method=\"GET\">\n" + ((Objects.equals(parameter, "student")) ?
                                    "    <input type=\"text\" name=\"firstName\" value=\"" + studentDAO.getEntityByK(id).getFirstName() + "\">\n" +
                                            "    <input type=\"text\" name=\"lastName\" value=\"" + studentDAO.getEntityByK(id).getLastName() + "\">\n" :
                                    "    <input type=\"text\" name=\"object\" value=\"" + objectDAO.getEntityByK(id).getObject() + "\">\n") +
                                    "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                                    "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                                    "    <input type=\"submit\" name=\"button\" value=\"Save\"/>\n" +
                                    "    <input type=\"submit\" name=\"button\" value=\"Cancel\"/>\n" +
                                    "</form>");
                } else if (Objects.equals(request.getParameter("formButton"), "Delete")) {
                    response.getWriter().println(
                            "<form action=\"ShowServlet\" method=\"GET\">\n" + ((Objects.equals(parameter, "student")) ?
                                    "    Вы уверены что хотите удалить \"" + studentDAO.getEntityByK(id).getId() + " | " + studentDAO.getEntityByK(id).getFirstName() + " " + studentDAO.getEntityByK(id).getLastName() + "\"!\n" :
                                    "    Вы уверены что хотите удалить \"" + objectDAO.getEntityByK(id).getId() + " | " + objectDAO.getEntityByK(id).getObject() + "\"!\n") +
                                    "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                                    "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                                    "    <input type=\"submit\" name=\"button\" value=\"Delete\"/>\n" +
                                    "    <input type=\"submit\" name=\"button\" value=\"Cancel\"/>\n" +
                                    "</form>");
                } else if (Objects.equals(request.getParameter("formButton"), "Specify subject")) {
                    List<ObjectDTO> list = objectDAO.getAll();
                    response.getWriter().print("<p>" + "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                            "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\">\n");
                    for (ObjectDTO objectDTO : list) {
                        response.getWriter().print("    <input type=\"radio\" checked=\"checked\" name=\"idObject\" value=\"" + objectDTO.getId() + "\">" + objectDTO.getId() + " | " + objectDTO.getObject() + "<Br><Br>");
                    }
                    response.getWriter().print("    <input type=\"submit\" name=\"button\" value=\"Specify subject\" />\n" +
                            "    <input type=\"submit\" name=\"button\" value=\"Cancel\" />\n" +
                            "  </form></p>");
                }
            } catch (DaoException e) {
                response.sendRedirect("/ShowServlet?error=ErrorFormButton");
            }
        } else if (Objects.equals(parameter, "student")) {
            try {
                List<StudentDTO> lst1 = studentDAO.getAll();
                int idObject;
                for (StudentDTO studentDTO : lst1) {
                    idObject = studentDTO.getId();
                    response.getWriter().print("<p>" + studentDTO.getId() + "    " + studentDTO.getFirstName() + "     " + studentDTO.getLastName() + "     " +
                            "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                            "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\">\n" +
                            "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                            "    <input type=\"submit\" name=\"formButton\" value=\"Edit\"/>\n" +
                            "    <input type=\"submit\" name=\"formButton\" value=\"Delete\"/>\n" +
                            "    <input type=\"submit\" name=\"formButton\" value=\"Specify subject\"/>\n" +
                            "</form>" + "</p>");
                }
            } catch (DaoException e) {
                response.sendRedirect("/ShowServlet?error=ErrorGetAllStudent");
            }
        } else if (Objects.equals(parameter, "object")) {
            try {
                List<ObjectDTO> lst2 = objectDAO.getAll();
                int idObject;
                for (ObjectDTO objectDTO : lst2) {
                    idObject = objectDTO.getId();
                    response.getWriter().print("<p>" + objectDTO.getId() + "    " + objectDTO.getObject() + "     " +
                            "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                            "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\">\n" +
                            "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                            "    <input type=\"submit\" name=\"formButton\" value=\"Edit\"/>\n" +
                            "    <input type=\"submit\" name=\"formButton\" value=\"Delete\"/>\n" +
                            "</form>" + "</p>");
                }
            } catch (DaoException e) {
                response.sendRedirect("/ShowServlet?error=ErrorGetAllObject");
            }
        }
        response.getWriter().println("</body></html>");
    }

}

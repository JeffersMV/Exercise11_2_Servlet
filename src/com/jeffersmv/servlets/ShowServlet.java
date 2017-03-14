package com.jeffersmv.servlets;

import com.jeffersmv.dao.DaoException;
import com.jeffersmv.dto.ObjectDTO;
import com.jeffersmv.dto.RelationsDTO;
import com.jeffersmv.dto.StudentDTO;
import com.jeffersmv.sql.ObjectDAO;
import com.jeffersmv.sql.RelationsDAO;
import com.jeffersmv.sql.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ShowServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
                "<!DOCTYPE HTML><html><body>\n" +
                        "  <form action=\"/ShowServlet\" method=\"POST\">\n" +
                        "    <input type=\"hidden\" name=\"action\" value=\"student\">\n" +
                        "    <input type=\"submit\" value=\"Отобразить список студентов\" />\n" +
                        "  </form>\n" +
                        "</br>\n" +
                        "  <form action=\"/ShowServlet\" method=\"POST\">\n" +
                        "    <input type=\"hidden\" name=\"action\" value=\"object\">\n" +
                        "    <input type=\"submit\" value=\"Отобразить список предметов\" />\n" +
                        "  </form><br>");

        if (Objects.equals(request.getParameter("action"), "student")) {
            studentAction(request, response);
        } else if (Objects.equals(request.getParameter("action"), "object")){
            objectAction(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Edit")){
            editAction(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Delete")){
            deleteAction(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Specify subject")){
            specifySubjectAction(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Save")){
            save(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Remove")){
            remove(request, response);
        } else if (Objects.equals(request.getParameter("action"), "Appoint")){
            appoint(request, response);
        }

        response.getWriter().println("</body></html>");
    }

    private void studentAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
            List<StudentDTO> lst1 = studentDAO.getAll();
            int idObject;
            for (StudentDTO studentDTO : lst1) {
                idObject = studentDTO.getId();
                response.getWriter().print("<p>" + studentDTO.getId() + "    " + studentDTO.getFirstName() + "     " + studentDTO.getLastName() + "     " +
                        "<form action=\"/ShowServlet\" method=\"POST\">\n" +
                        "    <input type=\"hidden\" name=\"parameter\" value=\"" + request.getParameter("action") + "\">\n" +
                        "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                        "    <input type=\"submit\" name=\"action\" value=\"Edit\"/>\n" +
                        "    <input type=\"submit\" name=\"action\" value=\"Delete\"/>\n" +
                        "    <input type=\"submit\" name=\"action\" value=\"Specify subject\"/>\n" +
                        "</form>" + "</p>");
            }
        } catch (DaoException e) {
            outputServerResponse(response , "ErrorGetAllStudent");
        }
    }

    private void objectAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
            List<ObjectDTO> lst2 = objectDAO.getAll();
            int idObject;
            for (ObjectDTO objectDTO : lst2) {
                idObject = objectDTO.getId();
                response.getWriter().print("<p>" + objectDTO.getId() + "    " + objectDTO.getObject() + "     " +
                        "<form action=\"/ShowServlet\" method=\"POST\">\n" +
                        "    <input type=\"hidden\" name=\"parameter\" value=\"" + request.getParameter("action") + "\">\n" +
                        "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                        "    <input type=\"submit\" name=\"action\" value=\"Edit\"/>\n" +
                        "    <input type=\"submit\" name=\"action\" value=\"Delete\"/>\n" +
                        "</form>" + "</p>");
            }
        } catch (DaoException e) {
            outputServerResponse(response , "ErrorGetAllObject");
        }
    }

    private void editAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String parameter = request.getParameter("parameter");
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            response.getWriter().println(
                    "<form action=\"ShowServlet\" method=\"POST\">\n");
            if (Objects.equals(parameter, "student")) {
                StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
                StudentDTO studentDTO = studentDAO.getEntityByK(id);
                response.getWriter().println(
                    "    <input type=\"text\" name=\"firstName\" value=\"" + studentDTO.getFirstName() + "\" required placeholder=\"First name\">\n" +
                    "    <input type=\"text\" name=\"lastName\" value=\"" + studentDTO.getLastName() + "\" required placeholder=\"Last name\">\n");
            } else {
                ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
                response.getWriter().println(
                    "    <input type=\"text\" name=\"object\" value=\"" + objectDAO.getEntityByK(id).getObject() + "\" required placeholder=\"Subject name\">\n");
            }
            response.getWriter().println(
                "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                "    <input type=\"submit\" name=\"action\" value=\"Save\"/>\n" +
                "    <input type=\"submit\" name=\"action\" value=\"Cancel\"/>\n" +
                "</form>");
        } catch (DaoException e) {
            outputServerResponse(response, "ErrorFormEdit");
        }
    }

    private void deleteAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String parameter = request.getParameter("parameter");
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            response.getWriter().println(
                    "<form action=\"ShowServlet\" method=\"POST\">\n" +
                            "    Вы уверены что хотите удалить \"");
            if (Objects.equals(parameter, "student")) {
                StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
                StudentDTO studentDTO = studentDAO.getEntityByK(id);
                response.getWriter().println(
                        studentDTO.getId() + " | " + studentDTO.getFirstName() + " " + studentDTO.getLastName() + "\"!\n");
            } else {
                ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
                ObjectDTO objectDTO = objectDAO.getEntityByK(id);
                response.getWriter().println(
                        objectDTO.getId() + " | " + objectDTO.getObject() + "\"!\n");
            }
            response.getWriter().println(
                    "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>\n" +
                            "    <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\"/>\n" +
                            "    <input type=\"submit\" name=\"action\" value=\"Remove\"/>\n" +
                            "    <input type=\"submit\" name=\"action\" value=\"Cancel\"/>\n" +
                            "</form>");
        } catch (DaoException e) {
            outputServerResponse(response, "ErrorFormDelete");
        }
    }

    private void specifySubjectAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            List<ObjectDTO> list = objectDAO.getAll();
            response.getWriter().print("<p>" + "<form action=\"/ShowServlet\" method=\"POST\">\n" +
                    "    <input type=\"hidden\" name=\"id\" value=\"" + id + "\">\n");
            for (ObjectDTO objectDTO : list) {
                response.getWriter().print("    <input type=\"radio\" checked=\"checked\" name=\"idObject\" value=\"" + objectDTO.getId() + "\">" + objectDTO.getId() + " | " + objectDTO.getObject() + "<Br><Br>");
            }
            response.getWriter().print("    <input type=\"submit\" name=\"action\" value=\"Appoint\" />\n" +
                    "    <input type=\"submit\" name=\"action\" value=\"Cancel\" />\n" +
                    "  </form></p>");
        } catch (DaoException e) {
            outputServerResponse(response, "ErrorFormSpecifySubject");
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String parameter = request.getParameter("parameter");
        Integer idParameter = Integer.valueOf(request.getParameter("id"));
        if (Objects.equals(parameter, "student")) {
            StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            if (firstName == null || "".equals(firstName.trim())) {
                outputServerResponse(response , "NotFound");
            } else if (lastName == null || "".equals(lastName.trim())) {
                outputServerResponse(response , "NotFound");
            } else {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(idParameter);
                studentDTO.setFirstName(firstName);
                studentDTO.setLastName(lastName);
                try {
                    studentDAO.update(studentDTO);
                } catch (DaoException e) {
                    outputServerResponse(response , "ErrorSaveStudent");
                }
                outputServerResponse(response, "save");
            }
        } else if (Objects.equals(parameter, "object")) {
            ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
            String object = request.getParameter("object");
            ObjectDTO objectDTO = new ObjectDTO();
            objectDTO.setId(idParameter);
            objectDTO.setObject(object);
            try {
                objectDAO.update(objectDTO);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorSaveObject");
            }
            outputServerResponse(response, "save");
        }
    }

    private void remove(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String parameter = request.getParameter("parameter");
        Integer idParameter = Integer.valueOf(request.getParameter("id"));
        if (Objects.equals(parameter, "student")) {
            StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
            try {
                studentDAO.delete(idParameter);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorDeleteStudent");
            }
            outputServerResponse(response, "delete");
        } else if (Objects.equals(parameter, "object")) {
            ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
            try {
                objectDAO.delete(idParameter);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorDeleteObject");
            }
            outputServerResponse(response, "delete");
        }
    }

    private void appoint(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RelationsDAO relationsDAO = (RelationsDAO) request.getSession().getAttribute("relationsDAO");
        Integer idParameter = Integer.valueOf(request.getParameter("id"));
        Integer idObject = Integer.valueOf(request.getParameter("idObject"));
        List<RelationsDTO> lst2;
        int cont = 0;
        try {
            lst2 = relationsDAO.getAll();
            for (RelationsDTO relationsDTO : lst2) {
                if ((relationsDTO.getIdStudent() == idParameter) && (relationsDTO.getIdObject() == idObject)) {
                    outputServerResponse(response, "exists");
                    cont++;
                    break;
                }
            }
        } catch (DaoException e) {
            outputServerResponse(response , "ErrorSearchSpecify");
        }
        if (cont == 0) {
            RelationsDTO relationsDTO = new RelationsDTO();
            relationsDTO.setIdStudent(idParameter);
            relationsDTO.setIdObject(idObject);
            try {
                relationsDAO.create(relationsDTO);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorSpecifySubject");
            }
            outputServerResponse(response, "specifySubject");
        }
    }

    private void outputServerResponse(HttpServletResponse response, String serverResponse) throws IOException {
        if (Objects.equals(serverResponse, "save")) {
            response.getWriter().print("<p>Сохранение успешно выполнено!</p>\n");
        } else if (Objects.equals(serverResponse, "delete")) {
            response.getWriter().print("<p>Удаление успешно выполнено!</p>\n");
        } else if (Objects.equals(serverResponse, "specifySubject")) {
            response.getWriter().print("<p>Назначение успешно выполнено!</p>\n");
        } else if (Objects.equals(serverResponse, "exists")) {
            response.getWriter().print("<p>Назначение уже существовало!</p>\n");
        }else if (Objects.equals(serverResponse, "NotFound")) {
            response.getWriter().print("<p style=\"color: crimson\">Необходимо заполнить поле, поле не может быть пустым!</p>");
        } else if (Objects.equals(serverResponse, "ErrorSaveStudent")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка сохранения студента!</p>");
        } else if (Objects.equals(serverResponse, "ErrorSaveObject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка сохранения предмета!</p>");
        } else if (Objects.equals(serverResponse, "ErrorDeleteStudent")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка удаления студента!</p>");
        } else if (Objects.equals(serverResponse, "ErrorDeleteObject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка удаления предмета!</p>");
        } else if (Objects.equals(serverResponse, "ErrorSearchSpecify")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка поиска назначения на наличие!</p>");
        } else if (Objects.equals(serverResponse, "ErrorSpecifySubject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка назначения предмета студенту!</p>");
        } else if (Objects.equals(serverResponse, "ErrorFormEdit")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка создания формы редактирования!</p>");
        } else if (Objects.equals(serverResponse, "ErrorFormDelete")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка создания формы удаления!</p>");
        } else if (Objects.equals(serverResponse, "ErrorFormSpecifySubject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка создания формы назначения!</p>");
        } else if (Objects.equals(serverResponse, "ErrorGetAllStudent")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка студентов!</p>");
        } else if (Objects.equals(serverResponse, "ErrorGetAllObject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка предметов!</p>");
        }
    }
}
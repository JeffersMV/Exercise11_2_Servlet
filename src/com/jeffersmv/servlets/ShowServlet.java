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
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ShowServlet extends HttpServlet {

    public ShowServlet() throws DaoException, SQLException {
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
                "<!DOCTYPE HTML><html><body>\n" +
                "  <form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"student\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список студентов\" />\n" +
                "  </form>\n" +
                "</br>\n" +
                "  <form action=\"/ShowServlet\" method=\"GET\">\n" +
                "    <input type=\"hidden\" name=\"parameter\" value=\"object\">\n" +
                "    <input type=\"submit\" value=\"Отобразить список предметов\" />\n" +
                "  </form><br>");

        if (request.getParameter("id") != null) {
            if (Objects.equals(request.getParameter("button"), "Save")) {
                saveButtonParameter(request, response);
            } else if (Objects.equals(request.getParameter("button"), "Delete")){
                deleteButtonParameter(request, response);
            } else if (Objects.equals(request.getParameter("button"), "Specify subject")){
                specify_subjectButtonParameter(request, response);
            }
        }

        if (request.getParameter("formButton") != null) {
            formButtonParameter(request, response);
        } else if (Objects.equals(request.getParameter("parameter"), "student")) {
            studentParameter(request, response);
        } else if (Objects.equals(request.getParameter("parameter"), "object")){
            objectParameter(request, response);
        }
            response.getWriter().println("</body></html>");
    }

    private void studentParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
            List<StudentDTO> lst1 = studentDAO.getAll();
            int idObject;
            for (StudentDTO studentDTO : lst1) {
                idObject = studentDTO.getId();
                response.getWriter().print("<p>" + studentDTO.getId() + "    " + studentDTO.getFirstName() + "     " + studentDTO.getLastName() + "     " +
                        "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                        "    <input type=\"hidden\" name=\"parameter\" value=\"" + request.getParameter("parameter") + "\">\n" +
                        "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                        "    <input type=\"submit\" name=\"formButton\" value=\"Edit\"/>\n" +
                        "    <input type=\"submit\" name=\"formButton\" value=\"Delete\"/>\n" +
                        "    <input type=\"submit\" name=\"formButton\" value=\"Specify subject\"/>\n" +
                        "</form>" + "</p>");
            }
        } catch (DaoException e) {
            outputServerResponse(response , "ErrorGetAllStudent");
        }
    }

    private void objectParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
            List<ObjectDTO> lst2 = objectDAO.getAll();
            int idObject;
            for (ObjectDTO objectDTO : lst2) {
                idObject = objectDTO.getId();
                response.getWriter().print("<p>" + objectDTO.getId() + "    " + objectDTO.getObject() + "     " +
                        "<form action=\"/ShowServlet\" method=\"GET\">\n" +
                        "    <input type=\"hidden\" name=\"parameter\" value=\"" + request.getParameter("parameter") + "\">\n" +
                        "    <input type=\"hidden\" name=\"id\" value=" + idObject + ">\n" +
                        "    <input type=\"submit\" name=\"formButton\" value=\"Edit\"/>\n" +
                        "    <input type=\"submit\" name=\"formButton\" value=\"Delete\"/>\n" +
                        "</form>" + "</p>");
            }
        } catch (DaoException e) {
            outputServerResponse(response , "ErrorGetAllObject");
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
        } else if (Objects.equals(serverResponse, "ErrorFormButton")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка создания форм редактирования!</p>");
        } else if (Objects.equals(serverResponse, "ErrorGetAllStudent")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка студентов!</p>");
        } else if (Objects.equals(serverResponse, "ErrorGetAllObject")) {
            response.getWriter().print("<p style=\"color: crimson\">Ошибка получения списка предметов!</p>");
        }
    }

    private void formButtonParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
        ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
        String parameter = request.getParameter("parameter");
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
            outputServerResponse(response, "ErrorFormButton");

        }
    }

    private void saveButtonParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
        ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
        String parameter = request.getParameter("parameter");
        Integer idParameter = Integer.valueOf(request.getParameter("id"));
        if (Objects.equals(parameter, "student")) {
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
            String object = request.getParameter("object");
            if (object == null || "".equals(object.trim())) {
                outputServerResponse(response , "NotFound");
            } else {
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
    }

    private void deleteButtonParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StudentDAO studentDAO = (StudentDAO) request.getSession().getAttribute("studentDAO");
        ObjectDAO objectDAO = (ObjectDAO) request.getSession().getAttribute("objectDAO");
        String parameter = request.getParameter("parameter");
        Integer idParameter = Integer.valueOf(request.getParameter("id"));
        if (Objects.equals(parameter, "student")) {
            try {
                studentDAO.delete(idParameter);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorDeleteStudent");
            }
            outputServerResponse(response, "delete");

        } else if (Objects.equals(parameter, "object")) {
            try {
                objectDAO.delete(idParameter);
            } catch (DaoException e) {
                outputServerResponse(response , "ErrorDeleteObject");
            }
            outputServerResponse(response, "delete");
        }
    }

    private void specify_subjectButtonParameter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
}
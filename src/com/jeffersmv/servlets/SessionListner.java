package com.jeffersmv.servlets;

import com.jeffersmv.dao.DaoFactory;
import com.jeffersmv.sql.ObjectDAO;
import com.jeffersmv.sql.RelationsDAO;
import com.jeffersmv.sql.StudentDAO;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;


public class SessionListner implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // Get the session that was created / Получить сессию, которая была создана
        HttpSession session = httpSessionEvent.getSession();

        // Store something in the session, and log a message / Хранить что-то в сессии, и войти сообщение
        try {
            new DaoFactory();
            Connection connection = DaoFactory.getConnection();
            StudentDAO studentDAO = new StudentDAO(connection);
            ObjectDAO objectDAO = new ObjectDAO(connection);
            RelationsDAO relationsDAO = new RelationsDAO(connection);
            System.out.println("[MySessionListener] Session created: "+session);

            session.setAttribute("studentDAO", studentDAO);
            session.setAttribute("objectDAO", objectDAO);
            session.setAttribute("relationsDAO", relationsDAO);
        } catch (Exception e) {
            System.out.println("[MySessionListener] Error setting session attribute: " + e.getMessage());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // Get the session that was invalidated \ Получить сессию, которая была признана недействительной
        HttpSession session = httpSessionEvent.getSession();

        // Log a message
        System.out.println("[MySessionListener] Session invalidated: "+session);
    }
}

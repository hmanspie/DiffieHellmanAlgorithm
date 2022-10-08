package com.hmanspie;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "start-server", value = "/start-server")
public class ServerServlet extends HttpServlet {
    private String message;
    public static boolean serverStart = false;
    public void init() {
        message = "Server is running!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1 align=\"center\">" + message + "</h1>");
        out.println("</body></html>");
        serverStart = true;
    }

    public void destroy() {}
}
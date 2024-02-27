package mk.ukim.finki.wp.eshop.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/servlet/hello"})
public class HelloWorldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Query string parameters
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        // Write the html in the response
        PrintWriter writer = resp.getWriter();
        writer.format("<html><head></head><body><h1>Hello %s %s.</h1></body></html>", name, surname);
        writer.flush();
    }
}

package com.login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/login")
public class LoginServelet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get credentials from request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String filePath = getServletContext().getRealPath("/login/Data/user.txt");
        boolean isAuthenticated = false;

        File file = new File(filePath);
        if (!file.exists()) {
            response.getWriter().println("Error: User data not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Email: ")) {
                    String storedEmail = line.substring(7).trim();
                    String storedPasswordLine = reader.readLine(); // Next line should be Phone, then Password

                    if (storedPasswordLine != null && storedPasswordLine.startsWith("Phone: ")) {
                        storedPasswordLine = reader.readLine(); // Read Password line
                    }

                    if (storedPasswordLine != null && storedPasswordLine.startsWith("Password: ")) {
                        String storedPassword = storedPasswordLine.substring(9).trim();

                        if (storedEmail.equals(email) && storedPassword.equals(password)) {
                            isAuthenticated = true;
                            break;
                        }
                    }
                }
            }
        }

        if (isAuthenticated) {
            // Set session if needed
            HttpSession session = request.getSession();
            session.setAttribute("email", email);

            // Redirect to home or dashboard
            response.sendRedirect("home.jsp"); // Replace with your actual home page
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('Invalid email or password.'); window.location='index.jsp';</script>");
        }
    }
}
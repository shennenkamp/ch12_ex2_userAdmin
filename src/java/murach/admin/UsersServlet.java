package murach.admin;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;

public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();

        String url = "/index.jsp";
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "display_users";  // default action
        }
        
        // perform action and set URL to appropriate page
        if (action.equals("display_users")) {            
            // get list of users
            ArrayList<User> users = UserDB.selectUsers();            

            // set as a request attribute
						request.setAttribute("users", users);
        } 
        else if (action.equals("display_user")) {
            // get user for specified email
						String email = request.getParameter("email");
            User user = UserDB.selectUser(email);
            // set as session attribute
            session.setAttribute("user", user);
            url = "/user.jsp";
        }
        else if (action.equals("update_user")) {
            // update user in database
						String first = request.getParameter("firstName");
						String last = request.getParameter("lastName");
						User user = (User) session.getAttribute("user");
						user.setFirstName(first);
						user.setLastName(last);
						UserDB.update(user);
            // get current user list and set as request attribute
						ArrayList<User> users = UserDB.selectUsers();            
            request.setAttribute("users", users);     
        }
        else if (action.equals("delete_user")) {
            // get the user for the specified email
						String email = request.getParameter("email");
            User user = UserDB.selectUser(email);
            // delete the user      
						UserDB.delete(user);
            // get current list of users
						ArrayList<User> users = UserDB.selectUsers(); 
            // set as request attribute
						request.setAttribute("users", users);
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }    
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}
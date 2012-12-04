package com.amazonaws.cri.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.cri.AwsCriContext;

public class AwsCriServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        context.setAttribute("sqsmanager", AwsCriContext.get("sqsmanager"));
        context.setAttribute("dynamodbmanager", AwsCriContext.get("dynamodbmanager"));    	
    }
}

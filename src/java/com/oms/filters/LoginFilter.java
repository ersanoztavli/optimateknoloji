/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.filters;
import com.oms.models.KULLANICI;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author ersan
 */
public class LoginFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURI();
        
        KULLANICI user = (KULLANICI) req.getSession().getAttribute("validUser");
        
        /*
        // Get the loginBean from session attribute
        SessionBean sessionBean = (SessionBean)((HttpServletRequest)request).getSession().getAttribute("sessionBean");
        // For the first application request there is no loginBean in the session so user needs to log in
        // For other requests loginBean is present but we need to check if user has logged in successfully
        if (sessionBean == null || !sessionBean.isLogged) {
            String contextPath = ((HttpServletRequest)request).getContextPath();
            ((HttpServletResponse)response).sendRedirect(contextPath + "/admin/login.jsf");
        }         
        chain.doFilter(request, response);
        */
        //Eğer Oturum Açılmamışsa login sayfasına yönlendiriyoruz...
        if(user == null)
        {
            String contextPath = req.getContextPath();
            res.sendRedirect(contextPath + "/admin/login.jsf");
        }
        //Oturum açılmışsa istenen sayfaya yönleniyoruz...
        else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
    
}

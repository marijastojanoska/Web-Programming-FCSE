package mk.ukim.finki.wp.eshop.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.eshop.model.User;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@WebFilter(filterName = "auth-filter", urlPatterns = "/*",
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD},
        initParams = {
            @WebInitParam(name = "login-path", value = "/login"),
            @WebInitParam(name = "register-path", value = "/register")
        })
@Profile("servlet")
public class LoginFilter implements Filter {

    private String loginPath;
    private String registerPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        loginPath = filterConfig.getInitParameter("login-path");
        registerPath = filterConfig.getInitParameter("register-path");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");
        String path = request.getServletPath();
        if (path.startsWith(loginPath) || path.startsWith(registerPath) || user != null) {
            System.out.println("WebFilter preprocessing...");
            filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("WebFilter postprocessing...");
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

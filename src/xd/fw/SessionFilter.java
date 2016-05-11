package xd.fw;

import xd.fw.action.BaseAction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {

    public void init(FilterConfig filterconfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletrequest,
                         ServletResponse resp, FilterChain filterchain)
            throws IOException, ServletException {

        String uri = ((HttpServletRequest) servletrequest).getRequestURI();
            if (!uri.endsWith("user!userLogin.cmd") && BaseAction.currentUser() == null) {
                HttpServletResponse response = (HttpServletResponse) resp;
                response.setHeader("Cache-Control", "no-store");
                response.setDateHeader("Expires", 0);
                response.setHeader("Prama", "no-cache");
                response.sendRedirect("/");
                return;
        }
        filterchain.doFilter(servletrequest, resp);
    }

    @Override
    public void destroy() {
    }
}

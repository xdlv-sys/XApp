package xd.fw;

import xd.fw.FwUtil;
import xd.fw.action.BaseAction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {

    String[] whiteNames;
    public void init(FilterConfig filterconfig) throws ServletException {
        whiteNames = filterconfig.getInitParameter("whiteNames").split(",");
    }

    boolean accept(String uri){
        for (String wn : whiteNames){
            if (uri.endsWith(wn)){
                return true;
            }
        }
        return false;
    }

    public void doFilter(ServletRequest servletrequest,
                         ServletResponse resp, FilterChain filterchain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) servletrequest).getRequestURI();
        if (!accept(uri) &&
            ((HttpServletRequest) servletrequest).getSession().getAttribute(BaseAction.USER) == null){
            HttpServletResponse response = (HttpServletResponse) resp;
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            response.setHeader("Prama", "no-cache");
            response.sendRedirect("result.cmd?needLogin=true");
            return;
        }
        filterchain.doFilter(servletrequest, resp);
    }

    @Override
    public void destroy() {
    }
}

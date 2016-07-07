package xd.fw;

import org.apache.log4j.Logger;
import xd.fw.FwUtil;
import xd.fw.action.BaseAction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class SessionFilter implements Filter {

    static Logger logger = Logger.getLogger(SessionFilter.class);

    String[] whiteNames;
    boolean showParameters = false;
    public void init(FilterConfig filterconfig) throws ServletException {
        whiteNames = filterconfig.getInitParameter("whiteNames").split(",");
        showParameters = Boolean.valueOf(filterconfig.getInitParameter("showParameters"));
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
        if (showParameters){
            StringBuilder builder = new StringBuilder(uri).append(":\n");
            Map parameterMap = servletrequest.getParameterMap();
            for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                builder.append(name).append("=");
                Object value = parameterMap.get(name);
                if (value instanceof String[]){
                    builder.append(Arrays.toString((String[])value));
                } else {
                    builder.append(value);
                }
                builder.append(",");
            }
            logger.info(builder.toString());
        }
        filterchain.doFilter(servletrequest, resp);
    }

    @Override
    public void destroy() {
    }
}

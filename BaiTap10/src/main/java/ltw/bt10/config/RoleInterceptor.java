package ltw.bt10.config;

import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String uri = req.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (uri.startsWith("/admin") && (auth==null || auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_ADMIN")))) {
            res.sendRedirect("/login");
            return false;
        }
        if (uri.startsWith("/user") && (auth==null || auth.getAuthorities().isEmpty())) {
            res.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
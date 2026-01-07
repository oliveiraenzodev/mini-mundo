package br.com.minimundo.web.filter;

import br.com.minimundo.domain.auth.JwtUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    final String uri = request.getRequestURI();
    final String ctx = request.getContextPath();

    final boolean isResource = uri.startsWith(ctx + "/javax.faces.resource/");
    final boolean isAuthPage = uri.startsWith(ctx + "/pages/auth/");

    if (isResource || isAuthPage) {
      chain.doFilter(req, res);
      return;
    }

    HttpSession session = request.getSession(false);
    String token = (session != null) ? (String) session.getAttribute("JWT_TOKEN") : null;

    if (token == null || token.trim().isEmpty()) {
      response.sendRedirect(ctx + "/pages/auth/login.xhtml");
      return;
    }

    try {
      JwtUtil.validate(token);
      chain.doFilter(req, res);
    } catch (Exception e) {
      if (session != null) {
        session.removeAttribute("JWT_TOKEN");
      }
      response.sendRedirect(ctx + "/pages/auth/login.xhtml");
    }
  }

  @Override
  public void destroy() {
  }
}

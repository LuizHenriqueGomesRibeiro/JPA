package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

@WebFilter(urlPatterns = {"/*"})
public class FilterAut implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");
		String url = req.getServletPath();					  
		
		if(!url.equalsIgnoreCase("index.jsf") && usuarioLogado == null) {
			RequestDispatcher dispache = request.getRequestDispatcher("/index.jsf");
			dispache.forward(request, response);
			return;
		}else {
			chain.doFilter(request, response);
			
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		JPAUtil.getEntityManager();
	}

}

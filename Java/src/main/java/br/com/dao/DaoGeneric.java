package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> {
	public void salvar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transacao = entityManager.getTransaction();
		
		transacao.begin();
		entityManager.persist(entidade);
		transacao.commit();
		entityManager.close();
	}
	
	public E merge(E entidade) {
		
		EntityManager gerenciador = JPAUtil.getEntityManager();
		EntityTransaction transacao = gerenciador.getTransaction();

		transacao.begin();
		E retorno = gerenciador.merge(entidade);
		transacao.commit();
		gerenciador.close();
		
		return retorno;
	}
	
	public void delete(E entidade) {
		
		EntityManager gerenciador = JPAUtil.getEntityManager();
		EntityTransaction transacao = gerenciador.getTransaction();
		
		transacao.begin();
		Object id = JPAUtil.getPrimaryKey(entidade);
		gerenciador.createQuery("DELETE FROM " + entidade.getClass().getCanonicalName() + " WHERE id = " + id).executeUpdate();		
		transacao.commit();
		gerenciador.close();
	}
	
	public List<E> getListEntity(E entidade){
		
		EntityManager gerenciador = JPAUtil.getEntityManager();
		EntityTransaction transacao = gerenciador.getTransaction();
		
		@SuppressWarnings("unchecked")
		List<E> retorno = gerenciador.createQuery("FROM " + entidade.getClass().getCanonicalName()).getResultList();
		transacao.begin();
		transacao.commit();
		gerenciador.close();
		
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String id) {
		EntityManager gerenciador = JPAUtil.getEntityManager();
		EntityTransaction transacao = gerenciador.getTransaction();
		transacao.begin();
		E objeto = (E) gerenciador.find(entidade, Long.parseLong(id));
		transacao.commit();
		return objeto;
	}
}

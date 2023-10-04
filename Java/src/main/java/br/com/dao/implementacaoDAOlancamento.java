package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUtil;

public class implementacaoDAOlancamento implements IDdaoLancamento{
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> consultar(Long codUser){
		
		List<Lancamento> lancamento = null;
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		lancamento = entityManager.createQuery(" FROM Lancamento WHERE usuario.id = " + codUser).getResultList();
		transaction.commit();
		entityManager.close();
		
		return lancamento;
	}
}
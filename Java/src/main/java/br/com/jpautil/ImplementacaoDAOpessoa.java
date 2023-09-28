package br.com.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Pessoa;

public class ImplementacaoDAOpessoa implements IDdaoPessoa {
	
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoa = null;
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transacao = entityManager.getTransaction();
		
		transacao.begin();
		pessoa = (Pessoa) entityManager.createQuery("SELECT p FROM Pessoa p WHERE p.login = '" + login + "' AND p.senha = '" + senha + "'").getSingleResult();	
		transacao.commit();
		entityManager.close();
		
		return pessoa;
	}
}

package br.com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

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

	@Override
	public List<SelectItem> listarEstados() {
		
		List<SelectItem> selectItens = new ArrayList<SelectItem>();
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transacao = entityManager.getTransaction();
		
		List<Estados> estados = entityManager.createQuery("FROM Estados").getResultList();
		
		for (Estados estado : estados) {
			selectItens.add(new SelectItem(estado.getId(), estado.getNome()));
		}
				
		return selectItens;
	}
}

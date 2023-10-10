package br.com.dao;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entidades.Pessoa;

public interface IDdaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);
	
	List<SelectItem> listarEstados();
	
	
}

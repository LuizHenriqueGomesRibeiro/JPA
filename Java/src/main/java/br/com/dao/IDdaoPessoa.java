package br.com.dao;

import br.com.entidades.Pessoa;

public interface IDdaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);
}

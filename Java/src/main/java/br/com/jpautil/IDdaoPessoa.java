package br.com.jpautil;

import br.com.entidades.Pessoa;

public interface IDdaoPessoa {
	
	Pessoa consultarUsuario(String login, String senha);
}

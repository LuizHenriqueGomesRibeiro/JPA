package br.com.dao;

import java.util.List;

import br.com.entidades.Lancamento;

public interface IDdaoLancamento {
	List<Lancamento> consultar(Long codUser);
}

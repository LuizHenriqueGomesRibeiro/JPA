package br.com.Java;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.jpautil.IDdaoPessoa;
import br.com.jpautil.ImplementacaoDAOpessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDdaoPessoa daoPessoa = new ImplementacaoDAOpessoa();
	
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(pessoa);
	}
	
	public String salvar() {
		daoGeneric.salvar(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	
	public String merge() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		return "";
	}
	
	public String limpar() {
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	
	public String deletar() {
		daoGeneric.delete(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	
	public String logar() {
		Pessoa pessoaUser = daoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		if(pessoaUser != null) {
			return "primeiraPagina.jsf"; 
		}
		return "index.jsf";
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}
	
}

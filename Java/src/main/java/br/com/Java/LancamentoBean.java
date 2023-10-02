package br.com.Java;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.dao.implementacaoDAOlancamento;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {
	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	private implementacaoDAOlancamento daoLancamento = new implementacaoDAOlancamento();
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		lancamento.setId(null);
		lancamento.setUsuario(pessoa);
		daoGeneric.salvar(lancamento);
		carregarLancamentos();
		
		return "";
	}
	
	@PostConstruct
	public void carregarLancamentos() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		lancamentos = daoLancamento.consultar(pessoa.getId());
	}
	
	public String novo() {
		lancamento = new Lancamento();
		return "";
	}
	
	public String merge() {
		lancamento = daoGeneric.merge(lancamento);
		return "";
	}
	
	public String limpar() {
		lancamento = new Lancamento();
		return "";
	}
	
	public String deletar() {
		daoGeneric.delete(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
}

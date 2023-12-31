package br.com.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Size(min = 5, max = 20, message = "O nome deve ter entre cinco e vinte caracteres.")
	@NotEmpty(message = "Informe um nome válido.")
	@NotNull(message = "Informe um nome válido.")
	private String nome;
	@Size(min = 5, max = 20, message = "O sobrenome deve ter entre cinco e vinte caracteres.")
	@NotEmpty(message = "Informe um sobrenome válido.")
	@NotNull(message = "Informe um sobrenome válido.")
	private String sobrenome;
	@DecimalMin(value = "18", message = "O usuário deve ser maior de idade.")
	private Integer idade;
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	private Boolean ativo;
	@Size(min = 5, max = 20, message = "A senha deve ter entre cinco e vinte caracteres.")
	@NotEmpty(message = "Informe uma senha válida.")
	@NotNull(message = "Informe uma senha válida.")
	private String senha;
	@Size(min = 5, max = 20, message = "O login deve ter entre cinco e vinte caracteres.")
	@NotEmpty(message = "Informe um login válido.")
	@NotNull(message = "Informe um login válido.")
	private String login;
	private String perfil;
	private String sexo;
	private String nivel;
	private Integer[] linguagens;
	private String CEP;
	private String logradouro;
	private String bairro;
	private String localidade;
	private String uf;
	private String ibge;
	private String ddd;
	@Transient
	private Estados estados;
	@Column(columnDefinition = "text")
	private String fotoIconBase64;
	private String extensao;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBase64Original;
	
	public String getFotoIconBase64() {
		return fotoIconBase64;
	}
	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}
	public String getExtensao() {
		return extensao;
	}
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	public byte[] getFotoIconBase64Original() {
		return fotoIconBase64Original;
	}
	public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
		this.fotoIconBase64Original = fotoIconBase64Original;
	}
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public Estados getEstados() {
		return estados;
	}
	public void setEstados(Estados estados) {
		this.estados = estados;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getIbge() {
		return ibge;
	}
	public void setIbge(String ibge) {
		this.ibge = ibge;
	}
	public String getDDD() {
		return ddd;
	}
	public void setDDD(String ddd) {
		this.ddd = ddd;
	}
	public String getCEP() {
		return CEP;
	}
	public void setCEP(String cEP) {
		CEP = cEP;
	}
	public Integer[] getLinguagens() {
		return linguagens;
	}
	public void setLinguagens(Integer[] linguagens) {
		this.linguagens = linguagens;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Pessoa() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public Integer getIdade() {
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}
}

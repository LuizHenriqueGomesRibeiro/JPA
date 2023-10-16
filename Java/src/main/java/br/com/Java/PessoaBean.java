package br.com.Java;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.dao.IDdaoPessoa;
import br.com.dao.ImplementacaoDAOpessoa;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDdaoPessoa daoPessoa = new ImplementacaoDAOpessoa();
	private List<SelectItem> estados;
	private Part arquivofoto;
	
	public void carregaCidades(AjaxBehaviorEvent event) {
		
	}
	
	public String deslogar() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		httpServletRequest.getSession().invalidate();
		
		return "index.jsf";
	}
	
	public void pesquisaCEP(AjaxBehaviorEvent ajax) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCEP() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String CEP = "";
			StringBuilder json = new StringBuilder();
			
			while((CEP = br.readLine()) != null) {
				json.append(CEP);
			}
			
			Pessoa gson = new Gson().fromJson(json.toString(), Pessoa.class);
			
			pessoa.setLogradouro(gson.getLogradouro());
			pessoa.setBairro(gson.getBairro());
			pessoa.setLocalidade(gson.getLocalidade());
			pessoa.setUf(gson.getUf());
			pessoa.setIbge(gson.getIbge());
			pessoa.setDDD(gson.getDDD());
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mostrarMensagem("CEP inválido.");
		}
	}
	
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(pessoa);
	}
	
	public String salvar() throws IOException {
		
		byte[] imagem = null;
		if(arquivofoto != null) {
			imagem = getByte(arquivofoto.getInputStream());
		}
		
		if(imagem != null && imagem.length > 0) {
			pessoa.setFotoIconBase64Original(imagem);
			System.out.println(imagem);
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagem));
			
			int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
			int largura = 200;
			int altura = 200;
			
			BufferedImage resizedImage = new BufferedImage(largura, altura, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(resizedImage, 0, 0, largura, altura, null);
			g.dispose();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String extensao = arquivofoto.getContentType().split("\\/")[1];
			ImageIO.write(resizedImage, extensao, baos);
			String miniImagem = "data:" + arquivofoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
			System.out.println(miniImagem);
			pessoa.setFotoIconBase64(miniImagem);
			pessoa.setExtensao(extensao);
		}
		
		if(pessoa.getId() != null) {
			pessoa = daoGeneric.merge(pessoa);
			mostrarMensagem("Atualizado com sucesso.");	
		}else {
			daoGeneric.salvar(pessoa);
			mostrarMensagem("Cadastrado com sucesso.");
		}
		return "";
	}
	
	private void mostrarMensagem(String string) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(string);
		context.addMessage(null, message);
	}

	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String deletar() {
		daoGeneric.delete(pessoa);
		pessoa = new Pessoa();
		mostrarMensagem("Excluído com sucesso.");
		return "";
	}
	
	public String logar() {
		Pessoa pessoaUser = daoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if(pessoaUser != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();

			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();
			
			session.setAttribute("usuarioLogado", pessoaUser);
			
			String string = "Preencha os campos requiridos";
			
			FacesMessage message = new FacesMessage(string);
			context.addMessage(null, message);
			
			return "primeiraPagina.jsf"; 
		}
		return "index.jsf";
	}
	
	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		return pessoa.getPerfil().equals(acesso);
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
	public List<SelectItem> getEstados() {
		estados = daoPessoa.listarEstados();
		return estados;
	}
	public Part getArquivofoto() {
		return arquivofoto;
	}
	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}
	private byte[] getByte(InputStream inputstream) throws IOException {
	
		int length;
		int size = 1024;
		byte[] buf = null;
		
		if(inputstream instanceof ByteArrayInputStream) {
			size = inputstream.available();
			buf = new byte[size];
			length = inputstream.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((length = inputstream.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, length);
			}
			
			buf = bos.toByteArray();
		}
		
		return buf;
	}
}

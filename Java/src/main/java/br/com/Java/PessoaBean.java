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
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	
	public void mudancaValor(ValueChangeEvent evento) {
	}
	
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		externalContext.getSessionMap().remove("usuarioLogado");
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
			salvarImagem(imagem);
		}
		return salvarUsuario();
	}
	
	public String salvarUsuario() {
		if(pessoa.getId() != null) {
			pessoa = daoGeneric.merge(pessoa);
			mostrarMensagem("Atualizado com sucesso.");	
		}else {
			daoGeneric.salvar(pessoa);
			mostrarMensagem("Cadastrado com sucesso.");
		}
		return "primeiraPagina.jsf";
	}
	
	public void salvarImagem(byte[] imagem) throws IOException {
		pessoa.setFotoIconBase64Original(imagem);
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagem));
		salvarDimensoesImagem(bufferedImage);
	}
	
	public String salvarDimensoesImagem(BufferedImage bufferedImage) throws IOException {
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		int largura = 200;
		int altura = 200;
		return salvarRedimensionarImagem(type, bufferedImage, largura, altura);
	}
	
	public String salvarRedimensionarImagem(int type, BufferedImage bufferedImage, int largura, int altura) throws IOException {
		BufferedImage resizedImage = new BufferedImage(largura, altura, type);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(bufferedImage, 0, 0, largura, altura, null);
		graphics2D.dispose();
		return salvarImagemToByte(resizedImage);
	}
	
	public String salvarImagemToByte(BufferedImage resizedImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivofoto.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extensao, baos);
		return salvarMiniImagem(baos, extensao);
	}
	
	public String salvarMiniImagem(ByteArrayOutputStream baos, String extensao) {
		String miniImagem = "data:" + arquivofoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		return miniImagem;
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
		return logarAlternar(pessoaUser);
	}
	
	public String logarAlternar(Pessoa pessoaUser) {
		if(pessoaUser != null) {
			return logarContextos(pessoaUser);
		}else {
			return "index.jsf";
		}
	}
	
	public String logarContextos(Pessoa pessoaUser) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = ((HttpServletRequest) context.getExternalContext().getRequest()).getSession();
		return logarSets(context, session, pessoaUser);
	}
	
	public String logarSets(FacesContext context, HttpSession session, Pessoa pessoaUser) {
		session.setAttribute("usuarioLogado", pessoaUser);
		context.addMessage(null, new FacesMessage("Preencha os campos requiridos"));
		return "primeiraPagina.jsf";
	}
	
	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		return pessoa.getPerfil().equals(acesso);
	}
	
	public void download() throws IOException {
		//String fileDownload  = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fileDownload");
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownload = params.get("fileDownload");
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownload);
		downloadResponse((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(), pessoa);
	}
	
	public void downloadResponse(HttpServletResponse response, Pessoa pessoa) throws IOException {
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
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

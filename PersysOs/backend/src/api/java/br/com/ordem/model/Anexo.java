package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Session;

import com.sun.xml.bind.CycleRecoverable;

import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.interfaces.IAnexo;
import br.com.model.interfaces.IOrdem;
import br.com.ordem.dao.AnexoDAO;

@XmlRootElement
@Entity
@Table(name="anexo")
public class Anexo extends Model<Anexo> implements Serializable , CycleRecoverable ,IAnexo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "descricao")
	private String descricao = null; 

	@Column(name = "imagemCaminho")
	private String caminho = null; 

	@Column(name = "tamanho")
	private int tamanho; 

	@XmlTransient
	@Column(name = "imagem",length=100000)
	@Lob
	private byte[] imagem = null;


	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
	private Ordem ordem;

	public static String CONSTRUTOR = " (id,caminho,descricao,tamanho) ";
	
	public Anexo(Long id, String caminho, String descricao,
			int tamanho) {
		super(Anexo.class);
		setId(id);
		this.descricao = descricao;
		this.caminho = caminho;
		this.tamanho = tamanho;
	}

	public Anexo() {
		super();
	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the caminho
	 */
	public String getCaminho() {
		return caminho;
	}

	/**
	 * @param caminho the caminho to set
	 */
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	/**
	 * @return the tamanho
	 */
	public int getTamanho() {
		return tamanho;
	}

	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	/**
	 * @return the ordem
	 */
	public IOrdem getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(IOrdem ordem) {
		this.ordem = (Ordem) ordem;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	@Override
	public void valida() throws ModelException {
		setCodigo("PANX"+countPorConsumerSecret(Anexo.class,getConsumerSecret().getConsumerKey()));
	}

	public static ArrayList<Anexo> listaOrdemAnexo(Session session, String consumerKey, Long idOrdem) throws ModelException {
		AnexoDAO anexoDao = new AnexoDAO(Anexo.class);
		try {
			return (ArrayList<Anexo>) anexoDao.listaOrdemAnexo(Long.parseLong(consumerKey), idOrdem);
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public static byte[] getImagemAnexo(Session session, String cs, Long idAnexo) {
		AnexoDAO anexoDao = new AnexoDAO(Anexo.class);
		return anexoDao.getImagemAnexo(session, cs, idAnexo);
	}


	@Override
	public Object onCycleDetected(Context arg0) {
		Anexo anexo = new Anexo();
		return anexo;
	}
}

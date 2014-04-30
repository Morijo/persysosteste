package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Session;

import com.sun.xml.bind.CycleRecoverable;

import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.INota;
import br.com.model.interfaces.IOrdem;
import br.com.ordem.dao.NotaDAO;
import br.com.principal.helper.FormatDateHelper;
import br.com.usuario.model.Usuario;


@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="nota")
public class Nota extends Model<Nota> implements Serializable, INota,CycleRecoverable  {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String CONSTRUTOR = "id,titulo,nota";
	
	@Column(name = "titulo", length=100)
	private String titulo = null; 
	
	@Column(name = "nota", length = 1000)
	private String nota = null; 
	
	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
    private Ordem ordem;
	
	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id")
	})
    private Usuario usuario = null;

	public Nota(Long id, Date dataCriacao, Date dataAlteracao, String titulo,
			String nota, Ordem ordem, Funcionario funcionario) {
		super();
		setId(id);
		setDataAlteracao(dataAlteracao);
		setDataCriacao(dataCriacao);
		this.titulo = titulo;
		this.nota = nota;
		this.ordem = ordem;
		this.usuario = funcionario;
	}
	
	public Nota(Long id, String titulo, String nota) {
		super();
		setId(id);
		this.titulo = titulo;
		this.nota = nota;
	}
	
	public Nota() {}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @return the ordem
	 */
	public Ordem getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(IOrdem ordem) {
		this.ordem = (Ordem) ordem;
	}

	/**
	 * @return the funcionario
	 */
	public Usuario getUsuario() {
		if(usuario== null){
			usuario = new Usuario();
		}
		return usuario;
	}

	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Nota nota = new Nota();
		return nota;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nota, "invalid parameter");
		setCodigo("PPRI"+countPorConsumerSecret(Nota.class,getConsumerSecret().getConsumerKey()));
	}
	
	public static ArrayList<Nota> listaNota(Long idOrdem){
		NotaDAO notaDAO = new NotaDAO();
		return (ArrayList<Nota>) notaDAO.listaNotas(idOrdem);
	}
	
	public static Nota criarNota(Nota nota, Session session, Usuario usuario,
			Ordem ordemServico, String consumerKey) throws ModelException {
		nota.setConsumerId(Long.parseLong(consumerKey));
		nota.setTitulo("Nota Criada "+ FormatDateHelper.formatTimeZoneUSToBR(new Date().getTime()) +" por: "+ usuario.getRazaoNome());
		nota.setUsuario(usuario);
		nota.setOrdem(ordemServico);
		nota.salvar(session);
		return nota;
	}
	
}

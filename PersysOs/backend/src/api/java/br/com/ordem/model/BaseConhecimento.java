package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sun.xml.bind.CycleRecoverable;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IBaseConhecimento;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;

/**
 * <p> O modelo Base de Conhecimento eh composto pelos seguintes campos </p>
 * <p>codigo 			(String)(255),Codigo para conhecimento da base, (PBSC+Numero dela)</p>
 * <p>mensagem 			(String) Tamanho 1000 caracteres,Mensagem para a base de conhecimento</p>
 * <p>tipo 				(String) Tamanho 20 caracteres,Campo Obrigatorio, Tipo da base referente a interno e externo</p>
 * <p>titulo 			(String) tamanho 100 caracteres,Campo Obrigatorio, Nome da base de conhecimento</p>
 * <p>BaseConhecimento Lista a base de conhecimento ja cadastrada no sistema </p>
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/backend/model</p>
 * <br>Tabela no banco: baseconhecimento </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @since 27/03/2013
 * @see Ordem
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="baseconhecimento")
public class BaseConhecimento extends Model<BaseConhecimento> implements Serializable, CycleRecoverable, IBaseConhecimento{

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "titulo", length=100)
	private String titulo = null; 
	
	@Column(name = "tipo", length=20)
	private String tipo = null; 
	
	@Column(name = "mensagem", length=1000)
	private String mensagem = null;
	
	public BaseConhecimento() {
		super();
	}

	public static final String CONSTRUTOR = "id,codigo,statusModel,titulo,tipo";
	
	public BaseConhecimento(Long id, String codigo,Integer statusModel, String titulo,String tipo) {
		super();
		setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		this.titulo = titulo;
		this.tipo = tipo;
	}
	
	public BaseConhecimento(Long id, String codigo,Integer statusModel, String titulo,String tipo,String mensagem) {
		super();
		setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		this.titulo = titulo;
		this.tipo = tipo;
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return mensagem;
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(mensagem, bundle.getString("fulltextinvalid"));
		PreconditionsModel.checkEmptyString(titulo, bundle.getString("invalidtitle"));
		if(getCodigo().isEmpty()){
			setCodigo("PBSC"+countPorConsumerSecret(BaseConhecimento.class,getConsumerSecret().getConsumerKey()));
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		BaseConhecimento baseConhecimento = new BaseConhecimento();
		return baseConhecimento;
	}
	
	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<BaseConhecimento> busca(String consumerKey, String titulo, String mensagem, String codigo,
			Integer statusModel) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			if(titulo.length() >= 3){
				parameter.add(ParameterDAO.with("titulo","%"+titulo+"%",ParameterDAOHelper.ILIKE));
			}else if(mensagem.length() >= 1){
				parameter.add(ParameterDAO.with("mensagem","%"+mensagem+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsModel.checkNotNull(null, "Erro");
			}
			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			ArrayList<BaseConhecimento> baseConhecimentoLista =(ArrayList<BaseConhecimento>) BaseConhecimento.pesquisaListaPorConsumerSecret(session,BaseConhecimento.class,consumerKey, parameter);
			tx.commit();
			return baseConhecimentoLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseConhecimento other = (BaseConhecimento) obj;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		return true;
	}
}

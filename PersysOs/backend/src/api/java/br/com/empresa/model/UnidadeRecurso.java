package br.com.empresa.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.model.Model;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="unidaderecurso")
public class UnidadeRecurso extends Model<UnidadeRecurso>{

	@Column(name = "valoraquisicao")
	private BigDecimal valoraquisicao = new BigDecimal("0.00");

	@Column(name = "valorordem")
	private BigDecimal valorordem  = new BigDecimal("0.00");

	@Column(name = "quantidade")
	private BigDecimal quantidade  = new BigDecimal("0");

	@Column(name = "dataaquisicao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAquisicao = new Date();
	
	@Column(name = "tempogarantia")
	private Integer tempoGarantia;

	@Column(name = "notafiscal", nullable=false, length=100)
	private String notaFiscal = "";
	
	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "unidadeidpk", referencedColumnName="id"),
	})
	
    private Unidade unidade;

	public UnidadeRecurso (){
		super(UnidadeRecurso.class);
	}

	
	@Override
	public void valida() {
		// TODO Auto-generated method stub
		
	}
	

}
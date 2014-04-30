package br.com.rest.represention;

import java.io.Serializable;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import br.com.rest.resources.helper.ParameterRequestRest;

import com.sun.xml.bind.CycleRecoverable;


@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class LinkData implements Serializable , CycleRecoverable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	protected String next = null;
		
	@XmlAttribute
	protected  String previous = null;
	
	@XmlAttribute
	protected  Long numberPages = null;
	
	@XmlAttribute
	protected  Long number = null;
	
	@XmlAttribute
	protected  Long page = null;
	
	
	@XmlAttribute
	protected String type;
		
	public LinkData() {
		type = "application/json";
	}
		
	public LinkData(String type) {
	   this.type = type;
	}

	public static LinkData createLinks(String caminho, Integer tamanhoPagina, Integer inicio, Number numeroInfo, Integer status) {
		  
		LinkData links = new LinkData();
		double numeroInfoDouble = numeroInfo.doubleValue();
		double tamanhoPaginaDouble = tamanhoPagina;
		
		// Arrendondamento para cima, para fornecer o número certo de páginas
		Long numeroPaginas = (long) Math.ceil(numeroInfoDouble/ tamanhoPaginaDouble);
	
		// O resultado da divisão será um int.
		Long paginaAtual;
		try{
			paginaAtual = new Long(inicio / tamanhoPagina);
		}catch (Exception e) {
			paginaAtual = 0l;
		}
		
		links.numberPages = numeroPaginas;
		links.page = paginaAtual + 1;
		links.number = numeroInfo.longValue();

		if(paginaAtual+1 < numeroPaginas){
			links.next = UriBuilder.fromPath(caminho)
						   .queryParam(ParameterRequestRest.OFFSET, inicio+tamanhoPagina)
			               .queryParam(ParameterRequestRest.LIMIT, tamanhoPagina)
			               .queryParam(ParameterRequestRest.STATUS, status).build()
			               .toString();
		}
	
		if(paginaAtual >= 1){
			links.previous = UriBuilder.fromPath(caminho)
				               .queryParam(ParameterRequestRest.OFFSET, Math.max(inicio - tamanhoPagina,0))
				               .queryParam(ParameterRequestRest.LIMIT, tamanhoPagina)
				               .queryParam(ParameterRequestRest.STATUS, status).build()
			                   .toString();
		}	
		return links;
	}
	
	public static LinkData createLinks(String caminho, Integer tamanhoPagina, Integer inicio, Number numeroInfo) {
		  
		LinkData links = new LinkData();
		double numeroInfoDouble = numeroInfo.doubleValue();
		double tamanhoPaginaDouble = tamanhoPagina;
		
		// Arrendondamento para cima, para fornecer o número certo de páginas
		Long numeroPaginas = (long) Math.ceil(numeroInfoDouble/ tamanhoPaginaDouble);
	
		// O resultado da divisão será um int.
		Long paginaAtual;
		try{
			paginaAtual = new Long(inicio / tamanhoPagina);
		}catch (Exception e) {
			paginaAtual = 0l;
		}
		
		links.numberPages = numeroPaginas;
		links.page = paginaAtual + 1;
		links.number = numeroInfo.longValue();

		if(paginaAtual+1 < numeroPaginas){
			links.next = UriBuilder.fromPath(caminho)
						   .queryParam(ParameterRequestRest.OFFSET, inicio+tamanhoPagina)
			               .queryParam(ParameterRequestRest.LIMIT, tamanhoPagina).build()
			               .toString();
		}
	
		if(paginaAtual >= 1){
			links.previous = UriBuilder.fromPath(caminho)
				               .queryParam(ParameterRequestRest.OFFSET, Math.max(inicio - tamanhoPagina,0))
				               .queryParam(ParameterRequestRest.LIMIT, tamanhoPagina).build()
			                   .toString();
		}	
		return links;
	}
	
	public static LinkData criarLinks(Number number) {
		  
		LinkData links = new LinkData();
		links.numberPages = 1l;
		links.page = 1l;
		links.number = number.longValue();
		return links;
	}
	
	public static LinkData createLinks() {
		  
		LinkData links = new LinkData();
		links.numberPages = 1l;
		links.page = 1l;
		return links;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
		
	

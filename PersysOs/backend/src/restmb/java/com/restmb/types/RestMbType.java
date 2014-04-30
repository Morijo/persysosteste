package com.restmb.types;

import java.io.InputStream;
import java.util.Date;

import br.com.model.interfaces.IModel;

import com.restmb.Connection;
import com.restmb.DefaultJsonMapper;
import com.restmb.Parameter;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;
import com.restmb.exception.RestMBJsonMappingException;
import com.restmb.json.JsonException;
import com.restmb.oauth.service.ParameterList;

public class RestMbType<T> implements REST<T>, IModel{

	 @RestMB("id")
	 private Long id = null;
	 
	 @RestMB("dataAlteracao")
	 private Date dataAlteracao;
	 
	 @RestMB("dataCriacao")
	 private Date dataCriacao;

	 @RestMB("codigo")
	 private String codigo = "";
	
	 @RestMB("statusModel")
	 private Integer statusModel = 1; //{1: ativo, 0 inativo, 2 deletado}
	 
	 @RestMB("permitidoExcluir")
	 private Boolean permitidoExcluir = true;
	 
	 @RestMB("permitidoAlterar")
	 private Boolean permitidoAlterar = true;
	 
	 protected String resourcePath;
	
	 protected Class<T> paClass;
	 
	 public RestMbType(String resourcePath, Class<T> paClass){
		 this.resourcePath = resourcePath;
		 this.paClass = paClass;
	 }
	 
	 public RestMbType(){}
	  
	 public Long getId() {
		return id;
	 }
	 public void setId(Long id) {
		this.id = id;
	 }
	 public Date getDataAlteracao() {
		return dataAlteracao;
	 }
	 public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	 }
	 public Date getDataCriacao() {
		return dataCriacao;
	 }
	 public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	 }

	 public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public Integer getStatusModel() {
		return statusModel;
	}

	public void setStatusModel(Integer statusModel) {
		this.statusModel = statusModel;
	}

	public Boolean getPermitidoExcluir() {
		return permitidoExcluir;
	}

	public void setPermitidoExcluir(Boolean permitidoExcluir) {
		this.permitidoExcluir = permitidoExcluir;
	}

	public Boolean getPermitidoAlterar() {
		if(permitidoAlterar == null)
			return true;
		return permitidoAlterar;
	}

	public void setPermitidoAlterar(Boolean permitidoAlterar) {
		this.permitidoAlterar = permitidoAlterar;
	}
	
	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Class<T> getPaClass() {
		return paClass;
	}

	public void setPaClass(Class<T> paClass) {
		this.paClass = paClass;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Connection<T> listaAll(RestMBClient cliente, String resourcePath, Class<?> classPar, Parameter... p){
		try{
			Connection<?> lista = cliente.fetchConnection(resourcePath, classPar ,"data",p);
			return (Connection<T>) lista;
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado",e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Connection<T> lista(RestMBClient client, Parameter... p){
		try{
			Connection<?> lista = client.fetchConnection(resourcePath, paClass ,"data",p);
			return (Connection<T>) lista;
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}

	 
	@Override
	public T pesquisa(RestMBClient client, Long id) throws RestMBGraphException{
		try{
			return (T) client.fetchObject(resourcePath+"/"+id, paClass);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T pesquisa(RestMBClient client) {
		try{
			return (T) client.fetchObject(resourcePath, paClass);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	public static <T> T pesquisa(RestMBClient client,String resourcePath,Class<T> classPar, Long id) {
		try{
			return client.fetchObject(resourcePath+"/"+id, classPar);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	public static <T> T pesquisa(RestMBClient cliente, String resourcePath,Class<T> classPar) {
		try{
			return cliente.fetchObject(resourcePath, classPar);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	public static <T> T pesquisa(RestMBClient cliente, String resourcePath,Class<T> classPar, Parameter... p) {
		try{
			return cliente.fetchObject(resourcePath, classPar, p);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	
	public static InputStream pesquisaImagem(RestMBClient client, String resourcePath,Parameter... p) throws RestMBGraphException{
		try{
			return client.fetchImage(resourcePath,p);
		}catch (JsonException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}catch (RestMBGraphException e) {
			throw new RestMBGraphException("Sem Resultado", e.getMessage(), 0);
		}
	}
	
	@Override
	public T salvar(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return (T) client.publish(resourcePath,paClass,json.toJson(this),headers);
	}
	
	@Override
	public T alterar(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return (T) client.publishChanges(resourcePath+"/"+this.getId(),paClass,json.toJson(this),headers);
	}
	
	@Override
	public T alterarHome(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return (T) client.publishChanges(resourcePath,paClass,json.toJson(this),headers);
	}

	@Override
	public boolean remover(RestMBClient client) {
		try{
			return client.deleteObject(resourcePath+"/"+this.id);
		}catch (RestMBGraphException e) {
			return false;
		}
	}

	@Override
	public boolean remover(RestMBClient client, Long id) {
		try{
		    return client.deleteObject(resourcePath,id);
		}catch (RestMBGraphException e) {
			return false;
		}
	}
	
	public static boolean deletar(RestMBClient client, String resourcePath, Long id){
		try{
		  return client.deleteObject(resourcePath,id);
		}catch (RestMBGraphException e) {
			return false;
		}
	}
	
	@Override
	public boolean removerHome(RestMBClient client) {
		try{
			  return client.deleteObject(resourcePath);
		}catch (RestMBGraphException e) {
				return false;
		}
	}
	
	public String toJson(){
		try{
			DefaultJsonMapper json = new DefaultJsonMapper();
			return json.toJson(this);
		}catch (RestMBJsonMappingException e) {
			return null;
		}
	}
}

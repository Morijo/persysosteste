package com.persys.osmanager.data;

import br.com.model.interfaces.IStatus;

import com.restmb.types.StatusBoolean;
import com.restmb.types.Status;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

/**
 * 
 * @author ricardosabatine
 *@version v1.0.0
 *@since 10/10/2013
 *
 *Constante em listas para utilizar nos combobox
 */
public class AppData {

	 /**
	 * Retorna uma lista vinculando inteiro para os status ativo e inativo 
	 * 1 - Ativo
	 * 0 - Inativo
	 * @return BeanContainer
	 */
	public static BeanContainer<Integer, IStatus> listStatusInteger(){
		 BeanContainer<Integer, IStatus> beans =
			        new BeanContainer<Integer, IStatus>(IStatus.class);
			    
		beans.setBeanIdProperty("status");
		beans.addBean(new Status(1, "Ativo")); 
		beans.addBean(new Status(0, "Inativo")); 
		 
		return beans;
	}
	
	 
	 /**
	 * Retorna uma lista com os status ativo e inativo vinculados com true e false
	 * @return BeanContainer
	 */
	 public static BeanContainer<Boolean, StatusBoolean> listStatusBoolean(){
		 BeanContainer<Boolean, StatusBoolean> beans =
			        new BeanContainer<Boolean, StatusBoolean>(StatusBoolean.class);
			    
		beans.setBeanIdProperty("status");
		beans.addBean(new StatusBoolean(true, "Ativo")); 
		beans.addBean(new StatusBoolean(false, "Inativo")); 
		 
		return beans;
	}
	 
	 /**
	  * Retorna os tipos de contatos
	  * Comercial
	  * Técnico
	  * Administrativo
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listStatusContato(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Comercial"); 
		beans.addBean("Técnico"); 
		beans.addBean("Administrativo"); 
		beans.addBean("Social"); 
		return beans;
	}
	 
	 /**
	  * Retorna os estados do brasil
	  * Acre
	  * São Paulo
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listStates(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("AC");//Acre		
		beans.addBean("AL");//Alagoas		
		beans.addBean("AP");//Amapá		
		beans.addBean("AM");//Amazonas		
		beans.addBean("BA");//Bahia		
		beans.addBean("CE");//Ceará		
		beans.addBean("DF");//Distrito Federal		
		beans.addBean("ES");//Espírito Santo		
		beans.addBean("GO");//Goiás		
		beans.addBean("MA");//Maranhão		
		beans.addBean("MT");//Mato Grosso		
		beans.addBean("MS");//Mato Grosso do Sul		
		beans.addBean("MG");//Minas Gerais		
		beans.addBean("PA");//Pará		
		beans.addBean("PB");//Paraíba		
		beans.addBean("PR");//Paraná		
		beans.addBean("PE");//Pernambuco		
		beans.addBean("PI");//Piauí	
		beans.addBean("RJ");//Rio de Janeiro		
		beans.addBean("RN");//Rio Grande do Norte		
		beans.addBean("RS");//Rio Grande do Sul		
		beans.addBean("RO");//Rondônia		
		beans.addBean("RR");//Roraima		
		beans.addBean("SC");//Santa Catarina		
		beans.addBean("SP");//São Paulo		
		beans.addBean("SE");//Sergipe		
		beans.addBean("TO");//Tocantins
		return beans;
	}
}

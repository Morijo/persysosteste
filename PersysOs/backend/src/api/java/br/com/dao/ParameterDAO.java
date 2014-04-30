package br.com.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public final class ParameterDAO {

  public  String  name = null;
  public  Integer op = null;
  public  Object  value = null;

  private ParameterDAO(String name, Object value, Integer op) {
   
	  if (name.length()==0 || value == null)
      throw new IllegalArgumentException(ParameterDAO.class + " instances must have a non-blank name and non-null value.");

	  this.name = name;
	  this.value = value;
	  this.op = op;
  }
 
  public static ParameterDAO with(String name, Object value, Integer op) {
    return new ParameterDAO(name, value, op);
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!getClass().equals(obj.getClass()))
      return false;

    ParameterDAO other = (ParameterDAO) obj;

    if (this.name != other.name && (!this.name.equals(other.name)))
      return false;
    if (this.value != other.value && (!this.value.equals(other.value)))
      return false;

    return true;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + this.name.hashCode();
    hash = 41 * hash + this.value.hashCode();
    return hash;
  }
  /**
   * Classe que define as opções de operação.
   * @author ricardosabatine
   *
   *ILIKE - 1 (Aplique um Entre %)
   *EQ - 2 (Aplique um Igual)
   *LE - 3 (Aplique um "Menor ou igual)
   *GE - 4 (Aplique um "Maior ou igual")
   *LT - 5 (Aplique um "menor que" restrição)
   *GT - 5 (Aplique um "maior que" restrição)
   */
  public static class ParameterDAOHelper {
	  /**
	   * ILIKE (Aplique um Entre %)
	   */
	  public static final Integer ILIKE = 1;
	  /**
	   * Classe que define as opções de operação.
	   * @author ricardosabatine
	   *
	   *ILIKE - 1 (Aplique um Entre %)
	   *EQ - 2 (Aplique um Igual)
	   *LE - 3 (Aplique um "Menor ou igual)
	   *GE - 4 (Aplique um "Maior ou igual")
	   *LT - 5 (Aplique um "menor que" restrição)
	   *GT - 5 (Aplique um "maior que" restrição)
	   */
	  public static final Integer EQ    = 2;
	  /**
	   * Classe que define as opções de operação.
	   * @author ricardosabatine
	   *
	   *ILIKE - 1 (Aplique um Entre %)
	   *EQ - 2 (Aplique um Igual)
	   *LE - 3 (Aplique um "Menor ou igual)
	   *GE - 4 (Aplique um "Maior ou igual")
	   *LT - 5 (Aplique um "menor que" restrição)
	   *GT - 5 (Aplique um "maior que" restrição)
	   */
	  public static final Integer LE    = 3;
	  /**
	   * Classe que define as opções de operação.
	   * @author ricardosabatine
	   *
	   *ILIKE - 1 (Aplique um Entre %)
	   *EQ - 2 (Aplique um Igual)
	   *LE - 3 (Aplique um "Menor ou igual)
	   *GE - 4 (Aplique um "Maior ou igual")
	   *LT - 5 (Aplique um "menor que" restrição)
	   *GT - 5 (Aplique um "maior que" restrição)
	   */
	  public static final Integer GE    = 4;
	  /**
	   * Classe que define as opções de operação.
	   * @author ricardosabatine
	   *
	   *ILIKE - 1 (Aplique um Entre %)
	   *EQ - 2 (Aplique um Igual)
	   *LE - 3 (Aplique um "Menor ou igual)
	   *GE - 4 (Aplique um "Maior ou igual")
	   *LT - 5 (Aplique um "menor que" restrição)
	   *GT - 5 (Aplique um "maior que" restrição)
	   */
	  public static final Integer LT    = 5;
	  /**
	   * Classe que define as opções de operação.
	   * @author ricardosabatine
	   *
	   *ILIKE - 1 (Aplique um Entre %)
	   *EQ - 2 (Aplique um Igual)
	   *LE - 3 (Aplique um "Menor ou igual)
	   *GE - 4 (Aplique um "Maior ou igual")
	   *LT - 5 (Aplique um "menor que" restrição)
	   *GT - 5 (Aplique um "maior que" restrição)
	   */
	  public static final Integer GT    = 5;
  }
  
  public static void createRestrictions(Criteria criteria, ParameterDAO... parameters){
	  for (ParameterDAO parameterDao : parameters){
		  criteria.add(addRestrictions(parameterDao));
	  }
	}
  
  public static void createRestrictions(Criteria criteria, ArrayList<ParameterDAO> parameters){
	  for (ParameterDAO parameterDao : parameters){
		  criteria.add(addRestrictions(parameterDao));
	  }
	}
  
  private static Criterion addRestrictions(ParameterDAO parameter){

	  switch (parameter.op) {
		case 1:
			 return Restrictions.ilike(parameter.name, parameter.value );
		case 2:
			 return Restrictions.eq(parameter.name,parameter.value);
		case 3:
			 return Restrictions.le(parameter.name,parameter.value);
		case 4:
			 return Restrictions.ge(parameter.name,parameter.value);
		case 5:
			 return Restrictions.lt(parameter.name,parameter.value);
		case 6:
			 return Restrictions.gt(parameter.name,parameter.value);
	 	default:
			 return Restrictions.eq(parameter.name,parameter.value);
		}
  }
}
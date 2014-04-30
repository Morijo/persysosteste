/*
 * Copyright (c) 2010-2013 Mark Allen.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.restmb.types;
import br.com.model.interfaces.IMaterial;
import com.restmb.Connection;
import com.restmb.DefaultJsonMapper;
import com.restmb.Parameter;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;
import com.restmb.oauth.service.ParameterList;
/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 24/03/2013 RestMb para Material
 */
public class Material extends Recurso<Material> implements IMaterial {

  @RestMB("material")
  private String material = "";
 
  public Material(String resourcePath, Class<Material> paClass) {
		super(resourcePath, paClass);
	}
	
  public Material(){
		super("/recurso/material", Material.class);
	}
	
 public String getMaterial() {
		return material;
 }
	
	public void setMaterial(String material) {
		this.material = material;
	}

	public static Connection<Material> listaMaterial(RestMBClient client, Parameter... p){
		Connection<Material> material = client.fetchConnection("/recurso/material", Material.class,"data",p);
		return material;
	}
	
	public Connection<Material> lista(RestMBClient client, Parameter... p){
		Connection<Material> material = client.fetchConnection("/recurso/material", Material.class,"data",p);
		return material;
	}
	
	public static Material pesquisaMaterial(RestMBClient client, Long id){
		try{
			 return client.fetchObject("/recurso/material/"+id, Material.class);
			}catch (RestMBGraphException e) {
				return null;
		}
	}
	
	public Material pesquisa(RestMBClient client, Long id){
		try{
			 return client.fetchObject("/recurso/material/"+id, Material.class);
			}catch (RestMBGraphException e) {
				return null;
		}
	}
	
	public Material salvar(RestMBClient client){
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/recurso/material",Material.class,json.toJson(this),headers);
	}
	
	public Material alterar(RestMBClient client){
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/recurso/material/"+this.getId(),Material.class,json.toJson(this),headers);
		
	}
	
	public boolean remover(RestMBClient client){
		try{
		  return client.deleteObject("/recurso/material/"+this.getId());
		}catch (RestMBGraphException e) {
			return false;
		}
	}
	
	public boolean remover(RestMBClient client, Long id){
		try{
		  return client.deleteObject("/recurso/material",id);
		}catch (RestMBGraphException e) {
			return false;
		}
	}
	
	public static boolean deletarMaterial(RestMBClient client, Long id){
		try{
		  return client.deleteObject("/recurso/material",id);
		}catch (RestMBGraphException e) {
			System.out.println(e.getErrorMessage());
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof Long)
            return (getId().compareTo((Long)obj) == 0);
         
        return false;
    }
}
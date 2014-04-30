package com.restmb.types;



import com.restmb.Connection;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;


public interface REST<T> {

	public Connection<T> lista(RestMBClient client, Parameter... p) throws RestMBGraphException;
	public T pesquisa(RestMBClient client, Long id) throws RestMBGraphException;
	public T salvar(RestMBClient client) throws RestMBGraphException;
	public T alterar(RestMBClient client) throws RestMBGraphException;
	public T alterarHome(RestMBClient client) throws RestMBGraphException;
	public boolean removerHome(RestMBClient client) throws RestMBGraphException;
	public boolean remover(RestMBClient client) throws RestMBGraphException;
	public boolean remover(RestMBClient client, Long id) throws RestMBGraphException;
}

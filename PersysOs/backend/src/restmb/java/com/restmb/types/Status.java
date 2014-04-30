package com.restmb.types;

import br.com.model.interfaces.IStatus;

public class Status implements IStatus{

	private Integer status;
	private String  statusNome;
	
	public Status(int status, String statusNome) {
		super();
		this.status = status;
		this.statusNome = statusNome;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusNome() {
		return statusNome;
	}
	public void setStatusNome(String statusNome) {
		this.statusNome = statusNome;
	}

	// Custom comparison
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer)
            return (status.compareTo((Integer)obj) == 0);
         
        return false;
    }

    // Custom hash code calculation
    @Override
    public int hashCode() {
        return status.hashCode();
    }
}

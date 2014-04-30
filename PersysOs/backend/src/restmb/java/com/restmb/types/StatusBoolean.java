package com.restmb.types;

public class StatusBoolean {

	private Boolean status;
	private String  statusNome;
	
	public StatusBoolean(boolean status, String statusNome) {
		super();
		this.status = status;
		this.statusNome = statusNome;
	}
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
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
        if (obj instanceof Boolean)
            return (status.compareTo((Boolean)obj) == 0);
         
        return false;
    }

    // Custom hash code calculation
    @Override
    public int hashCode() {
        return status.hashCode();
    }
}

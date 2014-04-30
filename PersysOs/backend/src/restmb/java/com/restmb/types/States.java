package com.restmb.types;

public class States {

	private String states;
	private String  statesName;
	
	public States(String states, String statesName) {
		super();
		this.states = states;
		this.statesName = statesName;
	}
	
	public String getStatus() {
		return states;
	}
	public void setStatus(String states) {
		this.states = states;
	}
	public String getStatusNome() {
		return statesName;
	}
	public void setStatusNome(String statesName) {
		this.statesName = statesName;
	}

	// Custom comparison
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer)
            return (states.compareTo((String)obj) == 0);
         
        return false;
    }

    // Custom hash code calculation
    @Override
    public int hashCode() {
        return states.hashCode();
    }
}

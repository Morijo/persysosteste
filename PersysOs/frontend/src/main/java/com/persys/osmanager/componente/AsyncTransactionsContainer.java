package com.persys.osmanager.componente;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.data.util.IndexedContainer;

public abstract class AsyncTransactionsContainer<T> extends IndexedContainer {

	/**
	 * 
	 */
	private   Execute   executeTask;
	protected T[]       parameterDoIn;
	protected Refresher refresher;
	
	public AsyncTransactionsContainer(){}
	
	public Refresher getRefresher(){
		refresher = new Refresher();
		return refresher;
	}
	
	public synchronized void execute(T... parameterDoIn){
		this.parameterDoIn = parameterDoIn;
		
		executeTask = new Execute();
		executeTask.start();
	}
	
	public class Execute extends Thread{
		
		public void run(){
			onPreExecute();
			doInBackGround(parameterDoIn);
		    refresher.addListener(new DatabaseListener());
			cancelled();
		}
	}
	
	private static final long serialVersionUID = 1L;
	public abstract void onPreExecute();
	public abstract void doInBackGround(T... t);
	public abstract void refreshView(final Refresher source);
	
	public void cancelled(){
		if(executeTask.isAlive()){
			executeTask.interrupt();
		}
	}
	
	public class DatabaseListener implements RefreshListener {
        private static final long serialVersionUID = -8765221895426102605L;
        
        @Override
        public void refresh(final Refresher source) {
        	refreshView(source);  
        }
	}
}

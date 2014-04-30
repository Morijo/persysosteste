package com.persys.osmanager.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tools.ant.taskdefs.Sleep;

import br.com.eventos.model.Alerta;
import br.com.exception.ModelException;

import com.persys.osmanager.componente.NotificationUpdater;

public class NotificationWorker {

	public void fetchAndUpdateDataWith(final NotificationUpdater updater, String consumerKey) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i=0;
				while(true){
					try {
						Thread.sleep(5000);
						updater.updateLabel(i+"");
						i++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
	/*
	static HashMap<Long, ArrayList<NotificationUpdater>> hashMapNotifications = new HashMap<Long, ArrayList<NotificationUpdater>>();

	public void fetchAndUpdateDataWith(final NotificationUpdater updater, String consumerKey) {
	
		ArrayList<NotificationUpdater> arrayList = get(consumerKey);
		if(arrayList != null){
			arrayList.add(updater);
			put(consumerKey, arrayList);
		}else{
			arrayList = new ArrayList<NotificationUpdater>();
			arrayList.add(updater);
			put(consumerKey, arrayList);
		}

		update(consumerKey);
	}
	
	public synchronized static void put(String consumerKey, ArrayList<NotificationUpdater> arrayList){
		hashMapNotifications.put(Long.parseLong(consumerKey), arrayList);
	}
	
	public synchronized static ArrayList<NotificationUpdater> get(String consumerKey){
		return hashMapNotifications.get(Long.parseLong(consumerKey));
	}

	public static void update(String consumerKey){
		ArrayList<NotificationUpdater> hNotificationUpdaters = get(consumerKey);
		for(NotificationUpdater updater : hNotificationUpdaters){
			try {
				updater.updateLabel(Alerta.numberAlerta(consumerKey, 1).toString());
			} catch (ModelException e) {
				updater.updateLabel("");
			}
		}
	}
	*/
}

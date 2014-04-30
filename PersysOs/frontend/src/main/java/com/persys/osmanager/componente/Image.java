package com.persys.osmanager.componente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.persys.osmanager.componente.interfaces.IImage;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Image extends Window{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Embedded image = new Embedded("Uploaded Image");
	
	public Image(final IImage source){
		
	 setModal(true);
     setResizable(false);
     setDraggable(false);
     addStyleName("dialog");
     setClosable(true);
	
	image.setVisible(false);
		
	class ImageUploader implements Receiver, SucceededListener {
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public File file;
		 
		 public OutputStream receiveUpload(String filename,
		                                   String mimeType) {
		     // Create upload stream
		     FileOutputStream fos = null; // Stream to write to
		     try {
		         // Open the file for writing.
		         file = new File(filename);
		         fos = new FileOutputStream(file);
		     } catch (final java.io.FileNotFoundException e) {
		         new Notification("Could not open file<br/>",
		                          e.getMessage(),
		                          Notification.Type.ERROR_MESSAGE)
		             .show(Page.getCurrent());
		         return null;
		     }
		     return fos; // Return the output stream to write to
		 }
		
		 public void uploadSucceeded(SucceededEvent event) {
		     // Show the uploaded file in the image viewer
		     image.setVisible(true);
		     image.setSource(new FileResource(file));
		     source.image(file);
		 }
		};
		ImageUploader receiver = new ImageUploader(); 
		
		//Create the upload with a caption and set receiver later
		Upload upload = new Upload("", receiver);
		upload.setButtonCaption("Iniciar Upload");
		upload.addSucceededListener(receiver);
		     
		//Put the components in a panel
		com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel("Carregar Imagem");
		com.vaadin.ui.Layout panelContent = new VerticalLayout();
		panelContent.addComponents(upload, image);
		panel.setContent(panelContent);
		setContent(panel);
	}
	
}	
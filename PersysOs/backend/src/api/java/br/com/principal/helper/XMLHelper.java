package br.com.principal.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class XMLHelper {

 
	 /**
    * Verifica se o arquivo de configura��o existe
    * 
    * 
    * @return Returna true quando existe e false quando ainda n�o existe
    * 
    */
	public static Object carregaArquivo(Class<?> classe, String fileName) throws JAXBException{
		
		JAXBContext context = null;
		try{
			context = JAXBContext.newInstance(classe);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return unmarshaller.unmarshal(new File(fileName));
		}
		catch (JAXBException e) {
		    throw new JAXBException(e.getMessage());
		}
	}
	
	/**
     * Gera um arquivo xml no disco
     * 
     * @param obj
     *            O objeto que deseja gerar o xml
     * @param fileName
     *            Nome do arquivo xml
     * @return Returna uma string com o xml resultante
     * 
     */
	public static String geraArquivo(Object obj, String nameFile){
		final StringWriter out = new StringWriter();
		Marshaller marshaller = null;
		JAXBContext context = null;
		try{
			context = JAXBContext.newInstance(obj.getClass());
			marshaller = context.createMarshaller();
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch(PropertyException e){
			e.printStackTrace();
		}
		catch (JAXBException e) {
		    e.printStackTrace();
		}
		
		Writer writer = null;
		try{
			writer = new FileWriter(nameFile);
			marshaller.marshal(obj, writer);
		}
		catch(JAXBException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
			  if(writer != null)
				writer.close();
			  }
			catch (Exception e) {
				e.getMessage();
			}
		}
		return out.toString();
	}
	   
}

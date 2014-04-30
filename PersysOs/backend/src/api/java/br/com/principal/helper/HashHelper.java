package br.com.principal.helper;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashHelper {

	public static String md5Senha(String senha) throws NoSuchAlgorithmException {  
		  MessageDigest md = MessageDigest.getInstance("MD5");  
	      BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
	      return  hash.toString(16);           
	   }  

	public static String md5Codigo(String senha) throws NoSuchAlgorithmException {  
		  MessageDigest md = MessageDigest.getInstance("MD5");  
	      BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
	      return  hash.toString(16);           
	   }  

	public static String shaCodigo(String senha) throws NoSuchAlgorithmException {  
		
			MessageDigest md = MessageDigest.getInstance("SHA-256");
		    md.update(senha.getBytes());
		
		    byte byteData[] = md.digest();
		
		    //convert the byte to hex format method 1
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < byteData.length; i++) {
		     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		    }

		    return sb.toString();
	}
	
	public static String chave(String chave){
		if(chave != null && !chave.isEmpty()){
			return String.valueOf((chave.concat("consumer secret chave persys")).hashCode());
		}
		return chave;
	}
	
	public static String shortUUID() {
		  UUID uuid = UUID.randomUUID();
		  int i = ByteBuffer.wrap(uuid.toString().getBytes()).getInt();
		  return Integer.toString(i, Character.MAX_RADIX);
	}
}

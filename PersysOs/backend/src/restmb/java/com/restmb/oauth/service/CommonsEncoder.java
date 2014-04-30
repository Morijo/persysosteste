package com.restmb.oauth.service;

public class CommonsEncoder extends Base64Encoder
{

  
  @Override
  public String getType()
  {
    return "CommonsCodec";
  }

  public static boolean isPresent()
  {
      //Class.forName("org.apache.commons.codec.binary.Base64");
       return false;
   
  }

@Override
public String encode(byte[] bytes) {
	// TODO Auto-generated method stub
	return null;
}
}

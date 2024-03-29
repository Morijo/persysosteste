/*
 * Copyright (c) 2010-2013 Mark Allen.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.restmb;

import static com.restmb.util.StringUtils.ENCODING_CHARSET;
import static com.restmb.util.StringUtils.fromInputStream;
import static java.net.HttpURLConnection.HTTP_OK;





import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.restmb.oauth.OAuth;
import com.restmb.oauth.service.Parameter;
import com.restmb.oauth.service.ParameterList;
/**
 * Default implementation of a service that sends HTTP requests endpoint.
 * 
 */
public class DefaultWebRequestor implements WebRequestor {
  
  /**
   * Arbitrary unique boundary marker for multipart {@code POST}s.
   */
  private static final String MULTIPART_BOUNDARY = "*****";

  /**
   * Line separator for multipart {@code POST}s.
   */
  private static final String MULTIPART_CARRIAGE_RETURN_AND_NEWLINE = "\r\n";

  /**
   * Hyphens for multipart {@code POST}s.
   */
  private static final String MULTIPART_TWO_HYPHENS = "--";

  /**
   * Default buffer size for multipart {@code POST}s.
   */
  private static final int MULTIPART_DEFAULT_BUFFER_SIZE = 8192;

  /**
   * By default, how long should we wait for a response (in ms)?
   */
  private static final int DEFAULT_READ_TIMEOUT_IN_MS = 180000;

  private HttpURLConnection httpUrlConnection = null;
  
  private OAuth oauth = null;
  private ParameterList parameterHeadersList = null;
  private String body = null;
  
  
  /**
   * @see com.restmb.WebRequestor#executeGet(java.lang.String)
   */
  @Override
  public Response executeGet(String url) throws IOException {
  
    InputStream inputStream = null;

    try {
      httpUrlConnection = openConnection(new URL(url));
      //httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
      httpUrlConnection.setUseCaches(false);
      httpUrlConnection.setRequestMethod("GET");
      
      customizeConnection(httpUrlConnection,url);
      httpUrlConnection.connect();
     
      try {
        inputStream =
            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
              .getInputStream();
      } catch (IOException e) {}

      Response response = new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));

      return response;
    } finally {
      closeQuietly(httpUrlConnection);
    }
  }
  
  /**
   * @see com.restmb.WebRequestor#executeGet(java.lang.String)
   */
  @Override
  public InputStream executeGetImage(String url) throws IOException {
  
    InputStream inputStream = null;

    try {
      httpUrlConnection = openConnection(new URL(url));
      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
      httpUrlConnection.setUseCaches(false);
      httpUrlConnection.setRequestMethod("GET");
      
      customizeConnection(httpUrlConnection,url);
      httpUrlConnection.connect();
     
      try {
        inputStream =
            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
              .getInputStream();
      } catch (IOException e) {}
     
  	  return inputStream;
    } finally {
      closeQuietly(httpUrlConnection);
    }
  }

  @Override
  public Response executeDelete(String url) throws IOException {
	  InputStream inputStream = null;

	    try {
	      httpUrlConnection = openConnection(new URL(url));
	      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
	      httpUrlConnection.setUseCaches(false);
	      httpUrlConnection.setRequestMethod("DELETE");
	      
	      customizeConnection(httpUrlConnection,url);
	      httpUrlConnection.connect();
	     
	      try {
	        inputStream =
	            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
	              .getInputStream();
	      } catch (IOException e) {}

	      Response response = new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
	     
	      return response;
	     
	      
	 	} finally {
	      closeQuietly(httpUrlConnection);
	    }
  }

	@Override
	public Response executePut(String url, BinaryAttachment... binaryAttachments)
			throws IOException {
		
		if (binaryAttachments == null)
		      binaryAttachments = new BinaryAttachment[] {};
		   
		    HttpURLConnection httpUrlConnection = null;
		    OutputStream outputStream = null;
		    InputStream inputStream = null;

		    try {
		      httpUrlConnection = openConnection(new URL(url));
		      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);

		      httpUrlConnection.setRequestMethod("PUT");
		      httpUrlConnection.setDoOutput(true);
		      httpUrlConnection.setUseCaches(false);
		      httpUrlConnection.setRequestProperty("Content-Type", "application/json");
		      
		      customizeConnection(httpUrlConnection,url);

		      if (binaryAttachments.length > 0) {
		          httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
		          httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
		        }

		        httpUrlConnection.connect();
		        outputStream = httpUrlConnection.getOutputStream();
		        
		        if (binaryAttachments.length > 0) {
		          for (BinaryAttachment binaryAttachment : binaryAttachments) {
		            outputStream
		              .write((MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE
		                  + "Content-Disposition: form-data; name=\"" + createFormFieldName(binaryAttachment) + "\"; filename=\""
		                  + binaryAttachment.getFilename() + "\"" + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE)
		                .getBytes(ENCODING_CHARSET));

		            write(binaryAttachment.getData(), outputStream, MULTIPART_DEFAULT_BUFFER_SIZE);

		            outputStream.write((MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY
		                + MULTIPART_TWO_HYPHENS + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).getBytes(ENCODING_CHARSET));
		          }
		        } else {
		      	outputStream.write(body.getBytes("UTF-8"));
		        }

		        try {
		          inputStream =
		              httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
		                .getInputStream();
		        } catch (IOException e) {}

		        return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
		      } finally {
		        if (binaryAttachments.length > 0)
		          for (BinaryAttachment binaryAttachment : binaryAttachments)
		            closeQuietly(binaryAttachment.getData());

		        closeQuietly(outputStream);
		        closeQuietly(httpUrlConnection);
		      }
		    }
	/**
   * @see com.restmb.WebRequestor#executePost(java.lang.String, java.lang.String)
   */
  @Override
  public Response executePost(String url, String parameters) throws IOException {
    return executePost(url, (BinaryAttachment[]) null);
  }

  /**
   * @see com.restmb.WebRequestor#executePost(java.lang.String, java.lang.String, com.restmb.BinaryAttachment[])
   */
  @Override
  public Response executePost(String url, BinaryAttachment... binaryAttachments) throws IOException {
   
	if (binaryAttachments == null)
      binaryAttachments = new BinaryAttachment[] {};
   
    HttpURLConnection httpUrlConnection = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;

    try {
      httpUrlConnection = openConnection(new URL(url));
      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);

      httpUrlConnection.setRequestMethod("POST");
      httpUrlConnection.setDoOutput(true);
      httpUrlConnection.setUseCaches(false);
      
      customizeConnection(httpUrlConnection,url);

      
      if (binaryAttachments.length > 0) {
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+MULTIPART_BOUNDARY);
      }

      httpUrlConnection.connect();
      outputStream = httpUrlConnection.getOutputStream();

      if (binaryAttachments.length > 0) {
        for (BinaryAttachment binaryAttachment : binaryAttachments) {
        	  outputStream
              .write((MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE
                  + "Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\""
                  + binaryAttachment.getFilename() + "\"" + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE)
                .getBytes(ENCODING_CHARSET));

            write(binaryAttachment.getData(), outputStream, MULTIPART_DEFAULT_BUFFER_SIZE);

            outputStream.write((MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY
                + MULTIPART_TWO_HYPHENS + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).getBytes(ENCODING_CHARSET));
          
        }
      } else {
    	outputStream.write(body.getBytes("UTF-8"));
      }

      try {
        inputStream =
            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
              .getInputStream();
      } catch (IOException e) {}

      return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
    } finally {
      if (binaryAttachments.length > 0)
        for (BinaryAttachment binaryAttachment : binaryAttachments)
          closeQuietly(binaryAttachment.getData());

      closeQuietly(outputStream);
      closeQuietly(httpUrlConnection);
    }
  }
  
  
  public Response requestToken(String url) throws IOException {
	   
		httpUrlConnection = null;
	    OutputStream outputStream = null;
	    InputStream inputStream = null;

	    try {
	      httpUrlConnection = openConnection(new URL(url));
	      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);

	      httpUrlConnection.setRequestMethod("POST");
	      httpUrlConnection.setDoOutput(true);
	      httpUrlConnection.setUseCaches(false);
	      httpUrlConnection.setRequestProperty("Content-Type", "application/json");
	      if(oauth != null){
			  oauth.setUrl(url);
			  oauth.setVerbHttp("POST");
			  httpUrlConnection.setRequestProperty("Authorization", oauth.getRequestToken());
		  }

	      httpUrlConnection.connect();
	    
	      try {
	        inputStream =
	            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
	              .getInputStream();
	      } catch (IOException e) {}

	      return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
	    } finally {
	      closeQuietly(outputStream);
	      closeQuietly(httpUrlConnection);
	    }
	  }


	@Override
	public Response accessToken(String url) throws IOException {
		httpUrlConnection = null;
	    OutputStream outputStream = null;
	    InputStream inputStream = null;

	    try {
	      httpUrlConnection = openConnection(new URL(url));
	      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);

	      httpUrlConnection.setRequestMethod("POST");
	      httpUrlConnection.setDoOutput(true);
	      httpUrlConnection.setUseCaches(false);
	      if(oauth != null){
			  oauth.setUrl(url);
			  oauth.setVerbHttp("POST");
			  httpUrlConnection.setRequestProperty("Authorization", oauth.getAccessToken());
		  }

	      httpUrlConnection.connect();
	      
	      try {
	        inputStream =
	            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
	              .getInputStream();
	      } catch (IOException e) {}

	      return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
	    } finally {
	      closeQuietly(outputStream);
	      closeQuietly(httpUrlConnection);
	    }
	}
	
	@Override
	public Response sign(String url, String parameters) throws IOException {
		httpUrlConnection = null;
	    OutputStream outputStream = null;
	    InputStream inputStream = null;

	    try {
	      httpUrlConnection = openConnection(new URL(url));
	      httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
	      httpUrlConnection.setRequestMethod("POST");
	      httpUrlConnection.setDoOutput(true);
	      httpUrlConnection.setUseCaches(false);
	      httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      customizeConnection(httpUrlConnection,url);

	      httpUrlConnection.connect();
	     
	      outputStream = httpUrlConnection.getOutputStream();
	      outputStream.write(parameters.getBytes());
	      
	      try {
	        inputStream =
	            httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection
	              .getInputStream();
	      } catch (IOException e) {}

	      return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
	    } finally {
	      closeQuietly(outputStream);
	      closeQuietly(httpUrlConnection);
	    }
	}
  /**
   * Given a {@code url}, opens and returns a connection to it.
   * <p>
   * If you'd like to pipe your connection through a proxy, this is the place to do so.
   * 
   * @param url
   *          The URL to connect to.
   * @return A connection to the URL.
   * @throws IOException
   *           If an error occurs while establishing the connection.
   * @since 1.6.3
   */
  protected HttpURLConnection openConnection(URL url) throws IOException {
    return (HttpURLConnection) url.openConnection();
  }

  /**
   * Hook method which allows subclasses to easily customize the {@code connection}s created by
   * {@link #executeGet(String)} and {@link #executePost(String, String)} - for example, setting a custom read timeout
   * or request header.
   * <p>
   * This implementation is a no-op.
   * 
   * @param connection
   *          The connection to customize.
   */
  protected void customizeConnection(HttpURLConnection connection, String url) {
	  if(oauth != null){
		  oauth.setUrl(url);
		  oauth.setVerbHttp(connection.getRequestMethod());
		  connection.setRequestProperty("Authorization", oauth.getSignRequest());
	  }
	  if(parameterHeadersList !=null)
		  for(Parameter headers : parameterHeadersList.getParams()){
			  connection.setRequestProperty(headers.key, headers.value);
		  }
 }

  /**
   * Attempts to cleanly close a resource, swallowing any exceptions that might occur since there's no way to recover
   * anyway.
   * <p>
   * It's OK to pass {@code null} in, this method will no-op in that case.
   * 
   * @param closeable
   *          The resource to close.
   */
  protected void closeQuietly(Closeable closeable) {
    if (closeable == null)
      return;
    try {
      closeable.close();
    } catch (Throwable t) {}
  }

  /**
   * Attempts to cleanly close an {@code HttpURLConnection}, swallowing any exceptions that might occur since there's no
   * way to recover anyway.
   * <p>
   * It's OK to pass {@code null} in, this method will no-op in that case.
   * 
   * @param httpUrlConnection
   *          The connection to close.
   */
  protected void closeQuietly(HttpURLConnection httpUrlConnection) {
    if (httpUrlConnection == null)
      return;
    try {
      httpUrlConnection.disconnect();
    } catch (Throwable t) {}
  }

  /**
   * Writes the contents of the {@code source} stream to the {@code destination} stream using the given
   * {@code bufferSize}.
   * 
   * @param source
   *          The source stream to copy from.
   * @param destination
   *          The destination stream to copy to.
   * @param bufferSize
   *          The size of the buffer to use during the copy operation.
   * @throws IOException
   *           If an error occurs when reading from {@code source} or writing to {@code destination}.
   * @throws NullPointerException
   *           If either {@code source} or @{code destination} is {@code null}.
   */
  protected void write(InputStream source, OutputStream destination, int bufferSize) throws IOException {
    if (source == null || destination == null)
      throw new NullPointerException("Must provide non-null source and destination streams.");

    int read = 0;
    byte[] chunk = new byte[bufferSize];
    while ((read = source.read(chunk)) > 0)
      destination.write(chunk, 0, read);
  }

  /**
   * Creates the form field name for the binary attachment filename by stripping off the file extension - for example,
   * the filename "test.png" would return "test".
   * 
   * @param binaryAttachment
   *          The binary attachment for which to create the form field name.
   * @return The form field name for the given binary attachment.
   */
  protected String createFormFieldName(BinaryAttachment binaryAttachment) {
    String name = binaryAttachment.getFilename();
    int fileExtensionIndex = name.lastIndexOf(".");
    return fileExtensionIndex > 0 ? name.substring(0, fileExtensionIndex) : name;
  }
 
  public OAuth getOauth() {
		return oauth;
   }
	
   public void setOauth(OAuth oauth) {
		this.oauth = oauth;
   }

   @Override
	public void setHeaders(ParameterList parameterHeadersList) {
		this.parameterHeadersList = parameterHeadersList;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

  
  
}
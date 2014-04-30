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
import static com.restmb.util.StringUtils.isBlank;
import static com.restmb.util.StringUtils.join;
import static com.restmb.util.StringUtils.trimToEmpty;
import static com.restmb.util.UrlUtils.urlEncode;
import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.restmb.WebRequestor.Response;
import com.restmb.exception.RestMBException;
import com.restmb.exception.RestMBGraphException;
import com.restmb.exception.RestMBJsonMappingException;
import com.restmb.exception.RestMBNetworkException;
import com.restmb.exception.RestMBResponseStatusException;
import com.restmb.json.JsonException;
import com.restmb.json.JsonObject;
import com.restmb.oauth.OAuth;
import com.restmb.oauth.extractors.TokenExtractorImpl;
import com.restmb.oauth.service.ParameterList;
import com.restmb.oauth.service.Token;


public class DefaultClient extends BaseClient implements RestMBClient {
  
  /**
   * API OAuth.
   */
  protected OAuth oauth = null;
 
  /**
   * API endpoint URL.
   */
  protected String _GRAPH_ENDPOINT_URL = "http://localhost:8080/restfull";

  /**
   * Reserved method override parameter name.
   */
  protected static final String METHOD_PARAM_NAME = "method";

  /**
   * Reserved "multiple IDs" parameter name.
   */
  protected static final String IDS_PARAM_NAME = "ids";

 /**
   * Reserved "result format" parameter name.
   */
  protected static final String FORMAT_PARAM_NAME = "format";

  /**
   * API error response 'error' attribute name.
   */
  protected static final String ERROR_ATTRIBUTE_NAME = "error";
  
  /**
   * API error response 'type' attribute name.
   */
  protected static final String ERROR_TYPE_ATTRIBUTE_NAME = "type";

  /**
   * API error response 'message' attribute name.
   */
  protected static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "message";

  
  
  /**
   * Creates a client with no access token.
   * <p>
   * Without an access token, you can view and search public graph data but can't do much else.
   */
  public DefaultClient(String url) {
    this(null,url);
  }

  /**
   * Creates a Facebook Graph API client with the given {@code accessToken}.
   * 
   * @param accessToken
   *          A Facebook OAuth access token.
   */
  public DefaultClient(OAuth oAuth,String url) {
	this(oAuth,new DefaultWebRequestor(), new DefaultJsonMapper());
	_GRAPH_ENDPOINT_URL = url;
  }

  /**
   * 
   * @param webRequestor
   *          The {@link WebRequestor} implementation to use for sending requests to the API endpoint.
   * @param jsonMapper
   *          The {@link JsonMapper} implementation to use for mapping API response JSON to Java objects.
   * @throws NullPointerException
   *           If {@code jsonMapper} or {@code webRequestor} is {@code null}.
   */
  public DefaultClient(OAuth oAuth,WebRequestor webRequestor, JsonMapper jsonMapper) {
    super();

    verifyParameterPresence("jsonMapper", jsonMapper);
    verifyParameterPresence("webRequestor", webRequestor);

    this.webRequestor = webRequestor;
    this.jsonMapper = jsonMapper;
   
    this.oauth = oAuth;
    
    illegalParamNames.addAll(Arrays
      .asList(new String[] { ACCESS_TOKEN_PARAM_NAME, METHOD_PARAM_NAME, FORMAT_PARAM_NAME }));
  }

  /**
   * @see com.restmb.RestMBClient#deleteObject(java.lang.String, com.restmb.Parameter[])
   */
  @Override
  public boolean deleteObject(String object, Parameter... parameters) {
    verifyParameterPresence("object", object);
    
    trimToEmpty(object).toLowerCase();
    if (!object.startsWith("/"))
    	object = "/" + object;

    String parameter = toParameterString(parameters);
    
    if(parameter.length() <=0)
    	object = createEndpointForApiCall(object,false);
	else{
	    object = createEndpointForApiCall(object,false)+"?"+parameter;
	}
   
    final String fullEndpoint = object;
   
     if(oauth != null){
       webRequestor.setOauth(oauth);
     }
     
     String connectionJson = makeRequestAndProcessResponse(new Requestor() {
	      /**
	       * @see com.restmb.DefaultClient.Requestor#makeRequest()
	       */
	      public Response makeRequest() throws IOException {
	        return webRequestor.executeDelete(fullEndpoint);
	      }
	 });

	 return connectionJson.isEmpty();
  }
  
  @Override
  public boolean deleteObject(String resource, Object objectID) {
    verifyParameterPresence("object", resource);
    verifyParameterPresence("object", objectID);
    
    
    trimToEmpty(resource).toLowerCase();
    if (!resource.startsWith("/"))
    	resource = "/" + resource;

    final String fullEndpoint = createEndpointForApiCall(resource+"/"+urlEncode(String.valueOf(objectID)),false);
   
     if(oauth != null){
       webRequestor.setOauth(oauth);
     }
     
     String connectionJson = makeRequestAndProcessResponse(new Requestor() {
	      /**
	       * @see com.restmb.DefaultClient.Requestor#makeRequest()
	       */
	      public Response makeRequest() throws IOException {
	        return webRequestor.executeDelete(fullEndpoint);
	      }
	 });

	 return connectionJson.isEmpty();
  }

  /**
   * @see com.restmb.RestMBClient#fetchConnection(java.lang.String, java.lang.Class, com.restmb.Parameter[])
   */
  public <T> Connection<T> fetchConnection(String connection, Class<T> connectionType,String root, Parameter... parameters) {
    verifyParameterPresence("connection", connection);
    verifyParameterPresence("connectionType", connectionType);
    return new Connection<T>(this, makeRequest(connection, parameters), connectionType, root);
  }

  /**
   * @see com.restmb.RestMBClient#fetchConnectionPage(java.lang.String, java.lang.Class)
   */
  public <T> Connection<T> fetchConnectionPage(final String connectionPageUrl, Class<T> connectionType) {
	  
	if(oauth != null){
	  webRequestor.setOauth(oauth);
	}
    String connectionJson = makeRequestAndProcessResponse(new Requestor() {
      /**
       * @see com.restmb.DefaultClient.Requestor#makeRequest()
       */
      public Response makeRequest() throws IOException {
        return webRequestor.executeGet(connectionPageUrl);
      }
    });

    return new Connection<T>(this, connectionJson, connectionType,"data");
  }
  
  /**
   * @see com.restmb.RestMBClient#fetchConnectionPage(java.lang.String, java.lang.Class)
   */
  public InputStream fetchImage(String imageUrl, Parameter... parameters) {
	  
	if(oauth != null){
	  webRequestor.setOauth(oauth);
	}
	
	trimToEmpty(imageUrl).toLowerCase();
	if (!imageUrl.startsWith("/"))
		imageUrl = "/" + imageUrl;

	final String fullEndpoint;
	String parameter = toParameterString(parameters);
		
	if(parameter.length() <=0)
	   fullEndpoint = createEndpointForApiCall(imageUrl,false);
	else{
	   fullEndpoint = createEndpointForApiCall(imageUrl,false)+"?"+parameter;
	}
	 
    try {
		return webRequestor.executeGetImage(fullEndpoint);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RestMBGraphException("Imagem", "Falha ao carregar imagem", 1);
	}
	
  }

   public <T> Connection<T> fetchConnection(String connection,
			Class<T> connectionType, String root, ParameterList headers,
			Parameter... parameters) {
		
	   webRequestor.setHeaders(headers);
	   
	   verifyParameterPresence("connection", connection);
	   verifyParameterPresence("connectionType", connectionType);
	   return new Connection<T>(this, makeRequest(connection, parameters), connectionType, root);
	    
	}
  /**
   * @see com.restmb.RestMBClient#fetchObject(java.lang.String, java.lang.Class, com.restmb.Parameter[])
   */
  public <T> T fetchObject(String object, Class<T> objectType, Parameter... parameters) {
    verifyParameterPresence("object", object);
    verifyParameterPresence("objectType", objectType);
    return jsonMapper.toJavaObject(makeRequest(object, parameters), objectType);
  }

  /**
   * @see com.restmb.RestMBClient#fetchObjects(java.util.List, java.lang.Class, com.restmb.Parameter[])
   */
  @SuppressWarnings("unchecked")
  public <T> T fetchObjects(List<String> ids, Class<T> objectType, Parameter... parameters) {
    verifyParameterPresence("ids", ids);
    verifyParameterPresence("connectionType", objectType);

    if (ids.size() == 0)
      throw new IllegalArgumentException("The list of IDs cannot be empty.");

    for (Parameter parameter : parameters)
      if (IDS_PARAM_NAME.equals(parameter.name))
        throw new IllegalArgumentException("You cannot specify the '" + IDS_PARAM_NAME + "' URL parameter yourself - "
            + "RestFB will populate this for you with " + "the list of IDs you passed to this method.");

    // Normalize the IDs
    for (int i = 0; i < ids.size(); i++) {
      String id = ids.get(i).trim().toLowerCase();
      if ("".equals(id))
        throw new IllegalArgumentException("The list of IDs cannot contain blank strings.");
      ids.set(i, id);
    }

    try {
      JsonObject jsonObject =
          new JsonObject(makeRequest("",
            parametersWithAdditionalParameter(Parameter.with(IDS_PARAM_NAME, join(ids)), parameters)));

      return objectType.equals(JsonObject.class) ? (T) jsonObject : jsonMapper.toJavaObject(jsonObject.toString(),
        objectType);
    } catch (JsonException e) {
      throw new RestMBJsonMappingException("Unable to map connection JSON to Java objects", e);
    }
  }

  /**
   * @see com.restmb.RestMBClient#publish(java.lang.String, java.lang.Class, com.restmb.BinaryAttachment,
   *      com.restmb.Parameter[])
   */
  public <T> T publish(String connection, Class<T> objectType, BinaryAttachment binaryAttachment,
      Parameter... parameters) {
    verifyParameterPresence("connection", connection);

    List<BinaryAttachment> binaryAttachments = new ArrayList<BinaryAttachment>();
    if (binaryAttachment != null)
      binaryAttachments.add(binaryAttachment);

    return jsonMapper.toJavaObject(makeRequest(connection, true, false, binaryAttachments, parameters), objectType);
  }
  
  @Override
	public <T> T publish(String connection,Class<T> objectType, String body,ParameterList headers) {
	
	  verifyParameterPresence("connection", connection);
	  trimToEmpty(connection).toLowerCase();
	    if (!connection.startsWith("/"))
	    	connection = "/" + connection;

	    final String fullEndpoint = createEndpointForApiCall(connection,false);
	   
	     if(oauth != null){
	       webRequestor.setOauth(oauth);
	       webRequestor.setHeaders(headers);
	       webRequestor.setBody(body);
	     }
	     
	     String connectionJson = makeRequestAndProcessResponse(new Requestor() {
		      /**
		       * @see com.restmb.DefaultClient.Requestor#makeRequest()
		       */
		      public Response makeRequest() throws IOException {
		        return webRequestor.executePost(fullEndpoint);
		      }
		 });

		    return jsonMapper.toJavaObject(connectionJson, objectType);
	}
  
   @Override
	public <T> T publishChanges(String connection,Class<T> objectType, String body,ParameterList headers) {
	
	  verifyParameterPresence("connection", connection);
	  trimToEmpty(connection).toLowerCase();
	    if (!connection.startsWith("/"))
	    	connection = "/" + connection;

	    final String fullEndpoint = createEndpointForApiCall(connection,false);
	   
	     if(oauth != null){
	       webRequestor.setOauth(oauth);
	       webRequestor.setHeaders(headers);
	       webRequestor.setBody(body);
	     }
	     
	     String connectionJson = makeRequestAndProcessResponse(new Requestor() {
		      /**
		       * @see com.restmb.DefaultClient.Requestor#makeRequest()
		       */
		      public Response makeRequest() throws IOException {
		        return webRequestor.executePut(fullEndpoint);
		      }
		 });

		    return jsonMapper.toJavaObject(connectionJson, objectType);
	}

  /**
   * @see com.restmb.RestMBClient#publish(java.lang.String, java.lang.Class, com.restmb.Parameter[])
   */
  public <T> T publish(String connection, Class<T> objectType, Parameter... parameters) {
    return publish(connection, objectType, null, parameters);
  }

public Token obtainAppRequestToken(OAuth oauth, String endpoint) {
   
      webRequestor.setOauth(oauth);
     
     trimToEmpty(endpoint).toLowerCase();
     if (!endpoint.startsWith("/"))
       endpoint = "/" + endpoint;

     final String fullEndpoint = createEndpointForApiCall(endpoint,false);
    

     if(oauth != null){
	       webRequestor.setOauth(oauth);
	 }
     
     String response = makeRequestAndProcessResponse(new Requestor() {
         public Response makeRequest() throws IOException {
           return webRequestor.requestToken(fullEndpoint);
         }
       });
    
     TokenExtractorImpl token = new TokenExtractorImpl();
     return token.extract(response);
  }
  
  public String sign(OAuth oauth, String endpoint, final String parameters) {
	   
      webRequestor.setOauth(oauth);
     
     trimToEmpty(endpoint).toLowerCase();
     if (!endpoint.startsWith("/"))
       endpoint = "/" + endpoint;

     final String fullEndpoint =
         createEndpointForApiCall(endpoint,false) + "?oauth_token=" + oauth.getTokenKey();
    
     String response = makeRequestAndProcessResponse(new Requestor() {
         public Response makeRequest() throws IOException {
           return webRequestor.sign(fullEndpoint,parameters);
         }
       });
    
     TokenExtractorImpl token = new TokenExtractorImpl();
     return token.extractVerifier(response);
  }
  
  @Override
	public Token obtainAppAccessToken(OAuth oauth, String endpoint) {
	  
	  webRequestor.setOauth(oauth);
	     
	     trimToEmpty(endpoint).toLowerCase();
	     if (!endpoint.startsWith("/"))
	       endpoint = "/" + endpoint;

	     final String fullEndpoint =
	         createEndpointForApiCall(endpoint,false);
	    
	     String response = makeRequestAndProcessResponse(new Requestor() {
	         public Response makeRequest() throws IOException {
	           return webRequestor.accessToken(fullEndpoint);
	         }
	       });
	    
	     TokenExtractorImpl token = new TokenExtractorImpl();
	     return token.extract(response);
	  }
 
  /**
   * @see com.restmb.RestMBClient#getJsonMapper()
   */
  public JsonMapper getJsonMapper() {
    return jsonMapper;
  }

  /**
   * @see com.restmb.RestMBClient#getWebRequestor()
   */
  public WebRequestor getWebRequestor() {
    return webRequestor;
  }

  /**
   * Coordinates the process of executing the API request GET/POST and processing the response we receive from the
   * endpoint.
   * 
   * @param endpoint
   *          endpoint.
   * @param parameters
   *          Arbitrary number of parameters to send along to Facebook as part of the API call.
   * @return The JSON returned by for the API call.
   * @throws RestMBException
   *           If an error occurs while making the Facebook API POST or processing the response.
   */
  protected String makeRequest(String endpoint, Parameter... parameters) {
    return makeRequest(endpoint, false, false, null, parameters);
  }

  /**
   * Coordinates the process of executing the API request GET/POST and processing the response we receive from the
   * endpoint.
   * 
   * @param endpoint
   *          endpoint.
   * @param executeAsPost
   *          {@code true} to execute the web request as a {@code POST}, {@code false} to execute as a {@code GET}.
   * @param executeAsDelete
   *          {@code true} to add a special 'treat this request as a {@code DELETE}' parameter.
   * @param binaryAttachment
   *          A binary file to include in a {@code POST} request. Pass {@code null} if no attachment should be sent.
   * @param parameters
   *          Arbitrary number of parameters to send along to Facebook as part of the API call.
   * @return The JSON returned by Facebook for the API call.
   * @throws RestMBException
   *           If an error occurs while making the Facebook API POST or processing the response.
   */
  protected String makeRequest(String endpoint, final boolean executeAsPost, boolean executeAsDeletePost,
      final List<BinaryAttachment> binaryAttachments, Parameter... parameters) {
    verifyParameterLegality(parameters);

    if (executeAsDeletePost)
      parameters = parametersWithAdditionalParameter(Parameter.with(METHOD_PARAM_NAME, "delete"), parameters);

    trimToEmpty(endpoint).toLowerCase();
    if (!endpoint.startsWith("/"))
      endpoint = "/" + endpoint;

     String parameter = toParameterString(parameters);
	  
	  if(parameter.length() <=0)
		  endpoint = createEndpointForApiCall(endpoint,binaryAttachments != null && binaryAttachments.size() > 0);
	   else{
		  endpoint = createEndpointForApiCall(endpoint,binaryAttachments != null && binaryAttachments.size() > 0)+"?"+parameter;
	  }
	  
     final String fullEndpoint = endpoint;
     
     if(oauth != null){
       webRequestor.setOauth(oauth);
     }
     
     return makeRequestAndProcessResponse(new Requestor() {
      /**
       * @see com.restmb.DefaultClient.Requestor#makeRequest()
       */
      public Response makeRequest() throws IOException {
        return executeAsPost ? webRequestor.executePost(fullEndpoint, binaryAttachments == null ? null
            : binaryAttachments.toArray(new BinaryAttachment[] {})) : webRequestor.executeGet(fullEndpoint);
      }
    });
  }
 
  protected static interface Requestor {
    Response makeRequest() throws IOException;
  }
  
  protected String makeRequestAndProcessResponse(Requestor requestor) {
    Response response = null;
    
    try {
      response = requestor.makeRequest();
    } catch (Throwable t) {
      throw new RestMBNetworkException("Request failed", t);
    }

    String body = response.getBody();

    if (HTTP_OK != response.getStatusCode() && HTTP_BAD_REQUEST != response.getStatusCode()
        && HTTP_UNAUTHORIZED != response.getStatusCode() && HTTP_NOT_FOUND != response.getStatusCode()
        && HTTP_INTERNAL_ERROR != response.getStatusCode() && HTTP_FORBIDDEN != response.getStatusCode()
        && 204 != response.getStatusCode() && 406 != response.getStatusCode()
        && 201 != response.getStatusCode())
      throw new RestMBNetworkException(body, response.getStatusCode());
    
   
    // If the response contained an error code, throw an exception.
    throwResponseStatusExceptionIfNecessary(body, response.getStatusCode());

    if (HTTP_INTERNAL_ERROR == response.getStatusCode() || HTTP_UNAUTHORIZED == response.getStatusCode())
      throw new RestMBNetworkException(body, response.getStatusCode());

    return body;
  }

  /**
   * Throws an exception if returned an error response. Using the Graph API, it's possible to see both the new
   * Graph API-style errors as well as Legacy API-style errors, so we have to handle both here. This method extracts
   * relevant information from the error JSON and throws an exception which encapsulates it for end-user consumption.
   * <p>
   * For Graph API errors:
   * <p>
   * If the {@code error} JSON field is present, we've got a response status error for this API call.
   * <p>
   * For Legacy errors (e.g. FQL):
   * <p>
   * If the {@code error_code} JSON field is present, we've got a response status error for this API call.
   * 
   * @param json
   *          The JSON returned by in response to an API call.
   * @param httpStatusCode
   *          The HTTP status code returned by the server, e.g. 500.
   * @throws RestMBGraphException
   *           If the JSON contains a Graph API error response.
   * @throws RestMBResponseStatusException
   *           If the JSON contains an Legacy API error response.
   * @throws RestMBJsonMappingException
   *           If an error occurs while processing the JSON.
   */
  protected void throwResponseStatusExceptionIfNecessary(String json, Integer httpStatusCode) {
  
    try {
      // If the result is not an object, bail immediately.
      if (!json.startsWith("{"))
        return;

      JsonObject errorObject = new JsonObject(json);

      if (errorObject == null || !errorObject.has(ERROR_ATTRIBUTE_NAME))
        return;

      JsonObject innerErrorObject = errorObject.getJsonObject(ERROR_ATTRIBUTE_NAME);
   
      String type = innerErrorObject.getString(ERROR_TYPE_ATTRIBUTE_NAME);
      String message = innerErrorObject.getString(ERROR_MESSAGE_ATTRIBUTE_NAME);		  
      
      throw new RestMBGraphException(type,message,httpStatusCode);
    } catch (JsonException e) {
      throw new RestMBJsonMappingException("Unable to process the Persys API response", e);
    }
  }
  
  /**
   * 
   * @param parameters
   *          Arbitrary number of extra parameters to include in the request.
   * @return The parameter string to include in the API request.
   * @throws RestMBJsonMappingException
   *           If an error occurs when building the parameter string.
   */
  protected String toParameterString(Parameter... parameters) {
   
    StringBuilder parameterStringBuilder = new StringBuilder();
    boolean first = true;

    for (Parameter parameter : parameters) {
      if (first)
        first = false;
      else
        parameterStringBuilder.append("&");

      parameterStringBuilder.append(urlEncode(parameter.name));
      parameterStringBuilder.append("=");
      parameterStringBuilder.append(urlEncodedValueForParameterName(parameter.name, parameter.value));
    }

    return parameterStringBuilder.toString();
  }
  
  protected String parametersToJson(Parameter... queries) {
	    verifyParameterPresence("queries", queries);

	    JsonObject jsonObject = new JsonObject();

	    for (Parameter parameter : queries) {
	      if (isBlank(urlEncode(parameter.name)) || isBlank(urlEncodedValueForParameterName(parameter.name, parameter.value)))
	        throw new IllegalArgumentException("Provided queries must have non-blank keys and values. " + "You provided: "
	            + queries);

	      try {
	        jsonObject.put(trimToEmpty(urlEncode(parameter.name)), trimToEmpty(urlEncodedValueForParameterName(parameter.name, parameter.value)));
	      } catch (JsonException e) {
	        // Shouldn't happen unless bizarre input is provided
	        throw new IllegalArgumentException("Unable to convert " + queries + " to JSON.", e);
	      }
	    }

	    return jsonObject.toString();
	  }


  /**
   * @see com.restmb.BaseClient#createEndpointForApiCall(java.lang.String,boolean)
   */
  @Override
  protected String createEndpointForApiCall(String apiCall, boolean hasAttachment) {
    trimToEmpty(apiCall).toLowerCase();
    while (apiCall.startsWith("/"))
      apiCall = apiCall.substring(1);

    String baseUrl = getEndpointUrl();
    return format("%s/%s", baseUrl, apiCall);
  }

  /**
   * Returns the base endpoint URL for the Graph API.
   * 
   * @return The base endpoint URL for the Graph API.
   */
  public String getEndpointUrl() {
    return _GRAPH_ENDPOINT_URL;
  }

  	@Override
	public void setOauth(OAuth oAuth) {
		this.oauth = oAuth;
		
	}

	@Override
	public OAuth getOauth() {
		return this.oauth;
	}

	
}
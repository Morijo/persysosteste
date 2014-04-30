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

import java.io.InputStream;
import java.util.List;

import com.restmb.exception.RestMBException;
import com.restmb.oauth.OAuth;
import com.restmb.oauth.service.ParameterList;
import com.restmb.oauth.service.Token;

public interface RestMBClient {
  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param object
   *          ID of the object to fetch, e.g. {@code "me"}.
   * @param objectType
   *          Object type token.
   * @param parameters
   *          URL parameters to include in the API call (optional).
   * @return An instance of type {@code objectType} which contains the requested object's data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T fetchObject(String object, Class<T> objectType, Parameter... parameters);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param object
   *          ID of the object to fetch, e.g. {@code "me"}.
   * @param objectType
   *          Object type token.
   * @param parameters
   *          URL parameters to include in the API call (optional).
   * @return An instance of type {@code objectType} which contains the requested object's data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
   InputStream fetchImage(String object, Parameter... parameters);

  
  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param ids
   *          IDs of the objects to fetch, e.g. {@code "me", "arjun"}.
   * @param objectType
   *          Object type token.
   * @param parameters
   *          URL parameters to include in the API call (optional).
   * @return An instance of type {@code objectType} which contains the requested objects' data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T fetchObjects(List<String> ids, Class<T> objectType, Parameter... parameters);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The name of the connection, e.g. {@code "me/feed"}.
   * @param connectionType
   *          Connection type token.
   * @param parameters
   *          URL parameters to include in the API call (optional).
   * @return An instance of type {@code connectionType} which contains the requested Connection's data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> Connection<T> fetchConnection(String connection, Class<T> connectionType,String root,ParameterList headers, Parameter... parameters);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The name of the connection, e.g. {@code "me/feed"}.
   * @param connectionType
   *          Connection type token.
   * @param parameters
   *          URL parameters to include in the API call (optional).
   * @return An instance of type {@code connectionType} which contains the requested Connection's data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> Connection<T> fetchConnection(String connection, Class<T> connectionType,String root, Parameter... parameters);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param connectionPageUrl
   *          The URL of the connection page to fetch, usually retrieved via {@link Connection#getPreviousPageUrl()} or
   *          {@link Connection#getNextPageUrl()}.
   * @param connectionType
   *          Connection type token.
   * @return An instance of type {@code connectionType} which contains the requested Connection's data.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> Connection<T> fetchConnectionPage(String connectionPageUrl, Class<T> connectionType);

  /**
   * Performs a <a href="http://developers.facebook.com/docs/api#publishing">Graph API publish</a> operation on the
   * given {@code connection}, mapping the result to an instance of {@code objectType}.
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The Connection to publish to.
   * @param objectType
   *          Object type token.
   * @param parameters
   *          URL parameters to include in the API call.
   * @return An instance of type {@code objectType} which contains the Facebook response to your publish request.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T publish(String connection, Class<T> objectType, Parameter... parameters);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The Connection to publish to.
   * @param objectType
   *          Object type token.
   * @param headers
   *          HTTP parameters to include in the API call.
   * @return An instance of type {@code objectType} which contains the response to your publish request.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T publish(String connection,Class<T> objectType, String body, ParameterList headers);

  /**
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The Connection to publish to.
   * @param objectType
   *          Object type token.
   * @param headers
   *          HTTP parameters to include in the API call.
   * @return An instance of type {@code objectType} which contains  response to your publish request.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T publishChanges(String connection,Class<T> objectType, String body, ParameterList headers);

  /**
   * Verb: PUT HTTP
   * 
   * @param <T>
   *          Java type to map to.
   * @param connection
   *          The Connection to publish to.
   * @param objectType
   *          Object type token.
   * @param binaryAttachment
   *          The file to include in the publish request.
   * @param parameters
   *          URL parameters to include in the API call.
   * @return An instance of type {@code objectType} which contains response to your publish request.
   * @throws RestMBException
   *           If an error occurs while performing the API call.
   */
  <T> T publish(String connection, Class<T> objectType, BinaryAttachment binaryAttachment, Parameter... parameters);

  /**
   * Verb: Delete HTTP
   * 
   * @param objectID
   *          The ID of the object to delete.
   * @param parameters
   *          URL parameters to include in the API call.
   * @return {@code true} if indicated that the object was successfully deleted, {@code false} otherwise.
   * @throws RestMBException
   *           If an error occurred while attempting to delete the object.
   */
  boolean deleteObject(String object, Parameter... parameters);

  /**
   * Verb: Delete HTTP
   * 
   * @param objectID
   *          The ID of the object to delete.
   * @param resource
   *          URL in the API call.
   * @return {@code true} if indicated that the object was successfully deleted, {@code false} otherwise.
   * @throws RestMBException
   *           If an error occurred while attempting to delete the object.
   */
  boolean deleteObject(String resource, Object objectID);


  /**
   * Obtains an access token which can be used to perform Graph API operations on behalf of an application instead of a
   * user.
   * @param oauth
   *          The oauth with tokens of the app.
   * @param url
   *          URL in the API call.
   * @return The token for the application identified by {@code appId} and {@code appSecret}.
   * @throws RestMBException
   *           If an error occurs while attempting to obtain an access token.
   * @since 1.6.10
   */
  public Token obtainAppRequestToken(OAuth oauth,String url);

  /**
   * Obtains an sign which can be used to perform Graph API operations on behalf of an application instead of a
   * user.
   * 
   * @param oauth
   *          The oauth of the app for which you'd like to obtain an access token.
   * @param url
   *          URL in the API call.
   * @return The token verif for the application identified by {@code appId} and {@code appSecret}.
   * @throws RestMBException
   *           If an error occurs while attempting to obtain an access token.
   * @since 1.6.10
   */
  public String sign(OAuth oauth, String url, String parameters);

  public Token obtainAppAccessToken(OAuth oauth, String url);

   /**
   * Gets the {@code JsonMapper} used to convert Facebook JSON to Java objects.
   * 
   * @return The {@code JsonMapper} used to convert Facebook JSON to Java objects.
   * @since 1.6.7
   */
  JsonMapper getJsonMapper();

  /**
   * Gets the {@code WebRequestor} used to talk to the Facebook API endpoints.
   * 
   * @return The {@code WebRequestor} used to talk to the Facebook API endpoints.
   * @since 1.6.7
   */
  WebRequestor getWebRequestor();

  /**
   * Represents an access token/expiration date pair.
   * <p>
   * Facebook returns these types when performing access token-related operations - see
   * {@link com.restmb.RestMBClient#convertSessionKeysToAccessTokens(String, String, String...)},
   * {@link com.restmb.RestMBClient#obtainAppAccessToken(String, String)}, and
   * {@link com.restmb.RestMBClient#obtainExtendedAccessToken(String, String, String)} for details.
   * 
   * @author <a href="http://restfb.com">Mark Allen</a>
   */
  
   void setOauth(OAuth oAuth);
   OAuth getOauth();
  
 
}
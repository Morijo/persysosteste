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
import static com.restmb.util.StringUtils.trimToEmpty;
import static com.restmb.util.UrlUtils.urlEncode;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import com.restmb.json.JsonException;
import com.restmb.json.JsonObject;

abstract class BaseClient {

	/**
   * Handles {@code GET}s and {@code POST}s to the Facebook API endpoint.
   */
  protected WebRequestor webRequestor;

  /**
   * Handles mapping Facebook response JSON to Java objects.
   */
  protected JsonMapper jsonMapper;

  /**
   * Set of parameter names that user must not specify themselves, since we use these parameters internally.
   */
  protected final Set<String> illegalParamNames = new HashSet<String>();

  /**
   * Set of API calls that can use the read-only endpoint for a performance boost.
   */
  protected final Set<String> readOnlyApiCalls = new HashSet<String>();

  /**
   * Legacy API error response 'error_code' attribute name.
   */
  protected static final String LEGACY_ERROR_CODE_ATTRIBUTE_NAME = "error_code";

  /**
   * Legacy API error response 'error_msg' attribute name.
   */
  protected static final String LEGACY_ERROR_MSG_ATTRIBUTE_NAME = "error_msg";

  /**
   * Reserved access token parameter name.
   */
  protected static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  /**
   * Logger.
   */
  protected final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes this Facebook client.
   */
  public BaseClient() {}

  
  protected Parameter[] parametersWithAdditionalParameter(Parameter parameter, Parameter... parameters) {
    Parameter[] updatedParameters = new Parameter[parameters.length + 1];
    System.arraycopy(parameters, 0, updatedParameters, 0, parameters.length);
    updatedParameters[parameters.length] = parameter;
    return updatedParameters;
  }

  /**
   * Given a map of query names to queries, verify that it contains valid data and convert it to a JSON object string.
   * 
   * @param queries
   *          The query map to convert.
   * @return The {@code queries} in JSON string format.
   * @throws IllegalArgumentException
   *           If the provided {@code queries} are invalid.
   */
  protected String queriesToJson(Map<String, String> queries) {
    verifyParameterPresence("queries", queries);

    if (queries.keySet().size() == 0)
      throw new IllegalArgumentException("You must specify at least one query.");

    JsonObject jsonObject = new JsonObject();

    for (Entry<String, String> entry : queries.entrySet()) {
      if (isBlank(entry.getKey()) || isBlank(entry.getValue()))
        throw new IllegalArgumentException("Provided queries must have non-blank keys and values. " + "You provided: "
            + queries);

      try {
        jsonObject.put(trimToEmpty(entry.getKey()), trimToEmpty(entry.getValue()));
      } catch (JsonException e) {
        // Shouldn't happen unless bizarre input is provided
        throw new IllegalArgumentException("Unable to convert " + queries + " to JSON.", e);
      }
    }

    return jsonObject.toString();
  }

  /**
   * Gets the URL-encoded version of the given {@code value} for the parameter named {@code name}.
   * <p>
   * Includes special-case handling for access token parameters where we check if the token is already URL-encoded - if
   * so, we don't encode again. All other parameter types are always URL-encoded.
   * 
   * @param name
   *          The name of the parameter whose value should be URL-encoded and returned.
   * @param value
   *          The value of the parameter which should be URL-encoded and returned.
   * @return The URL-encoded version of the given {@code value}.
   */
  protected String urlEncodedValueForParameterName(String name, String value) {
    // Special handling for access_token -
    // '%7C' is the pipe character and will be present in any access_token
    // parameter that's already URL-encoded. If we see this combination, don't
    // URL-encode. Otherwise, URL-encode as normal.
    return ACCESS_TOKEN_PARAM_NAME.equals(name) && value.contains("%7C") ? value : urlEncode(value);
  }

  /**
   * Given an api call (e.g. "me" or "fql.query"), returns the correct FB API endpoint to use.
   * <p>
   * Useful for returning the read-only API endpoint where possible.
   * 
   * @param apiCall
   *          The FB API call (Graph or Old REST API) for which we'd like an endpoint.
   * @param hasAttachment
   *          Are we including a multipart file when making this API call?
   * @return An absolute endpoint URL to communicate with.
   * @since 1.6.3
   */
  protected abstract String createEndpointForApiCall(String apiCall, boolean hasAttachment);

  /**
   * Verifies that the provided parameter names don't collide with the ones we internally pass along to Facebook.
   * 
   * @param parameters
   *          The parameters to check.
   * @throws IllegalArgumentException
   *           If there's a parameter name collision.
   */
  protected void verifyParameterLegality(Parameter... parameters) {
    for (Parameter parameter : parameters)
      if (illegalParamNames.contains(parameter.name))
        throw new IllegalArgumentException("Parameter '" + parameter.name + "' is reserved for RestFB use - "
            + "you cannot specify it yourself.");
  }

  /**
   * Ensures that {@code parameter} isn't {@code null} or an empty string.
   * 
   * @param parameterName
   *          The name of the parameter (to be used in exception message).
   * @param parameter
   *          The parameter to check.
   * @throws IllegalArgumentException
   *           If {@code parameter} is {@code null} or an empty string.
   */
  protected void verifyParameterPresence(String parameterName, String parameter) {
    verifyParameterPresence(parameterName, (Object) parameter);
    if (parameter.trim().length() == 0)
      throw new IllegalArgumentException("The '" + parameterName + "' parameter cannot be an empty string.");
  }

  /**
   * Ensures that {@code parameter} isn't {@code null}.
   * 
   * @param parameterName
   *          The name of the parameter (to be used in exception message).
   * @param parameter
   *          The parameter to check.
   * @throws IllegalArgumentException
   *           If {@code parameter} is {@code null}.
   */
  protected void verifyParameterPresence(String parameterName, Object parameter) {
    if (parameter == null)
      throw new NullPointerException("The '" + parameterName + "' parameter cannot be null.");
  }
}
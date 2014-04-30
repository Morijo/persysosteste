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

package com.restmb.exception;

/**
 * Indicates that the Graph API endpoint returned JSON which indicates an error condition.
 * <p>
 * Example:<code>
  {
      "error": {
        "type": "Exception",
        "message": "..."
      }
  } </code>
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 * @since 1.5
 */
public class RestMBGraphException extends RestMBException {
  /**
   * The Graph API error type.
   */
  private String errorType;

  /**
   * The API error message.
   */
  private String errorMessage;

  /**
   * The HTTP status code returned by the server.
   */
  private Integer httpStatusCode;

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception with the given error type and message.
   * 
   * @param errorType
   *          Value of the response attribute {@code error.type}.
   * @param errorMessage
   *          Value of the response attribute {@code error.message}.
   * @param httpStatusCode
   *          The HTTP status code returned by the server, e.g. 500.
   */
  public RestMBGraphException(String errorType, String errorMessage, Integer httpStatusCode) {
    super(errorMessage);
    this.errorType = errorType;
    this.errorMessage = errorMessage;
    this.httpStatusCode = httpStatusCode;
  }

  /**
   * Gets the Graph API error type.
   * 
   * @return The Graph API error type.
   */
  public String getErrorType() {
    return errorType;
  }

  /**
   * Gets the Graph API error message.
   * 
   * @return The Graph API error message.
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Gets the HTTP status code returned by the server.
   * 
   * @return The HTTP status code returned by the server.
   * @since 1.6.10
   */
  public Integer getHttpStatusCode() {
    return httpStatusCode;
  }
}
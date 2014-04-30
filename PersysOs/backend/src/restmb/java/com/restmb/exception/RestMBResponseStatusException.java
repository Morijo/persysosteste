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

import static java.lang.String.format;

/**
 * Indicates that the Legacy REST endpoint returned JSON which indicates an error condition.
 * <p>
 * This exception may also be thrown when executing certain operations against the Graph API, e.g. FQL queries or Batch
 * API calls.
 * <p>
 * Example: <code>{"error_code": 2, "error_msg": "The service is not available at this time.", ...}</code>.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
public class RestMBResponseStatusException extends RestMBException {
  /**
   * The API error code.
   */
  private Integer errorCode;

  /**
   * The API error message.
   */
  private String errorMessage;

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception with the given message and error code.
   * 
   * @param errorCode
   *          Value of the response attribute {@code error_code}.
   * @param errorMessage
   *          Value of the response attribute {@code error_msg}.
   */
  public RestMBResponseStatusException(Integer errorCode, String errorMessage) {
    super(format("Received error response (code %d): %s", errorCode, errorMessage));
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  /**
   * Gets the API error code.
   * 
   * @return The API error code.
   */
  public Integer getErrorCode() {
    return errorCode;
  }

  /**
   * Gets the API error message.
   * 
   * @return The API error message.
   */
  public String getErrorMessage() {
    return errorMessage;
  }
}
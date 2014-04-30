package com.restmb.batch;

import java.util.ArrayList;
import java.util.List;

import com.restmb.RestMB;

public class BatchResourceResponse {

	@RestMB
	public int response;
	@RestMB
	public List<ResponseBatch> data = new ArrayList<ResponseBatch>();
	
	public static class ResponseBatch {
		@RestMB
		private String key;
		@RestMB
		private String value;
		@RestMB
		private int status;

		public ResponseBatch() {
		}{}

		public ResponseBatch(String key,int status, String value) {
			super();
			this.key = key;
			this.status = status;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

}

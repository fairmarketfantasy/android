package com.fantasysport.webaccess;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class RequestSender {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	public static final int METHOD_PUT = 4;

	public static String makeRequest(String url, String httpBody, int method, ByteArrayOutputStream out) throws IOException {
		return executeRequest(url, httpBody, method, out);
	}

	private static String executeRequest(String url, String data, int method, ByteArrayOutputStream out) throws IOException {

		HttpClient hcon = null;
		DataInputStream dis = null;

		try {
			hcon = new DefaultHttpClient();
			HttpUriRequest request = getRequest(method, url, data, out);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Accept", "application/json");

			HttpResponse response = hcon.execute(request);

			int code = response.getStatusLine().getStatusCode();

			StringBuilder responseMessage = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8);
			try {
				char[] buffer = new char[4 * 1024];
				int readed = 0;
				while ((readed = reader.read(buffer, 0, buffer.length)) > 0){
					responseMessage.append(buffer, 0, readed);
				}
			} finally {
				reader.close();
			}

			return responseMessage.toString();
		} finally {
			try {
				if (hcon != null)
					hcon.getConnectionManager().shutdown();
				if (dis != null)
					dis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private static HttpUriRequest getRequest(int method, String url, String data, ByteArrayOutputStream out) throws UnsupportedEncodingException {
		switch (method) {
		case METHOD_GET:
			return new HttpGet(url);
		case METHOD_POST:
			HttpPost post = new HttpPost(url);
			if (data != null) {
				post.setEntity(new StringEntity(data, HTTP.UTF_8));
			}
			return post;
		case METHOD_PUT:
			HttpPut put = new HttpPut(url);
			if (data != null) {
				put.setEntity(new StringEntity(data, HTTP.UTF_8));
			}
			return put;
		default:
			return null;
		}
	}
}

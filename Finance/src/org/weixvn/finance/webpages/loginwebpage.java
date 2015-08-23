package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.weixvn.finance.LoginActivity;
import org.weixvn.http.AsyncWaeHttpClient;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.JsoupHttpRequestResponse;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;

import android.os.Message;

public class loginwebpage extends JsoupHttpRequestResponse {

	String URI = "http://cw.swust.edu.cn/LocalLogin.aspx";

	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		String username = (String) getHttpClient().getCache("username");
		String password = (String) getHttpClient().getCache("password");
		request.setRequestURI(URI);
		request.setRequestType(RequestType.POST);
		this.setCharset("GBK");
		request.getRequestParams().put("uid", username);
		request.getRequestParams().put("pwd", password);
		getHttpClient().addHeader(AsyncWaeHttpClient.HEADER_ACCEPT_ENCODING,
				AsyncWaeHttpClient.ENCODING_GZIP);
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, Document doc) {
	}


	public boolean analyse(Document doc) {
		
		String result = doc.getElementsByTag("title").text();
		if (result.equals("SSO")) {
			return false;
		} else if (result.equals("高校教育经济信息网")) {
			return true;
		}
		return false;
	}
	
}

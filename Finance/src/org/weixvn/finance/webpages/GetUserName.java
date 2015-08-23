package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;
import org.weixvn.http.JsoupHttpRequestResponse;

public class GetUserName extends JsoupHttpRequestResponse{

	String URI="http://cw.swust.edu.cn/baobiao/Queue/QueueController.aspx";
	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		request.setRequestURI(URI);
		request.setRequestType(RequestType.GET);
		setCharset("gb2312");
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, Document doc) {
		
	}

	public String analyse(Document doc) {
		Elements elements = doc.getElementsByTag("span");
		String name = elements.get(0).getElementsByTag("span").text();
		if (name.equals("用户：/"))
			return "请登陆";
		else
			return name;
	}
}

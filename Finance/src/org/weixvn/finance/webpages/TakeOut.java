package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;
import org.weixvn.http.JsoupHttpRequestResponse;

public class TakeOut extends JsoupHttpRequestResponse {

	String URI1 = "http://cw.swust.edu.cn/baobiao/Queue/QueueSystem.aspx?deptID=1&dateType=Today&timeType=AM";
	String URI2 = "http://cw.swust.edu.cn/baobiao/Queue/QueueSystem.aspx?deptID=1&dateType=NextDday&timeType=AM";
	boolean flag;

	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		flag = (Boolean) getHttpClient().getCache("timeType");
		if (flag) {
			request.setRequestURI(URI1);
		} else {
			request.setRequestURI(URI2);
		}
		request.setRequestType(RequestType.GET);
		setCharset("GBK");
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, Document arg2) {
		// TODO Auto-generated method stub

	}

	public String[] analyse(Document doc) {
		Element elements = doc.getElementsByTag("body").first();

		getHttpClient().putCache("__EVENTVALIDATION", elements.getElementById(
				"__EVENTVALIDATION").val());
		getHttpClient().putCache("__VIEWSTATE", elements
				.getElementById("__VIEWSTATE").val());

		// 获取 共取号[91]当前号[A058]等待人数[34] 两个
		Elements element = doc.getElementsByTag("span");
		String[] array = new String[2];
		if (flag) {
			if (!element.get(2).getElementsByTag("span").toString().equals("")) {
				array[0] = element.get(2).getElementsByTag("span").text();
			}

		}
		Elements element1 = doc.getElementsByTag("option");
		if (!element1.toString().equals("")) {
			array[1] = element1.first().text();
		}
		return array;
	}
}

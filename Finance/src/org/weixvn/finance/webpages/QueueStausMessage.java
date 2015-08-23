package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weixvn.http.AsyncWaeHttpClient;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.JsoupHttpRequestResponse;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;

public class QueueStausMessage extends JsoupHttpRequestResponse{
	String result[]=new String[10];
	String URI = "http://cw.swust.edu.cn/baobiao/Queue/QueueController.aspx?vid=Tue%20Apr%2008%202014%2020:22:33%20GMT+0800";
	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		request.setRequestURI(URI);
		request.setRequestType(RequestType.GET);
		this.setCharset("GBK");
		getHttpClient().addHeader(AsyncWaeHttpClient.HEADER_ACCEPT_ENCODING,
				AsyncWaeHttpClient.ENCODING_GZIP);
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, Document doc) {
		
	}

	public String[] analyze(Document doc) {
		Element content = doc.select("table").first();
		Elements elements = content.getElementsByTag("td");
		int i=0;
		for (Element elementtwo : elements) 
		{
			if(i!=0&&i!=3&&i!=6)
			{
		      result[i] = elements.get(i).getElementsByTag("td").text();
			}
			i++;
		}
		String[] array = new String[]{result[1],result[2],result[4],result[5],result[7],result[8]};
		return array;
	}
}

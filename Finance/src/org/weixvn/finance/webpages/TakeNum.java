package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;
import org.weixvn.http.JsoupHttpRequestResponse;

public class TakeNum extends JsoupHttpRequestResponse{

	private String URIToday = "http://cw.swust.edu.cn/baobiao/Queue/QueueSystem.aspx?deptID=1&dateType=Today&timeType=AM",
			URITomorrow = "http://cw.swust.edu.cn/baobiao/Queue/QueueSystem.aspx?deptID=1&dateType=NextDday&timeType=AM";

	private String deptID = "1", dateType, timeType = "AM", Txt_Yzm,__VIEWSTATE,__EVENTVALIDATION;
	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		boolean flag=(Boolean) getHttpClient().getCache("timeType");
		__EVENTVALIDATION=(String) getHttpClient().getCache("__EVENTVALIDATION");
		__VIEWSTATE=(String) getHttpClient().getCache("__VIEWSTATE");
		Txt_Yzm=(String) getHttpClient().getCache("Txt_Yzm");
		if (flag) {
			request.setRequestURI(URIToday);
			dateType = "Today";
		} else {
			request.setRequestURI(URITomorrow);
			dateType = "NextDday";
		}
		request.getRequestParams().put("__VIEWSTATE", __VIEWSTATE);;
		request.getRequestParams().put("__EVENTVALIDATION", __EVENTVALIDATION);
		request.getRequestParams().put("deptID", deptID);
		request.getRequestParams().put("timeType", timeType);
		request.getRequestParams().put("Repeater1$ctl00$ImageButton1.x", "136");
		request.getRequestParams().put("Repeater1$ctl00$ImageButton1.y", "17");
		request.getRequestParams().put("Txt_Yzm", Txt_Yzm);
		request.setRequestType(RequestType.POST);
		setCharset("GBK");
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, Document arg2) {
		// TODO Auto-generated method stub
		
	}
	public String analyse(Document doc) {
		String a;
		String array;
		Elements content = doc.select("SCRIPT");
		a = content.get(2).toString();
		int start, end;
		start = a.indexOf("alert('");
		end = a.indexOf("');");
		array = a.substring(start + 7, end);
		return array;
	}

}

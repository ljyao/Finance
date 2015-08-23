package org.weixvn.finance.webpages;

import org.apache.http.Header;
import org.weixvn.finance.ImagePreProcess;
import org.weixvn.http.AsyncWaeHttpRequest;
import org.weixvn.http.AsyncWaeHttpRequest.RequestType;
import org.weixvn.http.BinaryHttpRequestResponse;

import com.loopj.android.http.RequestParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetCode extends BinaryHttpRequestResponse {

	public static final String URI = "http://cw.swust.edu.cn/gif.aspx";
	public static final String TXT_YZM = "Txt_Yzm";
	
	public GetCode() {
		super(new String[] {
		        RequestParams.APPLICATION_OCTET_STREAM,
		        "image/jpeg",
		        "image/Png",
		        "image/gif"
		    });
	}

	@Override
	public void doResponse(int arg0, Header[] arg1, byte[] arg2) {
		this.getHttpClient().putCache(TXT_YZM, analyse(arg2));
	}

	@Override
	public void onRequest(AsyncWaeHttpRequest request) {
		request.setRequestURI(URI);
		request.setRequestType(RequestType.GET);
		getHttpClient().addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		getHttpClient().addHeader("Accept-Encoding","gzip, deflate");
		//getHttpClient().addHeader(AsyncWaeHttpClient.HEADER_ACCEPT_ENCODING,
			//	AsyncWaeHttpClient.ENCODING_GZIP);
	}
	
	public String analyse(byte[] responseBody) {
		Bitmap Finalbitmap=BitmapFactory.decodeByteArray(responseBody, 0,
				responseBody.length);
		String code = null;
			try {
				code = ImagePreProcess.getAllOcr(Finalbitmap);
				System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊" + code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
	}

}

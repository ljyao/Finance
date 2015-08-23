package org.weixvn.finance;

import android.content.Context;

public class Common {
	// 自定义进度条
	private MyProgressDialog progressDialog;

	/**
	 * 启动进度条
	 */
	public void showProgressDialog(Context con) {
		progressDialog = new MyProgressDialog(con);
		progressDialog.show();
		progressDialog.setMessage("请稍等");
	}

	
	public void cancelProgressDialog() {
		progressDialog.cancel();
	}
}

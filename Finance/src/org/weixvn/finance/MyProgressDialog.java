package org.weixvn.finance;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class MyProgressDialog extends ProgressDialog {

	private TextView message;

	/**
	 * 自定义进度条的构造函数
	 * 
	 * @param context
	 *            context
	 */
	public MyProgressDialog(Context context) {
		super(context);
	}

	/**
	 * 设置内容
	 */
	@Override
	public void setMessage(CharSequence message) {
		super.setMessage(message);
		this.message.setText(message);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.progress_dialog);
		this.message = (TextView) findViewById(R.id.progress_dialog_message);
	}

}

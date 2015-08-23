package org.weixvn.finance;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.weixvn.finance.webpages.GetCode;
import org.weixvn.finance.webpages.TakeNum;
import org.weixvn.finance.webpages.TakeOut;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

public class ServiceTypeActivity extends Activity {
	public static Common dialog = new Common();
	private TextView auto, titele;
	public String  tip_array;
	public  int count = 0;
	private Button back;
	private TextView view[] = new TextView[3];
	public  AlertDialog.Builder builder;
	private boolean tip_diaglog_cancel = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.servicetype);

		builder = new AlertDialog.Builder(ServiceTypeActivity.this);
		builder.setTitle("提示");
		builder.setCancelable(false);

		titele = (TextView) findViewById(R.id.stitlemain);
		back = (Button) findViewById(R.id.backmain);
		back.setOnClickListener(new blisten());
		auto = (TextView) findViewById(R.id.auto);
		auto.setOnClickListener(new blisten());
		init();

		Boolean datetype =(Boolean) LoginActivity.httpClient.getCache("timeType");
		if (datetype) {
			titele.setText("取当日号");
		} else  {
			titele.setText("取次日号");
		}
	}

	private void init() {
		
		dialog.showProgressDialog(ServiceTypeActivity.this);
		
		LoginActivity.httpClient.execute(new TakeOut() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, Document doc) {
				String array[] = analyse(doc);
				dialog.cancelProgressDialog();
				view[0] = (TextView) findViewById(R.id.teacher_show);
				view[1] = (TextView) findViewById(R.id.text_queueNum);
				view[0].setText(array[0]);
				if (array[1]!=null) {
					view[1].setText(array[1]);
				}
				LoginActivity.httpClient.execute(new GetCode() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] binaryData) {
						dialog.cancelProgressDialog();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] binaryData, Throwable error) {
						dialog.cancelProgressDialog();
						super.onFailure(statusCode, headers, binaryData, error);
					}

				});
				super.onSuccess(statusCode, headers, doc);
			}

		});
		
	}
	private void request() {
		LoginActivity.httpClient.execute(new TakeNum(){

			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(int statusCode, Header[] headers, Document doc) {
				
				String array=analyse(doc);
				tip_array=array + "\n" + "正在尝试第" + count + "次抢号";
				count++;
				dialog.cancelProgressDialog();
				if (!tip_diaglog_cancel)
				{
					showDialog(1);
					request();
				}
				super.onSuccess(statusCode, headers, doc);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Document doc, Throwable throwable) {
				String array = null;
				try {
					array=analyse(doc);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				tip_array=array + "\n" + "正在尝试第" + count + "次抢号";
				count++;
				dialog.cancelProgressDialog();
				if (!tip_diaglog_cancel)
				{
					showDialog(1);
					request();
				}
				super.onFailure(statusCode, headers, doc, throwable);
			}
			
		});
	}

	private class blisten implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.auto:
				count = 0;
				tip_diaglog_cancel = false;
				dialog.showProgressDialog(ServiceTypeActivity.this);
				request();
				break;
			case R.id.backmain:
				finish();
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			tip_diaglog_cancel=true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件
		StatService.onEvent(ServiceTypeActivity.this, "2", "取号", 1);
		// 自定义事件开始
		StatService.onEventStart(ServiceTypeActivity.this, "2", "取号");
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件结束
		StatService.onEventEnd(ServiceTypeActivity.this, "2", "取号");
		StatService.onPause(this);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {

			builder.setPositiveButton("停止抢号",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							tip_diaglog_cancel = true;
						}
					});
			builder.setMessage(tip_array);
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		if (id == 1) {
			AlertDialog dialog1 = (AlertDialog) dialog;
			dialog1.setMessage(tip_array);
		}
		super.onPrepareDialog(id, dialog);
	}

}

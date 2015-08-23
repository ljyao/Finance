package org.weixvn.finance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

public class SetActivity extends Activity {

	private RelativeLayout suggestLayout;
	private RelativeLayout recommendLayout;
	private RelativeLayout set_cancel;
	private Button set_back;
	AlertDialog.Builder builder;
	private String FILE = "saveUserNamePwd";// 用于保存SharedPreferences的文件
	private SharedPreferences sp = null;// 声明一个SharedPreferences

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);

		set_back = (Button) findViewById(R.id.backmain);
		set_cancel = (RelativeLayout) findViewById(R.id.cancelLayout);
		suggestLayout = (RelativeLayout) findViewById(R.id.suggestLayout);
		recommendLayout = (RelativeLayout) findViewById(R.id.recommendLayout);
		
		Listener listener = new Listener();
		suggestLayout.setOnClickListener(listener);
		recommendLayout.setOnClickListener(listener);
		set_cancel.setOnClickListener(listener);
		set_back.setOnClickListener(listener);
	}

	class Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.backmain) {
				SetActivity.this.finish();
				SetActivity.this.overridePendingTransition(
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			} else if (v.getId() == R.id.suggestLayout) {
				launchFeedbackUi(SetActivity.this, "财务处叫号系统", "6");
			} else if (v.getId() == R.id.recommendLayout) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
				intent.putExtra(Intent.EXTRA_TEXT,
						"嗨，我正在使用@这款插件，感觉非常好用，你也来试试吧，在安卓市场就可以下载。");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, "财务处叫号系统"));
				SetActivity.this.overridePendingTransition(
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}else if(v.getId() == R.id.cancelLayout){
				cancel();
			}else if (v.getId() == R.id.backmain) {
				finish();
			}
		}
	}
	
	/**
	 * 注销账号
	 */
	public void cancel() {
		builder = new AlertDialog.Builder(SetActivity.this);
		builder.setTitle("提示");
		builder.setMessage("确定要注销？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				Toast.makeText(SetActivity.this,
						getResources().getString(R.string.set_cancel_success),
						Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent();
				intent.setClass(SetActivity.this, LoginActivity.class);
				SetActivity.this.startActivity(intent);
				      
				//清空httpclient
				LoginActivity.httpClient.cancelAllRequests(true);
				LoginActivity.httpClient.clearCache();
				//清空SharedPreferences里的信息
				if (sp == null) {
					sp = getSharedPreferences(FILE, MODE_PRIVATE);
				}
				Editor edit = sp.edit();
				edit.putString("isMemory", "NO");
				edit.commit();
				MainActivity.main.finish();
				SetActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
	public void launchFeedbackUi(Context context, String title,
			String schoolSysId) {
		ComponentName com = new ComponentName("org.weixvn.frame",
				"org.weixvn.frame.FeedbackActivity");

		Intent intent = new Intent();

		intent.putExtra("title", title);
		intent.putExtra("Plugin_id", schoolSysId);
		intent.setComponent(com);
		context.startActivity(intent);
		SetActivity.this.overridePendingTransition(
				android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SetActivity.this.finish();
			SetActivity.this.overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//自定义事件
		StatService.onEvent(SetActivity.this, "ID","Name", 1);
		//自定义事件开始
		StatService.onEventStart(SetActivity.this,"ID","Name");
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//自定义事件结束
		StatService.onEventEnd(SetActivity.this,"ID","Name");
		StatService.onPause(this);
	}
}

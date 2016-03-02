package com.hulian.evenhandle.pagerdetail;

import com.hulian.evenhandle.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.btn_login)
	private Button Login_btn;
	@ViewInject(R.id.edit_uid)
	private EditText username;
	@ViewInject(R.id.tv_psw)
	private EditText password;
	private TextWatcher mTextWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay2);
		initView();
		}

	private void initView() {
		Login_btn.setOnClickListener(this);
		 mTextWatcher = new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (username.getText().toString().trim().length() > 0
							&& password.getText().toString().trim().length() > 0) {
						Login_btn.setEnabled(true);
					} else {
						Login_btn.setEnabled(false);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}

				@Override
				public void afterTextChanged(Editable arg0) {
				}
			};
		
	}

	@Override
	       public void onClick(View v) {
		   switch (v.getId()) {
		   case R.id.login_button:
			   String userName=username.getText().toString();
			   String passWord=password.getText().toString();

			break;

	default:
			break;
		}
	}

}

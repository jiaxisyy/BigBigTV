package com.share_will.mobile.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.share_will.mobile.utils.Utils;

public class ConfirmDialog extends Dialog implements android.view.View.OnClickListener {
	int width;
	private TextView confirmTitle, confirmMsg;
	private Button submitBtn, cancelBtn;
	private Context context;
	/**中间的分割线**/
    private View lineView;
	public ConfirmDialog(Context context) {
		super(context, R.style.Style_Dialog);
		width = Utils.getScreenWidth(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.confirm_dialog, null);
		setContentView(view);
		confirmTitle = view.findViewById(R.id.dialog_titel);
		confirmMsg = view.findViewById(R.id.dialog_msg);
		submitBtn = view.findViewById(R.id.dialog_sumbit_btn);
		cancelBtn = view.findViewById(R.id.dialog_cancel_btn);
		lineView = view.findViewById(R.id.config_line);
		submitBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = (int) (width * 0.8); // 宽度
		dialogWindow.setAttributes(lp);
		this.onWindowFocusChanged(false);
		this.setCancelable(false);
	}
	public ConfirmDialog setSubmitText(String sureStr){
		if (submitBtn!=null){
			submitBtn.setText(sureStr);
		}
		return this;
	}
	public ConfirmDialog setSubmitTextColor(int corlorId){
		if (submitBtn!=null){
			submitBtn.setTextColor(context.getResources().getColor(corlorId));
		}
		return this;
	}
	public ConfirmDialog setTitleColor(int corlorId){
		if (confirmTitle!=null){
			confirmTitle.setTextColor(context.getResources().getColor(corlorId));
		}
		return this;
	}
	public ConfirmDialog setCancerBtTextColor(int corlorId){
		if (cancelBtn!=null){
			cancelBtn.setTextColor(context.getResources().getColor(corlorId));
		}
		return this;
	}
	public ConfirmDialog setCancerBtText(String string){
		if (cancelBtn!=null){
			cancelBtn.setText(string);
		}
		return this;
	}
	public ConfirmDialog setContentColor(int corlorId){
		if (confirmMsg!=null){
			confirmMsg.setTextColor(context.getResources().getColor(corlorId));
		}
		return this;
	}
	public void hidenTitle(){
		if (confirmTitle!=null) {
			confirmTitle.setVisibility(View.GONE);
		}	
	}
	public ConfirmDialog setConfirmTitle(String title){
		confirmTitle.setText(title);
		return this;
	}
	public ConfirmDialog setConfirmMessage(String message){
		confirmMsg.setText(message);
		return this;
	}

	ConfirmOnClickListener confirmOnClickListener = null;

	public ConfirmOnClickListener getConfirmOnClickListener() {
		return confirmOnClickListener;
	}

	public void setConfirmOnClickListener(ConfirmOnClickListener confirmOnClickListener) {
		this.confirmOnClickListener = confirmOnClickListener;
	}

    /**
     * 修改样式 只有一个关闭按钮
     */
	public void changeStytle(){
        if(lineView!=null){
			lineView.setVisibility(View.GONE);
        }
        if(submitBtn!=null){
            submitBtn.setVisibility(View.GONE);
        }
    }

	public interface ConfirmOnClickListener {
		void sumBitOnClickListener();

		void cancelOnClickListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_sumbit_btn:
			if (confirmOnClickListener != null)
				confirmOnClickListener.sumBitOnClickListener();
			break;
		case R.id.dialog_cancel_btn:
			if (confirmOnClickListener != null)
				confirmOnClickListener.cancelOnClickListener();
			break;

		default:
			break;
		}
		dismiss();
	}

}

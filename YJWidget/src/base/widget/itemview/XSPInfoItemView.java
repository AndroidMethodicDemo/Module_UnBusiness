package base.widget.itemview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import base.util.chat.XSFaceUtil;
import base.util.ui.XSSoftKeyboard;

import com.example.demoxswidget.R;

/**
 * 第一次写可能麻烦，但是利用复用
 */
public class XSPInfoItemView extends LinearLayout {
	private boolean contentIsSingleLine = true;
	
	private Drawable rightImgDrawable;
	private String contentTxtStr, noteTxtStr, leftTxtStr;
	private boolean isEditMode, isJumpMode, isShowLeft;
	
	private EditText contentEdt;
	private TextView contentTxtView, noteTxtView, leftSingleTextView;
	private ImageButton rightImgBtn;
	private LinearLayout rightFrameLayout;
	
	private View twoLableView;
	private View xspInfoItemView;

	private OnInfoItemClickEvent mInfoItemOnclickEvent;

	private boolean isShowFaceEnable = true;
	
	public void setOnInfoItemClickEvent(
			OnInfoItemClickEvent infoItemOnclickEvent) {
		this.mInfoItemOnclickEvent = infoItemOnclickEvent;
	}

	public void setShowFaceEnable(boolean isShowFaceEnable) {
		this.isShowFaceEnable = isShowFaceEnable;
	}

	public XSPInfoItemView(Context context) {
		this(context, null);
	}

	public XSPInfoItemView(Context context, AttributeSet attrs) {
		//this(context, attrs, -1);
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.infoItem);
        // 右图片
		rightImgDrawable = a.getDrawable(R.styleable.infoItem_rightImg);
		// 内容
		contentTxtStr = a.getString(R.styleable.infoItem_contentLabel);
		// 注释
		noteTxtStr = a.getString(R.styleable.infoItem_noteLabel);
		// 文本
		leftTxtStr = a.getString(R.styleable.infoItem_leftSingleLable);
		// 是否编辑模式
		isEditMode = a.getBoolean(R.styleable.infoItem_editMode, false);
		// 是否跳转模式
		isJumpMode = a.getBoolean(R.styleable.infoItem_jumpMode, true);
		// 是否显示左
		isShowLeft = a.getBoolean(R.styleable.infoItem_showSingleLeft, false);
		
		xspInfoItemView = LayoutInflater.from(context).inflate(R.layout.base_xsp_view_002_info_item, this, true);
		a.recycle();
	}
	//-------------------------------------------------------------初始化开始-----------------------------------------------------------
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		contentTxtView = (TextView) findViewById(R.id.txt_content);
        contentEdt = (EditText) findViewById(R.id.edt_content);
        noteTxtView = (TextView) findViewById(R.id.txt_note);
        leftSingleTextView = (TextView) findViewById(R.id.left_single_text);
        rightImgBtn = (ImageButton) findViewById(R.id.img_edit);
        
        rightFrameLayout = (LinearLayout) findViewById(R.id.right_layout);
        twoLableView = (LinearLayout) findViewById(R.id.left_two_lable_view);
		initContentTxt();

		initNoteTxt();
        
		initLeftSingleTxt();
		
		initRightLayout();	
	}
	
	private void initContentTxt() {
	    CharSequence csContentStr = "";
	    if (!TextUtils.isEmpty(contentTxtStr)) {
	    	if (isShowFaceEnable) {
	    		 csContentStr = XSFaceUtil.getInstance(getContext()).getFaceString(
	 	                contentTxtStr, XSFaceUtil.MIDDLE_SIZE_DRAWABLE);
	    	} else {
	    		csContentStr = contentTxtStr;
	    	}
	       
	    }
	    
		if (isEditMode) {
		    contentEdt.setText(csContentStr);
		    contentEdt.setSelection(csContentStr.length());
		    contentEdt.setVisibility(View.VISIBLE);
		    contentTxtView.setVisibility(View.GONE);
		} else {
		    contentTxtView.setText(csContentStr);
		    contentEdt.setVisibility(View.GONE);
		    contentTxtView.setVisibility(View.VISIBLE);
		}

	}

	private void initNoteTxt() {
		if (!TextUtils.isEmpty(noteTxtStr)) {
			noteTxtView.setText(noteTxtStr);
		} else {
			noteTxtView.setVisibility(View.GONE);
		}
	}
	
	private void initLeftSingleTxt(){
		if (isShowLeft) {
			twoLableView.setVisibility(View.GONE);
			leftSingleTextView.setVisibility(View.VISIBLE);
			leftSingleTextView.setText(leftTxtStr);
		}
		
	}
	
	private void initRightLayout(){
		if (null != rightImgDrawable) {
			rightImgBtn.setImageDrawable(rightImgDrawable);
			rightImgBtn.setVisibility(View.VISIBLE);
			setOnClickEvent(xspInfoItemView);
		}		
	}
	
	private void setOnClickEvent(View view) {
		if (!isJumpMode) {
			setContentHeightAdatptiveWhenClick(view);
			return;
		}
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == mInfoItemOnclickEvent) {
					return;
				}

				// 右侧按钮
				mInfoItemOnclickEvent.onRightClick(v);
			}
		});
	}
	//------------------------------------------------初始化结束，设置开始------------------------------------------------------
	public void setContentTxt(String contentStr) {
		if (null == contentStr) {
			return;
		}
		contentTxtStr = contentStr;
		initContentTxt();
	}
	
	public void setNoteTxt(String noteStr) {
	    if (null == noteTxtView || TextUtils.isEmpty(noteStr)) {
	        return;
	    }
	    
	    noteTxtView.setText(noteStr);
	    noteTxtView.setVisibility(View.VISIBLE);
	}

	public void setEditMode(boolean isEditMode) {
	    this.isEditMode = isEditMode;
	    contentTxtStr = contentEdt.getText().toString().trim();
	    initContentTxt();
	}
	
	public void setJumpMode(boolean isJumpMode){
		this.isJumpMode = isJumpMode;
	}
	//------------------------------------------------设置结束，View的setter、getter方法------------------------------------------------------
	public void setEditTextRequestFocus() {
	    if (isEditMode) {
	        contentEdt.requestFocus();
	        XSSoftKeyboard.showKeyboard(contentEdt);
	    }
	}
	
	public void setEditTextInputType(int inputType) {
	    if (isEditMode && inputType > 0) {
	        contentEdt.setInputType(inputType);
	    }
	}
	
	public EditText getEditText() {
	    return contentEdt;
	}
	
	public void setRightImg(int rightImgResId) {
		if (null != rightImgBtn && 0 < rightImgResId) {
			rightImgBtn.setImageResource(rightImgResId);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
	}
    	
	/**
	 * 前提content布局是单行显示的
	 */
	private void setContentHeightAdatptiveWhenClick(View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == contentTxtView)
				    return;
				contentTxtView.setSingleLine(!contentIsSingleLine);
				contentIsSingleLine = !contentIsSingleLine;
			}
		});
	}
	
	public interface OnInfoItemClickEvent {
		public void onRightClick(View view);
	}

	public TextView getContentTxtView() {
		return contentTxtView;
	}

	public void setContentTxtView(TextView contentTxtView) {
		this.contentTxtView = contentTxtView;
	}
	
	public void setBigContentTxtView(){
		contentTxtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
	}

	public EditText getContentEdt() {
		return contentEdt;
	}

	public void setContentEdt(EditText contentEdt) {
		this.contentEdt = contentEdt;
	}

	public ImageButton getRightImgBtn() {
		return rightImgBtn;
	}

	public void setRightImgBtn(ImageButton rightImgBtn) {
		this.rightImgBtn = rightImgBtn;
	}
	
	public void setRightImgBtnInvisible() {
		rightImgBtn.setVisibility(View.INVISIBLE);
	}
	
	public void setRightImgBtnVisible() {
		rightImgBtn.setVisibility(View.VISIBLE);
	}
	
	public void setLeftSingleLine(){
		twoLableView.setVisibility(View.GONE);
	    leftSingleTextView.setVisibility(View.VISIBLE);
		isShowLeft = true;
			
	}
	
	public void setLeftSingleText(String singleTxt){
		leftSingleTextView.setText(singleTxt);
	}
	
	public void setLeftTwoLines(){
		twoLableView.setVisibility(View.GONE);
		leftSingleTextView.setVisibility(View.VISIBLE);
		isShowLeft = false;
	}
	
	public boolean isShowSingle(){
		return isShowLeft;
	}
	
	public void setRightLayout(View view, LinearLayout.LayoutParams params){
		rightFrameLayout.removeAllViews();
		rightFrameLayout.addView(view, params);
	}

}


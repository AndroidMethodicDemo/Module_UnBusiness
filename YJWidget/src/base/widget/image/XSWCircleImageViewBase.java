package base.widget.image;

import com.example.demoxswidget.R;

import base.util.ui.XSBitmapUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形图片控件基类
 * @author zWX202297
 *
 */
public abstract class XSWCircleImageViewBase extends ImageView {
	
	
	public XSWCircleImageViewBase(Context context) {
		this(context, null);
	}

	public XSWCircleImageViewBase(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XSWCircleImageViewBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	/**
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		
		Bitmap bitmap = null;
		if (drawable instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		}

		if(null == bitmap){
			return;
		}
		
		Bitmap roundBitmap = XSBitmapUtil.cropCircleBitmap(bitmap);
		Bitmap scaleBitmap = XSBitmapUtil.imageScale(roundBitmap, getImageWidth(), getImageHeight());
		
		canvas.drawBitmap(scaleBitmap, 0, 0, null);
	}
	
	/**
	 * 
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		initImageSize();
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		initImageSize();
		super.setImageBitmap(bm);
	}
	
	@Override
	public void setImageResource(int resId) {
		initImageSize();
		super.setImageResource(resId);
	}
	/**
	 * 有什么用呢？
	 * 
	 */
	protected void initImageSize(){
		if(null == getLayoutParams()) return;
		
		getLayoutParams().width = getImageWidth();
		getLayoutParams().height = getImageHeight();
	}
	
	Drawable mDrawable;
	
	protected int getImageWidth(){
		if(null == mDrawable){
			mDrawable = getPlaceHolderDrawable();
		}
		return mDrawable.getIntrinsicWidth();
	}
	
	protected int getImageHeight(){
		if(null == mDrawable){
			mDrawable = getPlaceHolderDrawable();
		}
		return mDrawable.getIntrinsicHeight();
	}
	
	/**
	 * 获取占位Drawable 资源ID
	 * @return
	 */
	protected int getDrawableResourceID() {
		return R.drawable.main_002_default_contact_head;
	}
	/**
	 * 获取默认drawable
	 * @return
	 */
	protected Drawable getPlaceHolderDrawable(){
		return getResources().getDrawable(getDrawableResourceID());
	}
}

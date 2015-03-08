package base.util.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import base.util.ui.XSDisplayUtil;

import com.example.demoxswidget.R;

public class XSFaceUtil {
	private static final String TAG = "FaceUtil";
	private static XSFaceUtil mFaceUtil = null;
	private final String[] FACE_STRING_ARRAY;
	private HashMap<String, Integer> mFaceIndexMap;
	private Pattern mPattern;
	public final int FACE_KEY_MAX_LENGTH;
	private static final String WHITESPACE = " ";

	private Drawable mDeleteDrawable;
	
	private Context mContext = null;
	private List<LayerDrawable> mLayerDrawableList = new ArrayList<LayerDrawable>();
	private static final int[] SIZE_DRAWABLE_ARRAY = new int[3];
	public static final int SMALL_SIZE_DRAWABLE = 0;
	public static final int MIDDLE_SIZE_DRAWABLE = 1;
	public static final int LARGE_SIZE_DRAWABLE = 2;
	
	private static final int SMALL_DRAWABLE_BOUND_DIP = 20;
	private static final int MIDDLE_DRAWABLE_BOUND_DIP = 22;
	private static final int LARGE_DRAWABLE_BOUND_DIP = (int)(24 * 1.7f);
	
	/**
	 * @param context 
	 */
	private XSFaceUtil(Context context){
		mContext = context;
		initLayerDrawable();
		
		//if (GsmaUtils.isForGsma()) {
		//	FACE_STRING_ARRAY = mContext.getResources().getStringArray(R.array.face_key_gsma);
		//} else {
			FACE_STRING_ARRAY = mContext.getResources().getStringArray(R.array.face_key);
		//}
		mFaceIndexMap = new HashMap<String, Integer>(FACE_STRING_ARRAY.length*2);
		
		StringBuilder patternString = new StringBuilder(FACE_STRING_ARRAY.length * 6);
		patternString.append('(');
		int maxKeyLength = 0;
		for (int i = 0; i < FACE_STRING_ARRAY.length; i++) {
			FACE_STRING_ARRAY[i] = FACE_STRING_ARRAY[i].trim();
			if (FACE_STRING_ARRAY[i].length() > maxKeyLength) {
				maxKeyLength = FACE_STRING_ARRAY[i].length();
			}
			mFaceIndexMap.put(FACE_STRING_ARRAY[i], i);
			patternString.append(Pattern.quote(FACE_STRING_ARRAY[i]));
            patternString.append('|');
		}
		FACE_KEY_MAX_LENGTH = WHITESPACE.length() + maxKeyLength + WHITESPACE.length();
		patternString.replace(patternString.length() - 1, patternString.length(), ")");
		mPattern = Pattern.compile(patternString.toString());
		
//		LogApi.d(TAG, "patternString=" + patternString);
//		LogApi.d(TAG, "mFaceIndexMap=" + mFaceIndexMap);
	}
	
	public static XSFaceUtil getInstance(Context context){
		if(null == mFaceUtil){
			mFaceUtil = new XSFaceUtil(context);
		}
		
		return mFaceUtil;
	}
	
	/**
	 * 鑾峰彇琛ㄦ儏涓暟
	 */
	public int getFaceCount(){
		return mLayerDrawableList.get(0).getNumberOfLayers();
	}
	
	private String getKey(int index){
		String key = null;
		if(index < FACE_STRING_ARRAY.length) {
			key = FACE_STRING_ARRAY[index];
		}
		return key;
	}
	
	/**
	 * 鑾峰緱鍒犻櫎鍥炬爣
	 * @return
	 */
	public Drawable getDeleteFace(){
		return mDeleteDrawable;
	}
	
	public int getDrawableBoundHeight(){
		return SIZE_DRAWABLE_ARRAY[LARGE_SIZE_DRAWABLE];
	}
	
	/**
	 * 鏍规嵁缁欏畾鑼冨洿锛岃幏鍙栬〃鎯�
	 */
	public ArrayList<Drawable> getFaceList(int start, int end){
		ArrayList<Drawable> list = new ArrayList<Drawable>();
		for(int i=start; i<end && i<getFaceCount(); i++){
			Drawable drawable = getLayerDrawable(LARGE_SIZE_DRAWABLE).getDrawable(i);
			if(drawable != null){
				list.add(drawable);
			}
		}
		
		return list;
	}
	
	private CharSequence parseFace(Spannable content, int sizeType){
		if (content == null) {
			return "";
		}
		
		if(sizeType < 0 || sizeType >= SIZE_DRAWABLE_ARRAY.length){
			sizeType = MIDDLE_SIZE_DRAWABLE;
		}
		
        Matcher matcher = mPattern.matcher(content);
        while (matcher.find()) {
        	String faceKey = matcher.group();
        	Drawable faceDrawable = getLayerDrawable(sizeType).getDrawable(mFaceIndexMap.get(faceKey));
        	faceDrawable.setVisible(true, true);
        	ImageSpan imageSpan = new ImageSpan(faceDrawable);
        	
        	int start = matcher.start();
        	int end = matcher.end();
        	//濡傛灉琛ㄦ儏涓よ竟閮芥湁绌烘牸锛屽垯鎶� "+ facekey+" "鏄剧ず涓哄浘鐗囷紝鍚﹀垯鐩存帴鎶奻acekey鏄剧ず涓哄浘鐗�
        	if(hasTrim(content.toString(), start, end)){
        		start -= WHITESPACE.length();
        		end += WHITESPACE.length();
        	}
        	if(start >=0 && end <= content.length()){
        		content.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        	}
        }
        
        return content;
	}
	
	public CharSequence getFaceString(String content, int sizeType){
		if (content == null) {
			return "";
		}
		
		Spannable conentSpannable = Spannable.Factory.getInstance().newSpannable(content);
		return parseFace(conentSpannable, sizeType);
	}
	public CharSequence getFaceString(String content, boolean isDraft){
		if (content == null) {
			return "";
		}
		Spannable conentSpannable = Spannable.Factory.getInstance().newSpannable(content);
		return getFaceString(conentSpannable, isDraft);
	}
	
	public CharSequence getFaceString(Spannable content, boolean isDraft){
		if (content == null) {
			return "";
		}
		if (isDraft) {
//			String draftStr = mContext.getString(R.string.str_messaging_type_draft_with_bracket_003_008);
			String draftStr = "草稿";
			content = Spannable.Factory.getInstance().newSpannable(draftStr + " " + content);
			content.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.grassgreen)), 0, draftStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
        return parseFace(content, MIDDLE_SIZE_DRAWABLE);
	}
	/**
	 * 鍦╡ditable涓彃鍏ヨ〃鎯�
	 * @param editable
	 * @param insertPosition
	 * @param faceIndex
	 */
	public boolean addFace(Editable editable, int insertPosition, int faceIndex){
		String faceKey = getKey(faceIndex);
//		LogApi.d(TAG, "addFace() faceKey=" + faceKey);
		if(null != faceKey){
			String fontString;
			String backString = WHITESPACE;
			if(editable.length() == 0 || isFaceAtCursorPositon(editable.toString(), insertPosition)){
				fontString = WHITESPACE;
			}else{
				fontString = WHITESPACE + WHITESPACE;
			}
			editable.insert(insertPosition, fontString + faceKey + backString);
			return true;
		}
		
		return false;
	}
	
	public void delete(Editable editable, int position) {
		if (position <= 0) {
			return;
		}
		
		int startIndex = position > FACE_KEY_MAX_LENGTH ? position - FACE_KEY_MAX_LENGTH : 0;
		CharSequence subString = editable.subSequence(startIndex, position);
		Matcher matcher = mPattern.matcher(subString);
		if (matcher.find()) {
			int start = matcher.start() + startIndex;
			int end = matcher.end() + startIndex;
        	if(hasTrim(editable.toString(), start, end)){//濡傛灉琛ㄦ儏涓插墠鍚庢湁WHITESPACE锛屽氨鎶婂墠鍚嶹HITESPACE涔熷垹鎺�
        		start -= WHITESPACE.length();
        		end += WHITESPACE.length();
        	}
			if (end == position) {//浠ヨ〃鎯呯粨灏�
				editable.delete(start, end);
			} else {
				editable.delete(position - 1, position);
			}
			
		} else {
			editable.delete(position - 1, position);
		}
	}
	
	//鍒ゆ柇琚尮閰嶅埌鐨勮〃鎯呭瓧绗︿覆鍓嶅悗鏄惁鏈塛HITESPACE
	private boolean hasTrim(String editTextContent, int startIndex, int endIndex){
		if(editTextContent == null || WHITESPACE == null){
			return false;
		}
		
		if(startIndex - WHITESPACE.length() < 0 || endIndex + WHITESPACE.length() > editTextContent.length()){
			return false;
		}
		
		if(WHITESPACE.equals(editTextContent.subSequence(startIndex-WHITESPACE.length(), startIndex)) 
				&& WHITESPACE.equals(editTextContent.subSequence(endIndex, endIndex+WHITESPACE.length()))){
			return true;
		}
		
		return false;
	}
	
	//鍒ゆ柇鍦ㄥ厜鏍囩殑浣嶇疆鍓嶉潰鏄惁鏄〃鎯呯
	private boolean isFaceAtCursorPositon(String editTextContent, int cursorPosition){
		if (cursorPosition <= 0 || TextUtils.isEmpty(editTextContent)) {
			return false;
		}
		
		int startIndex = cursorPosition > FACE_KEY_MAX_LENGTH ? cursorPosition - FACE_KEY_MAX_LENGTH : 0;
		CharSequence subString = editTextContent.subSequence(startIndex, cursorPosition);
		Matcher matcher = mPattern.matcher(subString);
		if (matcher.find()) {
			int end = matcher.end();
			if (end+startIndex == cursorPosition 
					|| (end+startIndex+WHITESPACE.length() == cursorPosition && subString.toString().endsWith(WHITESPACE)) ) {
				return true;
			}
		}
		
		return false;
	}
	
	private void initLayerDrawable(){
			SIZE_DRAWABLE_ARRAY[SMALL_SIZE_DRAWABLE] = XSDisplayUtil.dip2px(
					mContext, SMALL_DRAWABLE_BOUND_DIP);
			SIZE_DRAWABLE_ARRAY[MIDDLE_SIZE_DRAWABLE] = XSDisplayUtil.dip2px(
					mContext, MIDDLE_DRAWABLE_BOUND_DIP);
			SIZE_DRAWABLE_ARRAY[LARGE_SIZE_DRAWABLE] = XSDisplayUtil.dip2px(
					mContext, LARGE_DRAWABLE_BOUND_DIP);
		
		for(int i=0; i<SIZE_DRAWABLE_ARRAY.length; i++){
			LayerDrawable layerDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.base_layerlist_001_face);
			for(int j=0; j<layerDrawable.getNumberOfLayers(); j++){
				layerDrawable.getDrawable(j).setBounds(0, 0, SIZE_DRAWABLE_ARRAY[i], SIZE_DRAWABLE_ARRAY[i]);
			}
			mLayerDrawableList.add(layerDrawable);
		}
		
		mDeleteDrawable = mContext.getResources().getDrawable(R.drawable.im_face_delete);
		mDeleteDrawable.setBounds(0, 0, SIZE_DRAWABLE_ARRAY[LARGE_SIZE_DRAWABLE], SIZE_DRAWABLE_ARRAY[LARGE_SIZE_DRAWABLE]);
	}
	
	private LayerDrawable getLayerDrawable(int sizeType){
		return mLayerDrawableList.get(sizeType);
	}
}
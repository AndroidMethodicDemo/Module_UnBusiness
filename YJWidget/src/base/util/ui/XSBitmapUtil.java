package base.util.ui;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

public class XSBitmapUtil
{
    private static final String TAG = "BitmapTools";
    
    private static String IMAGE_TYPT_BMP = "bmp";
    private static String IMAGE_TYPT_JPG = "jpg";
    private static String IMAGE_TYPT_JPEG = "jpeg";
    private static String IMAGE_TYPT_PNG = "png";
    private static String IMAGE_TYPT_GIF = "gif";
    
    private static double MAX_IMAGE_VALUE = 240.0;
    
    public static final int PICTURE_QUALITY_HIGH = 100;
	public static final int PICTURE_QUALITY_NORMAL = 60;
    
    private static DisplayMetrics dm = new DisplayMetrics();
    private static int sysWidth = dm.widthPixels;
	private static int sysHight = dm.heightPixels;

	private static long  COMPRESS_IMAGE_FILE_SIZE_LIMIT = 250*1024;//250k
   
    private static double  COMPRESS_IMAGE_OVER_BIG_LIMIT = 2000.0;
    private static double  COMPRESS_IMAGE_OVER_SMALL_LIMIT = 250.0;
    
	private static int compressWidth = 720;
	private static int compressHeight = 1280;
	
	public static final String COMPRESS_IMAGE_FAILED = "compress_image_failed";
    /**
     * 解析图片的属性，返回 
     */
    public static BitmapFactory.Options getBitmapOptions(String path) {
    	BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		return opts;
    }
    
    public static Bitmap adjustImage(String path)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int maxValue = srcWidth > srcHeight ? srcWidth : srcHeight;
        //缩放的比例
        double ratio = MAX_IMAGE_VALUE / maxValue;
        
        Bitmap compressedBitmap = null;
        try
        {
            Bitmap thumbnailBitmap = getThumbnail(path,
                    (int) (ratio * srcWidth),
                    (int) (ratio * srcHeight));
            if (null == thumbnailBitmap)
            {
                return null;
            }
            BitmapFactory.Options tempOpts = new BitmapFactory.Options();
            tempOpts.inJustDecodeBounds = false;
            tempOpts.inSampleSize = 1;
            
            tempOpts.inPurgeable = true;
            tempOpts.inInputShareable = true;
            tempOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;
            compressedBitmap = ThumbnailUtils.extractThumbnail(thumbnailBitmap,
                    (int) (ratio * srcWidth),
                    (int) (ratio * srcHeight),
                    1);
        }
        catch (Error e)
        {
            e.printStackTrace();
            return null;
        }
        
        return compressedBitmap;
    }
    /**
     * 传入图片全路径，压缩该图片，返回Bitmap对象
     * @param filePath		要压缩的文件全路径
     * @param targetW		压缩后的宽度
     * @param targetH		压缩后的高度
     * @return
     * 其实targetH没有用，代码写的不严谨
     */
    public static Bitmap getThumbnail(String filePath, int targetW, int targetH)
    {
        Bitmap bitmap = null;
        
        File file = new File(filePath);
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") < 0)// 如果文件名中没有 . 
        {
            return bitmap;//return null;
        }
        
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);// 获取后缀名
        
        if (fileExtension.equalsIgnoreCase(IMAGE_TYPT_BMP)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_JPG)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_JPEG)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_PNG)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_GIF))
        {
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);// 解码图片，获取图片宽高
            int bitmapRealWidth = options.outWidth;
            int bitmapRealHeight = options.outHeight;
            options.outWidth = targetW;// 将传入的宽度作为options的宽度
            if (bitmapRealWidth > 0)
            {
                options.outHeight = bitmapRealHeight * targetW
                        / bitmapRealWidth;// 算出options的高度
            }
            
            options.inJustDecodeBounds = false;
            
            if (targetW > 0)
            {
                options.inSampleSize = bitmapRealWidth / targetW;
            }
            
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            
            bitmap = BitmapFactory.decodeFile(filePath, options);// 将文件转换为bitmap对象
        }// 如果后缀是图片格式的后缀
        
        return bitmap;
    }
    /**
     * 不是很明白这个方法的作用
     * 旋转图片
     * @param bitmap
     * @param picturePath
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, String picturePath) {
    	int degree = 0;
        try
        {
        	if (picturePath == null)
            {
                return null;
            }
        	// This is a class for reading and writing Exif tags in a JPEG file.
            ExifInterface exif = new ExifInterface(picturePath);
            // 获取方位
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                {
                    degree = 90;
                }
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                {
                    degree = 180;
                }
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                {
                    degree = 270;
                }
                    break;
                default:
                {
                    degree = 0;
                }
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        if (0 != degree)
        {
        	// 矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    matrix,
                    false);
        }
        return bitmap;
    }
    
    @SuppressWarnings("deprecation")
	public static Bitmap scaleBitmap(int id,Activity activity)
    {
        Bitmap _bitmapPreScale = BitmapFactory.decodeResource(activity.getResources(),
                id);
        
        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int normalStatusBarHeight = rectgle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
        int TitleBarHeight = contentViewTop - normalStatusBarHeight;
        
        
        int statusBarHeight = (int) Math.ceil(25 * activity.getResources().getDisplayMetrics().density);
        int oldWidth = _bitmapPreScale.getWidth();
        int oldHeight = _bitmapPreScale.getHeight();
        int newWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int newHeight = activity.getWindowManager().getDefaultDisplay().getHeight()
                - statusBarHeight;
        
        if (isHoneycombTablet(activity))
        {
            newHeight = activity.getWindowManager().getDefaultDisplay().getHeight()
                    - normalStatusBarHeight;
        }
        
        // calculate the scale
        float scaleWidth = ((float) newWidth) / oldWidth;
        float scaleHeight = ((float) newHeight) / oldHeight;
        
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        
        // recreate the new Bitmap
        return Bitmap.createBitmap(_bitmapPreScale,
                0,
                0,
                oldWidth,
                oldHeight,
                matrix,
                true);
    }
    
    public static boolean isTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout 
                & Configuration.SCREENLAYOUT_SIZE_MASK) 
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    public static boolean isHoneycombTablet(Context context)
    {
        return isHoneycomb() && isTablet(context);
    }
    
    public static boolean isHoneycomb()
    {
        // Can use static final constants like HONEYCOMB, declared in later
        // versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= 11;
    }
    
    public static Bitmap createBitMap(String picturePath, int i, int j) {
		int degree = 0;
        try
        {
        	if (picturePath == null)
            {
                return null;
            }
            ExifInterface exif = new ExifInterface(picturePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				degree = 0;
			}
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Bitmap thumbnail = XSBitmapUtil.adjustImage(picturePath);
        if (thumbnail == null)
        {
            return null;
        }
        if (0 != degree)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            thumbnail = Bitmap.createBitmap(thumbnail,
                    0,
                    0,
                    thumbnail.getWidth(),
                    thumbnail.getHeight(),
                    matrix,
                    false);
        }
        return thumbnail;
	}
    
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @return 如果不需要压缩，返回true；如果需要压缩，返回false
	 */
	protected static boolean isNeedCompress(int srcWidth, int srcHeight, int maxValue, int minValue, long size){
		boolean result = false;
		if (srcWidth <= sysWidth && srcHeight <= sysHight
				&& size <= COMPRESS_IMAGE_FILE_SIZE_LIMIT) {
			// 图片二边小于屏幕两边，不需要压缩
			result = true;
		}
		if (size <= COMPRESS_IMAGE_FILE_SIZE_LIMIT && maxValue <= COMPRESS_IMAGE_OVER_BIG_LIMIT) {
			// 文件小于规定大小 最大边小于规定
			result = true;
		}
		
		if (size > COMPRESS_IMAGE_FILE_SIZE_LIMIT && (minValue <= COMPRESS_IMAGE_OVER_SMALL_LIMIT)
				&& (maxValue <= COMPRESS_IMAGE_OVER_BIG_LIMIT)) {
			// 如果最小边小于规定并且最大边小于规定，则不需要压缩
			result = true;
		}
		return result;
	}
	/**
	 * 通过ExifInterface类，返回图片的角度
	 */
	protected static int getBitmapAngle(String path, int degree) {
		try {
			if (path == null) {
				degree = 0;
			}
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default: 
				degree = 0;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
    
    /**
	 * 计算需要压缩的长宽值
	 * @param srcWidth 图片宽
	 * @param srcHeight 图片高
	 * @param sysWidth 屏幕宽
	 * @param sysHight 屏幕高
	 * @return 计算结果:index 0: 宽 , 1: 高
	 */
	protected static int[] calculateCompressSize(int srcWidth, int srcHeight,	int sysWidth, int sysHight) {
		 int maxValue = srcWidth > srcHeight ? srcWidth : srcHeight; 
	     int minValue = srcWidth < srcHeight ? srcWidth : srcHeight;
	     int compressImageWidth = srcWidth > srcHeight ? compressHeight : compressWidth; 
	     int compressImageHeight = srcWidth > srcHeight ? compressWidth : compressHeight;
		//计算需要压缩的长宽值
		 int resultWidth = 0,resultHeight = 0;
		 //计算长宽值算法规则
		 //1 定最长边，短边小于最短值符合 
		 //2否则 定最短边，长边小于最长边大于屏幕，符合
		 //3否则 定屏幕宽，高度小于屏幕高，符合
		 //4否则 定屏幕高。
		// 1 定长边
		int smallLength = (int) (minValue * COMPRESS_IMAGE_OVER_BIG_LIMIT / maxValue);// 根据规定的长边计算短边
		if (maxValue > COMPRESS_IMAGE_OVER_BIG_LIMIT && smallLength < COMPRESS_IMAGE_OVER_SMALL_LIMIT) {
			if (maxValue == srcHeight) {
				resultHeight = (int) COMPRESS_IMAGE_OVER_BIG_LIMIT;
				resultWidth = smallLength;
			} else {
				resultWidth = (int) COMPRESS_IMAGE_OVER_BIG_LIMIT;
				resultHeight = smallLength;
			}
			return new int[] { resultWidth, resultHeight };
		}

		// 2 定短边
		int longLength = (int) (maxValue * COMPRESS_IMAGE_OVER_SMALL_LIMIT / minValue);// 根据规定的短边计算长边
		if (longLength <= COMPRESS_IMAGE_OVER_BIG_LIMIT
				&& ((maxValue == srcHeight && longLength > compressImageHeight) || (maxValue == srcWidth && longLength > compressImageWidth))) {
			if (maxValue == srcHeight) {
				resultHeight = longLength;
				resultWidth = (int) COMPRESS_IMAGE_OVER_SMALL_LIMIT;
			} else {
				resultWidth = longLength;
				resultHeight = (int) COMPRESS_IMAGE_OVER_SMALL_LIMIT;
			}
			return new int[] { resultWidth, resultHeight };
		}

		// 3定宽
		if (srcWidth >= compressImageWidth) {
			resultWidth = compressImageWidth;
			resultHeight = srcHeight * compressImageWidth / srcWidth;
			if (resultHeight <= compressImageHeight) {
				return new int[] { resultWidth, resultHeight };
			}
		}

		// 4定高
		if (srcHeight >= compressImageHeight) {
			resultHeight = compressImageHeight;
			resultWidth = srcWidth * compressImageHeight / srcHeight;
			return new int[] { resultWidth, resultHeight };
		}
		return new int[] { srcWidth, srcHeight };
	}
	

	
	/**
     * 根据UI显示要求调整图片大小
     * @param path
     * @param width
     * @param hight
     * @return
     */
    public static Bitmap adjustDisplayImage(String path,int width,int hight)
    {
        BitmapFactory.Options opts = getBitmapOptions(path);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;   
        Log.d(TAG, "adjustImage image size:" + srcWidth + "*"
                + srcHeight); 
        
        Bitmap compressedBitmap = null;
        Bitmap thumbnailBitmap = null;
        try
        {
            thumbnailBitmap = getThumbnail(path,width,hight);
            if (null == thumbnailBitmap)
            {
                return null;
            }
            BitmapFactory.Options tempOpts = new BitmapFactory.Options();
            tempOpts.inJustDecodeBounds = false;
            tempOpts.inSampleSize = 1;
            
            tempOpts.inPurgeable = true;
            tempOpts.inInputShareable = true;
            tempOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;
            compressedBitmap = ThumbnailUtils.extractThumbnail(thumbnailBitmap,width,hight,1);
            Log.d(TAG, "adjustDisplayImage image success..."); 
        }
        catch (Error e)
        {
        	Log.d(TAG, "adjustDisplayImage image error..."); 
            
            return null;
        }finally {
        	if (thumbnailBitmap != null) {
        		thumbnailBitmap.recycle();
        		thumbnailBitmap = null;
        	}
        }
        
        return compressedBitmap;
    }
    

    /**
     * get the video duration
     * @param filePath the path of the video file 
     * @return the video duration in second
     */
    @TargetApi(10)
	public static int getVideoDuration(String filePath) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(filePath);
		String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		retriever.release();
		
		return Integer.parseInt(durationStr)/1000;//exchange millisecond to second
	}
	
	 /**
     * get the first frame bitmap
     * @param filePath the path of the video file 
     * @param width the bitmap width
     * @param height the bitmap height
     * @return the first frame bitmap
     */
	@TargetApi(10)
	public static Bitmap getVideoFirstImage(String filePath, int width, int height) {
		Log.d(TAG, "getVideoImageAndDuration() file:"+ filePath);
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(filePath);
		Bitmap outBitmap = retriever.getFrameAtTime();
		outBitmap = ThumbnailUtils.extractThumbnail(outBitmap, width, height, 1);
		retriever.release();
		
		return outBitmap;
	}
	

	/**
     * 根据给出的路径看图片是否存在
     * @param path
     * @return
     */
	protected static boolean fileIsExists(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return true;
	}
	

	
	/**
	 * 裁剪正方形图片
	 * cropCircleBitmap用到了此方法
	 * @param bitmap
	 * @return
	 */
    public static Bitmap cropSquareBitmap(Bitmap bitmap) {
    	int oldWidth = bitmap.getWidth();
		int oldHeight = bitmap.getHeight();
    	
		boolean isWidthLarger = oldWidth > oldHeight;
    	int newWidth = isWidthLarger ? oldHeight : oldWidth;
		int newHeight = isWidthLarger ? oldHeight : oldWidth;
		
		int retX = oldWidth == newWidth ? 0 : (oldWidth - newWidth)/2;
        int retY = oldWidth == newWidth ? (oldHeight - newHeight)/2 : 0;
        return Bitmap.createBitmap(bitmap, retX, retY, newWidth, newHeight, null, false);
    }
	public static Bitmap imageScale(Bitmap bitmap, int dstWidth, int dstHeight) {
		if (null == bitmap) return null;
		
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();
        if (0 == srcWidth || 0 == srcHeight) {
        	return bitmap;
        }
            
		float scaleWidth = ((float) dstWidth) / srcWidth * 1.0f;
		float scaleHeight = ((float) dstHeight) / srcHeight * 1.0f;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight,
				matrix, true);

		return dstbmp;
	}
    
    /**
     * 图片圆形化处理
     * @param bitmap
     * @return
     */
	public static Bitmap cropCircleBitmap(Bitmap bitmap) {
		Bitmap bmp = cropSquareBitmap(bitmap);
		Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(bmp.getWidth() / 2, bmp.getHeight() / 2 , bmp.getWidth() / 2 , paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bmp, rect, rect, paint);

		return output;
	}
}

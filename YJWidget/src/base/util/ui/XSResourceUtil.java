package base.util.ui;

import android.content.Context;

/**
 * 
 * 用于获取资源ID，避免R类的引用
 * 
 * @version  [V1.0.0, 2013-4-27]
 */
public class XSResourceUtil {

	public static int getLayoutId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "layout",
				ctx.getPackageName());
	}

	public static int getStringId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "string",
				ctx.getPackageName());
	}

	public static int getDrawableId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "drawable",
				ctx.getPackageName());
	}

	public static int getStyleId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "style",
				ctx.getPackageName());
	}

	public static int getId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "id",
				ctx.getPackageName());
	}

	public static int getColorId(Context ctx, String name) {
		return ctx.getResources().getIdentifier(name, "color",
				ctx.getPackageName());
	}

	public static String getResourceName(Context ctx, int resid) {
		return ctx.getResources().getResourceName(resid);
	}
}
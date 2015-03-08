package base.widget.rect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
/**
 * Rect位于android.graphics下，表示一个矩形，由四条边的坐标组成，提供了一些设置方法，都比较简单
 */
public class DemoRect extends View {
    Paint paint = new Paint();
    Rect r1;
    Rect r2;
    Rect r3;
 
    public DemoRect(Context context) {
        super(context);
        init();
    }
 
    private void init() {
        // 两个构造方法
        r1 = new Rect(10, 10, 100, 100);
        r2 = new Rect(new Rect(110, 110, 170, 170));
 
        // 判断是否相等，重写了equals
        Log.i("equals", r1.equals(r2) + "");
 
        // 重写了toString
        Log.i("toString", r1.toString());
 
        Log.i("toShortString", r1.toShortString());
        Log.i("flattenToString", r1.flattenToString());
 
        r3 = Rect.unflattenFromString("180 180 280 280");
 
        // 判断是否为空
        Log.i("isEmpty", r1.isEmpty() + "");
 
        // 宽
        Log.i("width", r1.width() + "");
 
        // 高
        Log.i("height", r1.height() + "");
 
        // 中心X坐标
        Log.i("centerX", r1.centerX() + "");
 
        // 中心Y坐标
        Log.i("centerY", r1.centerY() + "");
 
        // 精确的中心X坐标
        Log.i("exactCenterX", r1.exactCenterX() + "");
 
        // 精确的中心Y坐标
        Log.i("exactCenterY", r1.exactCenterY() + "");
 
        // 重置为空
        r2.setEmpty();
 
        // 设置坐标
        r2.set(40, 40, 70, 70);
        printCoordinates(r2);
 
        // 使用已有的Rect进行设置
        r2.set(r1);
        printCoordinates(r2);
 
        // 偏移
        r1.offset(20, 20);
        printCoordinates(r1);
 
        r1.offsetTo(40, 40);
        printCoordinates(r1);
 
        r1.inset(20, 30);
        printCoordinates(r1);
 
        // 判断包含性
        Log.i("contains", r1.contains(20, 20) + "");
        Log.i("contains", r1.contains(20, 20, 40, 40) + "");
        Log.i("contains", r1.contains(r2) + "");
 
        // 判断相交
        Log.i("intersect", r1.intersect(30, 30, 30, 30) + "");
        Log.i("intersect", r1.intersect(r2) + "");
        Log.i("intersects", r1.intersects(30, 30, 30, 30) + "");
        Log.i("intersects", Rect.intersects(r2, r3) + "");
 
        // 取相交部分
        Log.i("setIntersect", r1.setIntersect(r2, r3) + "");
 
        // 取并
        r1.union(r2);
        printCoordinates(r1);
        r1.union(30, 30);
        printCoordinates(r1);
        r1.union(20, 30, 30, 30);
        printCoordinates(r1);
 
        // 交换
        r1.sort();
        printCoordinates(r1);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(r1, paint);
        canvas.drawRect(r2, paint);
        canvas.drawRect(r3, paint);
    }
 
    /**
     * 打印坐标
     *
     * @param rect
     */
    private void printCoordinates(Rect rect) {
        Log.i("left", rect.left + "");
        Log.i("top", rect.top + "");
        Log.i("right", rect.right + "");
        Log.i("bottom", rect.bottom + "");
    }
}

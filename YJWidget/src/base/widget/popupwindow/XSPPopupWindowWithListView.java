package base.widget.popupwindow;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.demoxswidget.R;

public class XSPPopupWindowWithListView extends PopupWindow{
    private Context context;
    
    private String clickedItemStr;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList;
    
    private OnInfoItemClick onItemClick;

    public XSPPopupWindowWithListView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }
    
    public XSPPopupWindowWithListView(Context context, List<String> dataList) {
        super(context);
        this.context = context;
        this.dataList = dataList;
        init(context);
    }
    
    private void init(Context context) {
        initData(dataList);
    }
    
    public void initData(List<String> dataList) {
        if (null == dataList || 0 == dataList.size()) return;
        View view = LayoutInflater.from(context).inflate(R.layout.base_xsp_dialog_007_with_listview, null);
        listView = (ListView) view.findViewById(R.id.popupwindow_listview);
        
        adapter = new ArrayAdapter<String>(context, R.layout.base_xsp_item_001_textview, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                clickedItemStr = (String) parent.getItemAtPosition(position);
                XSPPopupWindowWithListView.this.dismiss();
                if (null != onItemClick) {
                    onItemClick.OnItemClicked(clickedItemStr);
                }
            }
            
        });
        
        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
        // 设置XSPDownUpPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置XSPDownUpPopupWindow背景动画
        this.setAnimationStyle(R.style.AnimFade);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置XSPDownUpPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
    
    public void addItem(String itemStr) {
        if (null == itemStr || null == adapter) return;
        
        adapter.add(itemStr);
        adapter.notifyDataSetChanged();
    }
    
    public void removeItem(String itemStr) {
        if (null == itemStr || null == adapter) return;
        
        adapter.remove(itemStr);
        adapter.notifyDataSetChanged();
    }
    
    public void clearDatas() {
        if (null != adapter) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }
    
    public void setOnItemClickListener(OnInfoItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    
    public interface OnInfoItemClick {
        public void OnItemClicked(String clickedItemStr);
    }
}

package base.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.demoxswidget.R;

public class XSPTimePickerDialog extends Dialog {
    private int selectedHourOfDay;
    private int selectedMinute;
    // 分钟
    private String[] minuteArray = new String[] { "00", "15", "30", "45" };
    private TimePicker timePicker;
    
    private OnTimeChangedCallback mCallback;

    public XSPTimePickerDialog(Context context, OnTimeChangedCallback callback) {
        super(context);
        this.mCallback = callback;
        initUI();
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        this.setContentView(getContentVIew());
    }

    /**
     * 分钟数必须是15的倍数,即0,15,30,45
     * @param hour
     * @param minute
     */
    public void initData(int hour, int minute) {
        if (0 != minute % 15 || null == timePicker) return;
        
        timePicker.setCurrentHour(hour);
        if (Build.VERSION.SDK_INT >= 11 ) {
            timePicker.setCurrentMinute(minute / 15);
        } else {
            timePicker.setCurrentMinute(minute);
        }
    }
    
    private View getContentVIew() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.base_xsp_dialog_008_with_timepicker, null);
        
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) view.findViewById(R.id.btn_confirm);
        timePicker = (TimePicker) view.findViewById(R.id.dialog_timePicker);
        cancelBtn.setOnClickListener(cancelListener);
        confirmBtn.setOnClickListener(confirmListener);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(timeChangedListener);
        
        if (Build.VERSION.SDK_INT >= 11 ) {
            setMinSpanOfTimePicker(timePicker);
        }
        
        return view;
    }

    private OnTimeChangedListener timeChangedListener = new OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            if (Build.VERSION.SDK_INT >= 11 ) {
                selectedHourOfDay = hourOfDay;
                selectedMinute = Integer.valueOf(minuteArray[minute]);
                
            } else {
                if (45 < minute) {
                    selectedHourOfDay = hourOfDay + 1;
                    selectedMinute = 0; 
                } else {
                    selectedHourOfDay = hourOfDay;
                    int i = (int)Math.ceil(((double)minute)/15);
                    selectedMinute = 15 * i; 
                }
            }
        }
    };
    
    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    
    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (null != mCallback) {
                mCallback.onTimeChanged(selectedHourOfDay, selectedMinute);
            }
        }
    };
    
    /**
     *  查找timePicker里面的android.widget.NumberPicker组件 ，并对其进行时间间隔设置
     * 
     * @param viewGroup
     */
    @SuppressLint("NewApi")
    private void setMinSpanOfTimePicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = findNumberPicker(viewGroup);
        
        if (null != npList) {
            NumberPicker mMinuteSpinner = npList.get(1); // 第0个hour、第1个是minute
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue(minuteArray.length - 1);
            mMinuteSpinner.setDisplayedValues(minuteArray); // 分钟显示数组
        }
    }

    /**
     * 得到timePicker里面的android.widget.NumberPicker组件
     * （前两个android.widget.NumberPicker组件--hour，minute）
     * 
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;

        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }

        return npList;
    }
    
    public interface OnTimeChangedCallback {
        void onTimeChanged(int hourOfDay, int minute);
    }

}

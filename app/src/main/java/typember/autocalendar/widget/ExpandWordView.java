package typember.autocalendar.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CheckBox;

import typember.autocalendar.R;
/**
 * Created by nono0 on 2016/11/22.
 */

public class ExpandWordView extends CheckBox {
    public ExpandWordView(Context context, String text, int i) {
        super(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundResource(R.drawable.checkbox_selector);
        setButtonDrawable(null);
        setLayoutParams(params);
        setId(i);//为控件编号，便于在AutoExpandLinearLayout中调用其Text
        setText(text);
        setTextColor(Color.BLACK);
        setTextSize(18);
    }
}

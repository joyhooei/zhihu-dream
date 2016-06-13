package party.danyang.zhihunewsbydream.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yaki on 16-6-9.
 */
public class UnlimitedMarqueeTextVew extends TextView {
    public UnlimitedMarqueeTextVew(Context context) {
        super(context);
    }

    public UnlimitedMarqueeTextVew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnlimitedMarqueeTextVew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public UnlimitedMarqueeTextVew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility==VISIBLE){
            super.requestFocus();
        }
    }
}

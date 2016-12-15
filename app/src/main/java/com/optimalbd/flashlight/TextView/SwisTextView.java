package com.optimalbd.flashlight.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by optimal on 09-Jun-16.
 */
public class SwisTextView extends TextView {

    public static Typeface m_typeFace = null;

    public SwisTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        if (isInEditMode()) {
            return;
        }
        loadTypeFace(context);
    }

    public SwisTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        loadTypeFace(context);
    }

    public SwisTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        loadTypeFace(context);
    }

    private void loadTypeFace(Context context) {
        if (m_typeFace == null)
            m_typeFace = Typeface.createFromAsset(context.getAssets(),
                    "fonts/swis.ttf");
        this.setTypeface(m_typeFace);
    }
}

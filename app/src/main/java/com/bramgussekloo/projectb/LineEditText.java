package com.bramgussekloo.projectb;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class LineEditText extends AppCompatEditText {
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        getLineHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {



        super.onDraw(canvas);
    }
}

package com.bramgussekloo.projectb.Activities;

import android.support.design.widget.VisibilityAwareImageButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;

public class ProductEditActivity extends AppCompatActivity implements View.OnTouchListener,  GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = "ProductEditActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // UI
    private EditText mEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;


    // Variabelen
    private boolean mIsNewProduct;
    private Product mInitialProduct;
    private GestureDetector mGestureDetector;
    private int nMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        mEditText = findViewById(R.id.product_text);
        mEditTitle = findViewById(R.id.product_edit_title);
        mViewTitle = findViewById(R.id.product_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        if(getIncomingIntent()){
            // new product -> (edit mode)
            setNewProductProperties();
            enableEditMode();
        }
        else{
            // not new product -> (view mode)
            setProductProperties();
        }

        setListeners();



        


    }

    private void setListeners(){
        mEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
    }

    // Checks if its a new product
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_product")){
            mInitialProduct = getIntent().getParcelableExtra("selected_product");

            nMode = EDIT_MODE_DISABLED;
            mIsNewProduct = false;

            return false;
        }
        nMode = EDIT_MODE_ENABLED;
        mIsNewProduct = true;
        return true;
    }

    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        nMode = EDIT_MODE_ENABLED;
    }

    private void disableEditMode(){
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        nMode = EDIT_MODE_DISABLED;
    }

    private void setProductProperties(){
        mViewTitle.setText(mInitialProduct.getName());
        mEditTitle.setText(mInitialProduct.getName());
        mEditText.setText(mInitialProduct.getContent());

    }

    private void setNewProductProperties(){
        mViewTitle.setText("Product Title");
        mEditTitle.setText("Product Title");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: Double tap");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}

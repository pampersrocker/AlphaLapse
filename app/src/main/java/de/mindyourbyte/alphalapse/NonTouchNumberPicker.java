package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Button;

import com.sony.scalar.sysutil.ScalarInput;
public class NonTouchNumberPicker extends Button {

    public NonTouchNumberPicker(Context context) {
        super(context);
        updateText();
    }

    public NonTouchNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NonTouchNumberPicker);
        initializeAttributes(attributes);
        updateText();
    }

    public NonTouchNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NonTouchNumberPicker, defStyle, 0);
        initializeAttributes(attributes);
        updateText();
    }

    private void initializeAttributes(TypedArray attributes) {
        MinValue = attributes.getInteger(R.styleable.NonTouchNumberPicker_minValue, Integer.MIN_VALUE + 1);
        MaxValue = attributes.getInteger(R.styleable.NonTouchNumberPicker_maxValue, Integer.MAX_VALUE - 1);
        Postfix = attributes.getString(R.styleable.NonTouchNumberPicker_postfix);
    }

    private int Value;
    private int MaxValue = Integer.MAX_VALUE - 2;
    private int MinValue = Integer.MIN_VALUE + 1;
    private String Postfix = "";



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getScanCode()) {
            case ScalarInput.ISV_KEY_UP:
                return onUpKeyDown();
            case ScalarInput.ISV_KEY_DOWN:
                return onDownKeyDown();
            case ScalarInput.ISV_KEY_LEFT:
                return onLeftKeyDown();
            case ScalarInput.ISV_KEY_RIGHT:
                return onRightKeyDown();
            case ScalarInput.ISV_KEY_ENTER:
                return onEnterKeyDown();
            case ScalarInput.ISV_KEY_FN:
                return onFnKeyDown();
            case ScalarInput.ISV_KEY_AEL:
                return onAelKeyDown();
            case ScalarInput.ISV_KEY_MENU:
            case ScalarInput.ISV_KEY_SK1:
                return onMenuKeyDown();
            case ScalarInput.ISV_KEY_S1_1:
                return onFocusKeyDown();
            case ScalarInput.ISV_KEY_S1_2:
                return true;
            case ScalarInput.ISV_KEY_S2:
                return onShutterKeyDown();
            case ScalarInput.ISV_KEY_PLAY:
                return onPlayKeyDown();
            case ScalarInput.ISV_KEY_STASTOP:
                return onMovieKeyDown();
            case ScalarInput.ISV_KEY_CUSTOM1:
                return onC1KeyDown();
            case ScalarInput.ISV_KEY_DELETE:
            case ScalarInput.ISV_KEY_SK2:
                return onDeleteKeyDown();
            case ScalarInput.ISV_KEY_LENS_ATTACH:
                return onLensAttached();
            case ScalarInput.ISV_DIAL_1_CLOCKWISE:
            case ScalarInput.ISV_DIAL_1_COUNTERCW:
                return onUpperDialChanged(getDialStatus(ScalarInput.ISV_DIAL_1_STATUS) / 22);
            case ScalarInput.ISV_DIAL_2_CLOCKWISE:
            case ScalarInput.ISV_DIAL_2_COUNTERCW:
                return onLowerDialChanged(getDialStatus(ScalarInput.ISV_DIAL_2_STATUS) / 22);
            case ScalarInput.ISV_KEY_MODE_DIAL:
                return onModeDialChanged(getDialStatus(ScalarInput.ISV_KEY_MODE_DIAL));
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (event.getScanCode()) {
            case ScalarInput.ISV_KEY_UP:
                return onUpKeyUp();
            case ScalarInput.ISV_KEY_DOWN:
                return onDownKeyUp();
            case ScalarInput.ISV_KEY_LEFT:
                return onLeftKeyUp();
            case ScalarInput.ISV_KEY_RIGHT:
                return onRightKeyUp();
            case ScalarInput.ISV_KEY_ENTER:
                return onEnterKeyUp();
            case ScalarInput.ISV_KEY_FN:
                return onFnKeyUp();
            case ScalarInput.ISV_KEY_AEL:
                return onAelKeyUp();
            case ScalarInput.ISV_KEY_MENU:
            case ScalarInput.ISV_KEY_SK1:
                return onMenuKeyUp();
            case ScalarInput.ISV_KEY_S1_1:
                return onFocusKeyUp();
            case ScalarInput.ISV_KEY_S1_2:
                return true;
            case ScalarInput.ISV_KEY_S2:
                return onShutterKeyUp();
            case ScalarInput.ISV_KEY_PLAY:
                return onPlayKeyUp();
            case ScalarInput.ISV_KEY_STASTOP:
                return onMovieKeyUp();
            case ScalarInput.ISV_KEY_CUSTOM1:
                return onC1KeyUp();
            case ScalarInput.ISV_KEY_DELETE:
            case ScalarInput.ISV_KEY_SK2:
                return onDeleteKeyUp();
            case ScalarInput.ISV_KEY_LENS_ATTACH:
                return onLensDetached();
            case ScalarInput.ISV_DIAL_1_CLOCKWISE:
            case ScalarInput.ISV_DIAL_1_COUNTERCW:
                return true;
            case ScalarInput.ISV_DIAL_2_CLOCKWISE:
            case ScalarInput.ISV_DIAL_2_COUNTERCW:
                return true;
            case ScalarInput.ISV_KEY_MODE_DIAL:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    protected int getDialStatus(int key) {
        return ScalarInput.getKeyStatus(key).status;
    }
    protected boolean onUpKeyDown() { return false; }
    protected boolean onUpKeyUp() { return false; }
    protected boolean onDownKeyDown() { return false; }
    protected boolean onDownKeyUp() { return false; }
    protected boolean onLeftKeyDown() { return false; }
    protected boolean onLeftKeyUp() { return false; }
    protected boolean onRightKeyDown() { return false; }
    protected boolean onRightKeyUp() { return false; }
    protected boolean onEnterKeyDown() { return false; }
    protected boolean onEnterKeyUp() { return false; }
    protected boolean onFnKeyDown() { return false; }
    protected boolean onFnKeyUp() { return false; }
    protected boolean onAelKeyDown() { return false; }
    protected boolean onAelKeyUp() { return false; }
    protected boolean onMenuKeyDown() { return false; }
    protected boolean onMenuKeyUp() { return false; }
    protected boolean onFocusKeyDown() { return false; }
    protected boolean onFocusKeyUp() { return false; }
    protected boolean onShutterKeyDown() { return false; }
    protected boolean onShutterKeyUp() { return false; }
    protected boolean onPlayKeyDown() { return false; }
    protected boolean onPlayKeyUp() { return false; }
    protected boolean onMovieKeyDown() { return false; }
    protected boolean onMovieKeyUp() { return false; }
    protected boolean onC1KeyDown() { return false; }
    protected boolean onC1KeyUp() { return false; }
    protected boolean onLensAttached() { return false; }
    protected boolean onLensDetached() { return false; }
    protected boolean onUpperDialChanged(int value) {
        shiftValueBy(value * 10);
        return true;
    }
    protected boolean onLowerDialChanged(int value) {
        shiftValueBy(value);
        return true;
    }

    private void shiftValueBy(int value) {
        int newValue = getValue() + value;
        if (newValue > getMaxValue())
        {
            newValue = getMinValue();
        }
        else if(newValue < getMinValue())
        {
            newValue = getMaxValue();
        }
        setValue(newValue);
    }

    protected boolean onModeDialChanged(int value) { return false; }
    protected boolean onDeleteKeyDown() { return false; }
    protected boolean onDeleteKeyUp() { return false; }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
        clampValue();
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
        clampValue();
    }

    public int getMinValue() {
        return MinValue;
    }

    public void setMinValue(int minValue) {

        MinValue = minValue;
        clampValue();

    }

    private void clampValue()
    {
        int oldValue = Value;
        Value = Math.min(Math.max(getMinValue(), Value), getMaxValue());
        updateText();
    }

    private void updateText()
    {
        setText(new Integer(Value).toString() + Postfix);
    }
}

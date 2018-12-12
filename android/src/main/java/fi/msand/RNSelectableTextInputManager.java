
package fi.msand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewDefaults;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.textinput.ReactEditText;
import com.facebook.react.views.textinput.ReactTextInputManager;

public class RNSelectableTextInputManager extends ReactTextInputManager {
    private static final String REACT_CLASS = "RNSelectableTextInput";

    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-components-android.html#1-create-the-viewmanager-subclass
        return REACT_CLASS;
    }

    class CustomTextInput extends ReactEditText {
        private final InputMethodManager mInputMethodManager;

        public CustomTextInput(Context context) {
            super(context);
            mInputMethodManager = (InputMethodManager)
                    Assertions.assertNotNull(getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        }

        @Override
        protected void onCreateContextMenu(ContextMenu menu) {}

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            boolean result = super.onTouchEvent(ev);
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            return result;
        }

        @Override
        public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
            // Always return true if we are already focused. This is used by android in certain places,
            // such as text selection.
            if (isFocused()) {
                return true;
            }
            setFocusableInTouchMode(true);
            boolean focused = super.requestFocus(direction, previouslyFocusedRect);
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            return focused;
        }
    }

    @Override
    public ReactEditText createViewInstance(ThemedReactContext context) {
        ReactEditText editText = new CustomTextInput(context);
        int inputType = editText.getInputType();
        editText.setInputType(inputType & (~InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        editText.setReturnKeyType("done");
        editText.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                (int) Math.ceil(PixelUtil.toPixelFromSP(ViewDefaults.FONT_SIZE_SP)));
        editText.setTextIsSelectable(true);
        return editText;

    }

    @Override
    @ReactProp(name = "editable", defaultBoolean = true)
    public void setEditable(ReactEditText view, boolean editable) {
        // view.setEnabled(editable);
        view.setTextIsSelectable(true);
    }
}

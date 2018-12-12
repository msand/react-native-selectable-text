
package fi.msand;

import android.content.Context;
import android.graphics.Rect;
import android.text.Selection;
import android.text.SpannableString;
import android.view.ContextMenu;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.text.ReactTextView;
import com.facebook.react.views.text.ReactTextViewManager;

import javax.annotation.Nullable;

public class RNSelectableTextManager extends ReactTextViewManager {
    private static final String REACT_CLASS = "RCTMultilineTextInputView";

    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-components-android.html#1-create-the-viewmanager-subclass
        return REACT_CLASS;
    }

    class CustomText extends ReactTextView {
        private @Nullable SelectionWatcher mSelectionWatcher;

        public CustomText(Context context) {
            super(context);
        }

        @Override
        protected void onCreateContextMenu(ContextMenu menu) {}

        @SuppressWarnings("unused")
        public void setSelection(int start, int end) {
            CharSequence chars = getText();
            SpannableString str = new SpannableString(chars);
            Selection.setSelection(str, start, end);
        }

        @Override
        protected void onSelectionChanged(int selStart, int selEnd) {
            super.onSelectionChanged(selStart, selEnd);
            if (mSelectionWatcher != null && hasFocus()) {
                mSelectionWatcher.onSelectionChanged(selStart, selEnd);
            }
        }

        @Override
        protected void onFocusChanged(
                boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            if (focused && mSelectionWatcher != null) {
                mSelectionWatcher.onSelectionChanged(getSelectionStart(), getSelectionEnd());
            }
        }

        public void setSelectionWatcher(SelectionWatcher selectionWatcher) {
            mSelectionWatcher = selectionWatcher;
        }
    }
    @Override
    public CustomText createViewInstance(ThemedReactContext context) {
        return new CustomText(context);
    }

    @SuppressWarnings("unused")
    @ReactProp(name = "onSelectionChange")
    public void setOnSelectionChange(final CustomText view, boolean onSelectionChange) {
        if (onSelectionChange) {
            view.setSelectionWatcher(new ReactSelectionWatcher(view));
        } else {
            view.setSelectionWatcher(null);
        }
    }

    interface SelectionWatcher {
        void onSelectionChanged(int start, int end);
    }

    private class ReactSelectionWatcher implements SelectionWatcher {

        private CustomText mReactEditText;
        private EventDispatcher mEventDispatcher;
        private int mPreviousSelectionStart;
        private int mPreviousSelectionEnd;

        ReactSelectionWatcher(CustomText editText) {
            mReactEditText = editText;
            ReactContext reactContext = (ReactContext) editText.getContext();
            mEventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        }

        @Override
        public void onSelectionChanged(int start, int end) {
            // Android will call us back for both the SELECTION_START span and SELECTION_END span in text
            // To prevent double calling back into js we cache the result of the previous call and only
            // forward it on if we have new values
            if (mPreviousSelectionStart != start || mPreviousSelectionEnd != end) {
                mEventDispatcher.dispatchEvent(
                        new ReactTextSelectionEvent(
                                mReactEditText.getId(),
                                start,
                                end
                        ));

                mPreviousSelectionStart = start;
                mPreviousSelectionEnd = end;
            }
        }
    }

}

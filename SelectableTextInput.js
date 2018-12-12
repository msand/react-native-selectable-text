import React from 'react';
import {
  requireNativeComponent,
  Text,
  TextInput,
  TouchableWithoutFeedback,
  UIManager,
  Platform,
} from 'react-native';

const SelectableTextInput = requireNativeComponent(
  'RNSelectableTextInput',
  SelectableTextInputView,
);

export default class SelectableTextInputView extends TextInput {
  render() {
    let textInput;
    if (Platform.OS === 'ios') {
      textInput = UIManager.RCTVirtualText
        ? this._renderIOS()
        : this._renderIOSLegacy();
    } else if (Platform.OS === 'android') {
      textInput = this.renderAndroid();
    }
    return textInput;
  }
  renderAndroid() {
    const props = Object.assign({}, this.props);
    props.style = [this.props.style];
    props.autoCapitalize =
      UIManager.AndroidTextInput.Constants.AutoCapitalizationType[
        props.autoCapitalize || 'sentences'
      ];
    /* $FlowFixMe(>=0.53.0 site=react_native_fb,react_native_oss) This comment
   * suppresses an error when upgrading Flow's support for React. To see the
   * error delete this comment and run Flow. */
    let children = this.props.children;
    let childCount = 0;
    React.Children.forEach(children, () => ++childCount);
    if (childCount > 1) {
      children = <Text>{children}</Text>;
    }

    if (props.selection && props.selection.end == null) {
      props.selection = {
        start: props.selection.start,
        end: props.selection.start,
      };
    }

    const textContainer = (
      <SelectableTextInput
        ref={this._setNativeRef}
        {...props}
        mostRecentEventCount={0}
        onFocus={this._onFocus}
        onBlur={this._onBlur}
        onChange={this._onChange}
        onSelectionChange={this._onSelectionChange}
        onTextInput={this._onTextInput}
        text={this._getText()}
        children={children}
        disableFullscreenUI={this.props.disableFullscreenUI}
        textBreakStrategy={this.props.textBreakStrategy}
        onScroll={this._onScroll}
      />
    );

    return (
      <TouchableWithoutFeedback
        onLayout={props.onLayout}
        onPress={this._onPress}
        accessible={this.props.accessible}
        accessibilityLabel={this.props.accessibilityLabel}
        accessibilityRole={this.props.accessibilityRole}
        accessibilityStates={this.props.accessibilityStates}
        nativeID={this.props.nativeID}
        testID={this.props.testID}
      >
        {textContainer}
      </TouchableWithoutFeedback>
    );
  }
}

import React from 'react';
import { requireNativeComponent, Platform, Text } from 'react-native';

let SelectableText;
if (Platform.OS === 'ios') {
  SelectableText = requireNativeComponent(
    'SelectableText',
    SelectableTextView,
  );
} else {
  SelectableText = requireNativeComponent(
    'RCTMultilineTextInputView',
    SelectableTextView,
  );
}


export default class SelectableTextView extends React.Component {
  render() {
    let props = this.props;
    return (
      <SelectableText
        {...props}
        ref={props.forwardedRef}
        onSelectionChange={this._onSelectionChange}
        text={props.children || props.value}
      >
        <Text>{props.children || props.value}</Text>
      </SelectableText>
    );
  }

  _onSelectionChange = event => {
    event.persist();
    this.props.onSelectionChange && this.props.onSelectionChange(event);

    if (!this._inputRef) {
      // calling `this.props.onSelectionChange`
      // may clean up the input itself. Exits here.
      return;
    }

    this._lastNativeSelection = event.nativeEvent.selection;

    if (this.props.selection || this.props.selectionState) {
      this.forceUpdate();
    }
  };
}
SelectableTextView.displayName = 'SelectableText';
import React from 'react';
import { requireNativeComponent } from 'react-native';

const SelectableText = requireNativeComponent(
  'RCTMultilineTextInputView',
  SelectableTextView,
);

export default class SelectableTextView extends React.Component {
  render() {
    let props = this.props;
    return (
      <SelectableText
        {...props}
        ref={props.forwardedRef}
        onSelectionChange={this._onSelectionChange}
      >
        {props.children || props.value}
      </SelectableText>
    );
  }

  _onSelectionChange = event => {
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

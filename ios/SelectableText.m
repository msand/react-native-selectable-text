//
//  SelectableText.m
//  NativeMobileApp
//
//  Created by Shirly.Chen on 11/3/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//
#import "SelectableText.h"

@implementation SelectableTextManager
RCT_EXPORT_MODULE()

- (UIView *)view
{
  return [[RCTMultilineTextInputView alloc] initWithBridge:self.bridge];
}

#pragma mark - Multiline <TextInput> (aka TextView) specific properties

#if !TARGET_OS_TV
RCT_REMAP_VIEW_PROPERTY(dataDetectorTypes, backedTextInputView.dataDetectorTypes, UIDataDetectorTypes)
#endif

@end

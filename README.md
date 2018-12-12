
# react-native-selectable-text

Gives cross platform support for multiline selectable text, where no keyboard is wanted.

## Getting started

`$ npm install react-native-selectable-text --save`

### Mostly automatic installation

`$ react-native link react-native-selectable-text`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
- Add `import fi.msand.RNSelectableTextPackage;` to the imports at the top of the file
- Add `new RNSelectableTextPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
    ```
    include ':react-native-selectable-text'
    project(':react-native-selectable-text').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-selectable-text/android')
    ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
    compile project(':react-native-selectable-text')
  	```


## Usage
```jsx
import SelectableText from 'react-native-selectable-text';

export default ({ text, onSelectionChange }) => (
  <SelectableText
    selectable
    multiline
    contextMenuHidden
    scrollEnabled={false}
    editable={false}
    onSelectionChange={(event) => {
      const {
        nativeEvent: {
          selection: { start, end },
        },
      } = event
      const str = text.substring(start, end)
      onSelectionChange({ str, start, end })
    }}
    style={{
      color: "#BAB6C8",
    }}
    value={text}
  />);
```
  
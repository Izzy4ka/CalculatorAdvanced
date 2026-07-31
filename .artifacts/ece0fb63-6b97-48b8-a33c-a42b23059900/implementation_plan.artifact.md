# Implementation Plan - Fix Layout Render Issue

The layout `fragment_calculator.xml` has a render issue where the `Flow` helper (`flow_calculate`) is reported as covering the `btn_ac` button. This is likely due to the `Flow` being declared after the buttons in the XML, placing it higher in the Z-order.

## Proposed Changes

### Layout Fix

#### [MODIFY] [fragment_calculator.xml](file:///E:/code/Calculator/app/src/main/res/layout/fragment_calculator.xml)

- Move the `androidx.constraintlayout.helper.widget.Flow` element (with id `flow_calculate`) to be declared before the buttons it references. This will ensure it is at the bottom of the Z-stack and does not "cover" the buttons according to the layout validator.
- Also move the `androidx.constraintlayout.widget.Group` element (with id `group_scientific`) to the top for consistency and to avoid similar potential issues.

## Verification Plan

### Manual Verification
- The fix is specifically for a render issue in the IDE/Preview. After the change, the "covered by" warning in the layout validator should disappear.
- I will verify that the XML remains valid and the button IDs are still correctly referenced by the `Flow`.

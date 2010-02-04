/* **************************************************************************** 
 * CIShell: Cyberinfrastructure Shell, An Algorithm Integration Framework.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Apache License v2.0 which accompanies
 * this distribution, and is available at:
 * http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Created on Sep 20, 2006 at Indiana University.
 * 
 * Contributors:
 *     Indiana University - 
 * ***************************************************************************/
package org.cishell.reference.gui.guibuilder.swt.builder.components;

import org.cishell.reference.gui.guibuilder.swt.builder.AbstractComponent;
import org.cishell.reference.gui.guibuilder.swt.builder.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class StringComponent extends AbstractComponent {
	protected Text textField;
	protected Combo combo;
	protected String[] optionValues;
	private boolean multiline;

	public StringComponent() {
		this(false, 1);
	}

	public StringComponent(boolean multiline) {
		this(false, 1);
		this.multiline = multiline;
	}

	public StringComponent(boolean drawLabel, int numColumns) {
		super(drawLabel, numColumns);
	}

	public Control createGUI(Composite parent, int style) {

		GridData gd = new GridData(SWT.FILL,SWT.CENTER,true,true);
		gd.horizontalSpan = MAX_SPAN-1;
		gd.minimumWidth = 100;
		optionValues = attr.getOptionValues();
		if(optionValues != null) {
			combo = new Combo(parent, style | SWT.DROP_DOWN | SWT.READ_ONLY);

			String[] optionLabels = attr.getOptionLabels();
			if(optionLabels == null) {
				combo.setItems(optionValues);
			} else {
				combo.setItems(optionLabels);
			}

			combo.select(0);

			combo.setLayoutData(gd);

			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					update();
				}
			});

			return combo;
		} else {
			int flags;
			if(multiline) {
				flags = style | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL;
				gd.minimumHeight = 100;
				gd.minimumWidth = 250;
			} else {
				flags = style | SWT.BORDER;
			}
			textField = new Text(parent, flags);
			textField.setLayoutData(gd);

			textField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					update();
				}
			}); 

			return textField;
		}
	}

	public Object getValue() {
		Object value;
		if(combo == null) {
			value = StringConverter.getInstance().stringToObject(attr, textField.getText());
		} else {
			value = StringConverter.getInstance().stringToObject(attr, getListValue());
		}

		return value;
	}

	private String getListValue() {
		if (optionValues != null) {
			return optionValues[combo.getSelectionIndex()];
		} else {
			return "You are not specifying option values, fool!";
		}
	}

	public String validate() {
		if (getValue() == null) {
			return "Invalid basic value";
		}
		if(combo == null) {
			return attr.validate(textField.getText());
		} else {
			return attr.validate(getListValue());
		}
	}

	public void setValue(Object value) {
		if(value.toString().startsWith("textarea:")) {
			value = value.toString().substring("textarea:".length());
		}
		if (textField != null) {
			textField.setText(value == null ? "" : value.toString());
		} else if (combo != null) {

			int setComboToIndex = -1;
			for (int i = 0; i < optionValues.length; i++) {
				if (value.equals(optionValues[i])) {
					setComboToIndex = i;
				}
			}

			if (setComboToIndex != -1) {
				combo.select(setComboToIndex);
			} else {
				System.err.println("Attempted to set combo box to a value " +
				"that didn't exist inside the combo box.");
			}
		}
	}
}

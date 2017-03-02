package com.ipmetric.p1.client;

import java.io.Serializable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.ipmetric.p1.client.data.RackData;

public class Rack extends HorizontalPanel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1499981127214356790L;
	TextBox textBox;
	RackData rackData;
	public final static RackData unknownRack = new RackData("???????");

	Rack() {
		this(unknownRack);
	}

	Rack(final RackData rackData) {
		super();
		this.rackData = rackData;
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textBox = new TextBox();
		textBox.setStylePrimaryName("rackStyle");
		textBox.setMaxLength(7);
		final InlineLabel label = new InlineLabel(" ");
		label.setStylePrimaryName("rackStyle");
		this.add(label);
		this.add(textBox);
		this.setText(rackData.getRack());
		textBox.addChangeHandler(new ChangeHandler() {

			public void onChange(final ChangeEvent event) {
				rackData.setRack(textBox.getText());
			}

		});
	}

	void setText(final String text) {
		textBox.setText(text.toUpperCase());
//		textBox.setEnabled(text.equals(unknownRack));
		textBox.setEnabled(true);
		textBox.setWidth("84px");
	}

	void refreshText() {
		textBox.setText(rackData.getRack());
	}

	String getText() {
		return textBox.getText();
	}

	void updateRack(final RackData rack) {
		this.rackData.setRack(rack.getRack().toUpperCase());
		this.refreshText();

	}

}

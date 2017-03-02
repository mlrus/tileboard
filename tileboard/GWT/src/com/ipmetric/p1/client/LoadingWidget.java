package com.ipmetric.p1.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class LoadingWidget extends Composite {

	final HorizontalPanel pWidget = new HorizontalPanel();
	private final HTML html = new HTML();

	/**
	 * init widget
	 */
	public LoadingWidget() {

		final String sImage = "/images/loading2.gif";
		final Image image = new Image(sImage);

		pWidget.add(new HTML("&nbsp;"));
		pWidget.add(image);
		pWidget.add(new HTML("&nbsp;"));
		pWidget.add(html);

		initWidget(pWidget);

		pWidget.setStyleName("loadingImage");

		hide();
	}

	/**
	 * hide
	 */
	public void hide() {
		pWidget.setVisible(false);
	}

	/**
	 * show
	 */
	public void show() {
		pWidget.setVisible(true);
	}

	/**
	 * show with text
	 * 
	 * @param s
	 */
	public void show(final String s) {
		show();
		setHTML(s);
	}

	/**
	 * set text for loading
	 * 
	 * @param s
	 */
	public void setHTML(final String s) {
		html.setVisible(true);
		html.setHTML(s);
	}

	/**
	 * hide timed
	 */
	public void hideTimed() {
		pWidget.setVisible(true);

		final Timer t = new Timer() {
			@Override
			public void run() {
				pWidget.setVisible(false);
			}
		};
		t.schedule(3000);
	}
}

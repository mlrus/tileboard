package com.ipmetric.p1.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public class ParameterContainer extends Composite {
	private final MServiceAsync callProvider = (MServiceAsync) GWT.create(MService.class);

	String valueResult = null;
	boolean valid = false;
	private final boolean error = false;
	boolean newValue = false;

	boolean isValid() {
		return valid;
	}

	boolean isNew() {
		synchronized (this) {
			final boolean b = newValue;
			newValue = false;
			return b;
		}
	}

	boolean isError() {
		return error;
	}

	String getValue() {
		return valueResult;
	}

	public ParameterContainer(final String paramName) {
		getParam(paramName);
	}

	private void getParam(final String paramName) {
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(final Throwable ex) {
				valueResult = "error";
				GWT.log(ex.toString(), null);
			}

			public void onSuccess(final String result) {
				valueResult = result;
				valid = true;
				newValue = true;
			}
		};
		callProvider.getParam(paramName, callback);
	}

//	/**
//	 * observe
//	 */
//	public void onCellClicked(final SourcesTableEvents sender, final int row, final int cell) {
//		GWT.log("cell clicked: row=" + row + " cell=" + cell, null);
//	}
}

package com.ipmetric.p1.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public class ParameterRetriever extends Composite {

	private final MServiceAsync callProvider = (MServiceAsync) GWT.create(MService.class);

	private boolean error = false;
	private boolean valid = false;
	private boolean newValue = false;

	boolean isValid() {
		return valid;
	}

	boolean isNew() {
		if (!valid || !newValue) return false;
		newValue = false;
		return true;
	}

	boolean errorResult() {
		return error;
	}

	public ParameterRetriever(final String[] store, final String query) {
		this(store, 0, query);
	}

	public ParameterRetriever(final String[] store, final int slot, final String query) {
		assert slot < store.length : "ParameterRetriever for slot " + slot + " but " + store.length + " locations";
		getParam(store, slot, query);
	}

	private void getParam(final String[] store, final int slot, final String query) {
		final AsyncCallback<String> callback = new AsyncCallback<String>() {
			@SuppressWarnings("synthetic-access")
			public void onFailure(final Throwable ex) {
				error = true;
				GWT.log(ex.toString(), null);
			}

			@SuppressWarnings("synthetic-access")
			public void onSuccess(final String result) {
				store[slot] = result;
				newValue = true;
				valid = true;
			}
		};
		callProvider.getParam(query, callback);
	}
}

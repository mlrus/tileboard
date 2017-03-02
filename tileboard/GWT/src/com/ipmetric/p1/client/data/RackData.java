package com.ipmetric.p1.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RackData implements IsSerializable {
	/**
	 * 
	 */
	private String rack;

	public RackData() {
		rack = "";
	}

	public RackData(final String rack) {
		this.rack = rack;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(final String rack) {
		this.rack = rack;
	}
}

/**
 * 
 */
package com.ipmetric.p1.client;

import com.google.gwt.user.client.rpc.IsSerializable;

class IntPair implements IsSerializable {
	final int a, b;

	IntPair(final int a, final int b) {
		this.a = a;
		this.b = b;
	}

	int getA() {
		return a;
	}

	int getB() {
		return b;
	}

	@Override
	public String toString() {
		return "[" + a + "," + b + "]";
	}
}
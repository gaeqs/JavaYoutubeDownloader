package com.degoos.javayoutubedownloader.decrypt;

import javax.script.Invocable;

public class DecryptScript {

	private String name;
	private Invocable invocable;

	public DecryptScript(String name, Invocable invocable) {
		this.name = name;
		this.invocable = invocable;
	}

	public String getFunctionName() {
		return name;
	}

	public Invocable getInvocable() {
		return invocable;
	}
}

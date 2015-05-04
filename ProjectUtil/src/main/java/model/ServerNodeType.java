package model;

import gui.Utils;

import javax.swing.Icon;

public enum ServerNodeType {
	SERVER("/images/server.png"), USER("/images/user.png"), MAILBOX(
			"/images/mailbox.png", "/images/mailbox_down.png"), INFO(
			"/images/info.png"), MESSAGE(""), PREFERENCE("");

	private String defaultIconPath;
	private String openIconPath;

	private ServerNodeType(String defaultIconPath) {
		this.defaultIconPath = defaultIconPath;
		this.openIconPath = defaultIconPath;
	}

	private ServerNodeType(String defaultIconPath, String openIconPath) {
		this.defaultIconPath = defaultIconPath;
		this.openIconPath = openIconPath;
	}

	public Icon getIcon(boolean expanded) {
		if (expanded) {
			return Utils.createIcon(openIconPath);
		} else {
			return Utils.createIcon(defaultIconPath);
		}
	}
}

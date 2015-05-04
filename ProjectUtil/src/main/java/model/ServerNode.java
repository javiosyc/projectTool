package model;

public class ServerNode {
	private ServerNodeType type;
	private String name;

	public ServerNode(ServerNodeType type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServerNodeType getType() {
		return type;
	}

	public String toString() {
		return name;
	}
}

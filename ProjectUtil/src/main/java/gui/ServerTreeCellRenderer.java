package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import model.ServerNode;
import model.ServerNodeType;

public class ServerTreeCellRenderer implements TreeCellRenderer {

	private JCheckBox leafCheckBoxRenderer;
	private JLabel leafTextFieldRenderer;
	private DefaultTreeCellRenderer nonLeafRenderer;
	private Color textForeground;
	private Color textBackground;
	private Color selectionForeground;
	private Color selectionBackground;

	public ServerTreeCellRenderer() {
		leafCheckBoxRenderer = new JCheckBox();
		leafTextFieldRenderer = new JLabel();
		nonLeafRenderer = new DefaultTreeCellRenderer();

		textForeground = UIManager.getColor("Tree.textForeground");
		textBackground = UIManager.getColor("Tree.textBackground");
		selectionForeground = UIManager.getColor("Tree.selectionForeground");
		selectionBackground = UIManager.getColor("Tree.selectionBackground");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		Object userObject = node.getUserObject();

		if (userObject instanceof ServerNode) {
			ServerNode serverNode = (ServerNode) userObject;
			ServerNodeType type = serverNode.getType();

			if (type == ServerNodeType.MESSAGE || type == ServerNodeType.PREFERENCE) {
				Component leafRenderer = null;
				leafRenderer = genLeafRenderer(serverNode);
				if (selected) {
					leafRenderer.setForeground(selectionForeground);
					leafRenderer.setBackground(selectionBackground);
				} else {
					leafRenderer.setForeground(textForeground);
					leafRenderer.setBackground(textBackground);
				}
				return leafRenderer;
			} else {
				nonLeafRenderer.getTreeCellRendererComponent(tree, value,
						selected, expanded, leaf, row, hasFocus);
				nonLeafRenderer.setIcon(type.getIcon(expanded));
				nonLeafRenderer.setDisabledIcon(type.getIcon(expanded));
				if (ServerNodeType.USER != type) {
					nonLeafRenderer.setText("");
				}
				return nonLeafRenderer;
			}
		}
		return nonLeafRenderer.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);
	}

	private Component genLeafRenderer(ServerNode serverNode) {
		Component leafRenderer = null;

		switch (serverNode.getType()) {
		case MESSAGE:
			leafCheckBoxRenderer.setText(serverNode.getName());
			leafRenderer = leafCheckBoxRenderer;
			break;
		case PREFERENCE:
			leafTextFieldRenderer.setText(serverNode.getName());
			leafRenderer = leafTextFieldRenderer;
			break;
		default:
			leafCheckBoxRenderer.setText(serverNode.getName());
			leafRenderer = leafCheckBoxRenderer;
			break;
		}

		return leafRenderer;
	}
}

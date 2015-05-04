package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Toolbar extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3710616126983846137L;
	private JButton importButton;
	private JButton exportButton;
	private ToolBarListener toolBarListener;

	public Toolbar() {

		setBorder(BorderFactory.createEtchedBorder());
		importButton = new JButton();
		importButton.setIcon(Utils.createIcon("/images/Import16.gif"));
		importButton.addActionListener(this);
		importButton.setMnemonic(KeyEvent.VK_I);
		exportButton = new JButton();
		exportButton.setIcon(Utils.createIcon("/images/Export16.gif"));
		exportButton.addActionListener(this);
		exportButton.setMnemonic(KeyEvent.VK_O);
		setLayout(new FlowLayout(FlowLayout.LEFT));

		add(importButton);
		add(exportButton);
	}

	public void setToolBarListener(ToolBarListener toolBarListener) {
		this.toolBarListener = toolBarListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();

		if (clicked == importButton) {
			if (toolBarListener != null) {
				toolBarListener.fireLoadFileChooser();
			}
		} else if (clicked == exportButton) {
			if (toolBarListener != null) {
				toolBarListener.fireSaveFileChooser();
			}
		}
	}
}

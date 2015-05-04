package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import model.Message;
import model.ServerNode;
import model.ServerNodeType;
import controller.EmailServer;
import controller.MessageClient;

public class MessagePanel extends JPanel {
	private static final long serialVersionUID = 3141060834695113186L;
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ProgressDialog progressDialog;
	private JPanel buttonPanel;
	private JButton startButton;
	private JButton stopButton;
	private JTextField messageText;
	private JButton sendButton;
	private JComboBox<String> sendTo;

	private MessageClient client;
	private EmailServer server;

	SwingWorker<List<Message>, Integer> clientWorker;
	public MessagePanel() {

		client = new MessageClient();
		serverTree = createServerTree();

		initButtonPanel();
		initProgressDialog();

		setLayout(new BorderLayout());
		add(new JScrollPane(serverTree), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void initProgressDialog() {
		progressDialog = new ProgressDialog((Window) getParent(), "Message Downloading...");
		
		progressDialog.setListener(new ProgressDialogListener() {
			public void progressDialogCancelled() {
				System.out.println("cancalled...");
				if (clientWorker != null) {
					clientWorker.cancel(true);
				}
			}
		});
	}

	private void initButtonPanel() {
		buttonPanel = new JPanel();
		startButton = new JButton("start");
		stopButton = new JButton("stop");
		messageText = new JTextField(30);
		sendButton = new JButton("send");
		sendTo = new JComboBox<String>();

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server = new EmailServer();

				SwingWorker<List<Message>, Integer> worker = new SwingWorker<List<Message>, Integer>() {
					protected List<Message> doInBackground() throws Exception {
						server.start();
						return null;
					}
				};
				worker.execute();
			}
		});

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (server != null) {
					SwingWorker<List<Message>, Integer> worker = new SwingWorker<List<Message>, Integer>() {
						protected List<Message> doInBackground()
								throws Exception {
							server.stop();
							return null;
						}
					};
					worker.execute();
				}
			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<List<Message>, Integer> worker = new SwingWorker<List<Message>, Integer>() {
					protected List<Message> doInBackground() throws Exception {
						client.connect();

						String user = (String) sendTo.getSelectedItem();
						System.out.println(user);

						String message = messageText.getText();
						System.out.println(message);
						client.sendMessage(user, message);
						client.disconnect();
						return null;
					}
				};
				worker.execute();
				System.out.println("send");
			}
		});

		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 0.1;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		buttonPanel.add(startButton, gc);

		gc.gridx = 2;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		buttonPanel.add(stopButton, gc);

		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);

		DefaultComboBoxModel<String> user = new DefaultComboBoxModel<String>();
		user.addElement("Dave");
		user.addElement("Karen");
		sendTo.setModel(user);
		sendTo.setSelectedIndex(0);

		buttonPanel.add(sendTo, gc);

		gc.gridx = 1;
		gc.weightx = 2;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		buttonPanel.add(messageText, gc);

		gc.gridx = 2;
		gc.weightx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		buttonPanel.add(sendButton, gc);
	}

	private JTree createServerTree() {
		serverTree = new JTree(createTree());
		serverTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		treeCellRenderer = new ServerTreeCellRenderer();
		serverTree.setCellRenderer(treeCellRenderer);

		serverTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				// DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				// serverTree
				// .getLastSelectedPathComponent();

				// Object userObject = node.getUserObject();

				// System.out.println(userObject instanceof String);
			}
		});

		serverTree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {

				TreePath path = event.getPath();
				if (path == null)
					return;

				Object lastComponent = path.getLastPathComponent();
				if (lastComponent == null)
					return;

				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) lastComponent;

				Object userObject = treeNode.getUserObject();

				if (!(userObject instanceof ServerNode)) {
					return;
				}
				ServerNode serverNode = (ServerNode) userObject;

				if (serverNode.getType() == ServerNodeType.MAILBOX) {

					retrieveMessages(treeNode);

				}
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
			}
		});
		return serverTree;
	}

	private void retrieveMessages(final DefaultMutableTreeNode treeNode) {
		serverTree.setEnabled(false);
		System.out.println("connect to mail server...");

		progressDialog.setVisible(true);


		clientWorker = new SwingWorker<List<Message>, Integer>() {

			@Override
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);
				System.out.println("retrieve " + retrieved);
				progressDialog.setValue(retrieved);
			}

			@Override
			protected void done() {
				try {
					List<Message> retrieveMessages = get();

					for (Message message : retrieveMessages) {
						client.addMessage(message, "1");
						treeNode.add(new DefaultMutableTreeNode(new ServerNode(
								ServerNodeType.MESSAGE, message.toString())));
					}
					System.out.println(retrieveMessages.size());
					System.out.println("finish connect mail server...");

					DefaultTreeModel test = (DefaultTreeModel) serverTree
							.getModel();
					test.nodeStructureChanged(treeNode);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					serverTree.setEnabled(true);
					progressDialog.setVisible(false);
				}
			}

			@Override
			protected List<Message> doInBackground() throws Exception {
				List<Message> retrieveMessages = new ArrayList<Message>();

				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeNode
						.getParent();

				ServerNode node = (ServerNode) parent.getUserObject();

				String user = node.getName();

				try {
					client.connect();

					int size = client.getMessageSize(user);
					progressDialog.setMaximum(size);
					//publish(0);

					SimpleDateFormat sdFormat = new SimpleDateFormat(
							"MM/dd HH:mm");
					Calendar calendar = Calendar.getInstance();
					String date = sdFormat.format(calendar.getTime());
					for (int i = 0; i < size; i++) {
						String result = client.readMessage();
						Message message = new Message(result, date);
						retrieveMessages.add(message);
						publish(i+1);
						Thread.sleep(2*1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
					serverTree.setEnabled(true);
					progressDialog.setVisible(false);
				} finally {
					client.disconnect();
				}
				return retrieveMessages;
			}

		};

		clientWorker.execute();
	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(new ServerNode(
				ServerNodeType.SERVER, "Server"));

		loadUser(top);
		return top;
	}

	private void loadUser(DefaultMutableTreeNode top) {
		top.add(genUser("Dave", client.getMailbox1(), new String[] {}));
		top.add(genUser("Karen", client.getMailbox2(),
				new String[] { "userName:Karen" }));
	}

	public DefaultMutableTreeNode genUser(String name, Message[] messages,
			String[] infos) {

		DefaultMutableTreeNode mailBox = new DefaultMutableTreeNode(
				new ServerNode(ServerNodeType.MAILBOX, "mailBox"));

		for (Message message : messages) {
			DefaultMutableTreeNode messageNode = new DefaultMutableTreeNode(
					new ServerNode(ServerNodeType.MESSAGE, message.toString()));
			mailBox.add(messageNode);
		}

		DefaultMutableTreeNode infoNode = new DefaultMutableTreeNode(
				new ServerNode(ServerNodeType.INFO, "info"));

		for (String info : infos) {
			DefaultMutableTreeNode preference = new DefaultMutableTreeNode(
					new ServerNode(ServerNodeType.PREFERENCE, info));
			infoNode.add(preference);
		}

		DefaultMutableTreeNode user = new DefaultMutableTreeNode(
				new ServerNode(ServerNodeType.USER, name));
		user.add(mailBox);
		user.add(infoNode);
		return user;
	}
}

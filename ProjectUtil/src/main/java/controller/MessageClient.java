package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import model.Message;

public class MessageClient {

	private static InetAddress host;
	private static final int PORT = 1234;
	private Scanner networkInput;
	private PrintWriter networkOutput;

	private SortedSet<Message> mailbox1 = new TreeSet<Message>();

	private SortedSet<Message> mailbox2 = new TreeSet<Message>();

	private Socket link;
	private Scanner input;

	public MessageClient() {
		mailbox1 = loadDefalutMessage("1");
		mailbox2 = loadDefalutMessage("2");
	}

	private SortedSet<Message> loadDefalutMessage(String user) {
		SortedSet<Message> messages = new TreeSet<Message>();
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		if ("1".equals(user)) {
			calendar.set(2015, 2, 7, 13, 00, 00);
			messages.add(new Message("test2", sdFormat.format(calendar
					.getTime())));
			calendar.set(2015, 2, 7, 13, 01, 00);
			messages.add(new Message("test2", sdFormat.format(calendar
					.getTime())));
		} else if ("2".equals(user)) {
			calendar.set(2015, 2, 13, 13, 00, 00);
			messages.add(new Message("user2", sdFormat.format(calendar
					.getTime())));
		}

		return messages;
	}

	public Message[] getMailbox1() {
		Message[] result = new Message[mailbox1.size()];
		mailbox1.toArray(result);
		return result;
	}

	public Message[] getMailbox2() {
		Message[] result = new Message[mailbox2.size()];
		mailbox2.toArray(result);
		return result;
	}

	public void addMessage(Message message, String user) {
		if ("1".equals(user)) {
			mailbox1.add(message);
		} else if ("2".equals(user)) {
			mailbox2.add(message);
		}
	}

	public void connect() throws IOException {
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException uhEx) {
			System.out.println("Host ID not found!");
			throw uhEx;
		}

		try {
			link = new Socket(host, PORT); // Step 1.

			networkInput = new Scanner(link.getInputStream()); // Step 2.

			networkOutput = new PrintWriter(link.getOutputStream(), true);

		} catch (Exception ioEx) {
			ioEx.printStackTrace();
			throw ioEx;
		}
	}

	public int getMessageSize(String user) {
		networkOutput.println(user);
		networkOutput.println("read");
		return Integer.parseInt(networkInput.nextLine());
	}

	public void disconnect() throws IOException {

		try {
			System.out.println("\n* Closing connection... *");
			networkOutput.println("n");
			if (input != null)
				input.close();

			link.close(); // Step 4.
		} catch (IOException ioEx) {
			System.out.println("Unable to disconnect!");
			throw ioEx;
		}
	}

	/*
	 * private static void doSend(String message) { networkOutput.println(name);
	 * networkOutput.println("send"); networkOutput.println(message); }
	 */
	public String readMessage() throws IOException {
		return networkInput.nextLine();
	}

	public void sendMessage(String user, String message) {
		networkOutput.println(user);
		networkOutput.println("send");
		networkOutput.println(message);
	}

	public static void main(String[] args) throws IOException {
		MessageClient client = new MessageClient();

		String[] messages = { "test1", "2", "3", "test4" };
		for (String message : messages) {
			client.connect();
			client.sendMessage("Karen", message);
			client.disconnect();
		}
	}
}

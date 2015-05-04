package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EmailServer {
	private ServerSocket serverSocket;
	private static final int PORT = 1234;
	private static final String client1 = "Dave";
	private static final String client2 = "Karen";
	private static final int MAX_MESSAGES = 10;
	private static String[] mailbox1 = new String[MAX_MESSAGES];
	private static String[] mailbox2 = new String[MAX_MESSAGES];
	private static int messagesInBox1 = 0;
	private static int messagesInBox2 = 0;

	private boolean flag = true;
	

	public void start() throws IOException {
		System.out.println("Opening connection...\n");

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException ioEx) {
			System.out.println("Unable to attach to port!");
			throw ioEx;
		}
		do {
			try {
				runService();
			} catch (InvalidClientException icException) {
				System.out.println("Error: " + icException);
			} catch (InvalidRequestException irException) {
				System.out.println("Error: " + irException);
			}
		} while (flag);
	}
	
	public void stop() {
		flag = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runService() throws InvalidClientException,
			InvalidRequestException {

		Socket link = null;
		try {
			link = serverSocket.accept();
			Scanner input = new Scanner(link.getInputStream());
			PrintWriter output = new PrintWriter(link.getOutputStream(), true);

			String name = input.nextLine();
			if ("n".equals(name)) {
				return;
			}

			String sendRead = input.nextLine();

			if (!name.equals(client1) && !name.equals(client2))
				throw new InvalidClientException();
			if (!sendRead.equals("send") && !sendRead.equals("read"))
				throw new InvalidRequestException();
			System.out.println("\n" + name + " " + sendRead + "ing mail...");

			if (name.equals(client1)) {
				if (sendRead.equals("send")) {
					doSend(mailbox2, messagesInBox2, input);
					if (messagesInBox2 < MAX_MESSAGES)
						messagesInBox2++;
				} else {
					doRead(mailbox1, messagesInBox1, output);
					messagesInBox1 = 0;
				}
			} else {
				if (sendRead.equals("send")) {
					doSend(mailbox1, messagesInBox1, input);
					if (messagesInBox1 < MAX_MESSAGES)
						messagesInBox1++;
				} else {
					doRead(mailbox2, messagesInBox2, output);
					messagesInBox2 = 0;
				}
			}

		} catch (Exception ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				link.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void doRead(String[] mailbox, int messagesInBox,
			PrintWriter output) {
		/*
		 * Client has requested 'reading', so server must read messages from
		 * other client's message box and then send those messages to the first
		 * client.
		 */
		System.out.println("\nSending " + messagesInBox + " message(s).\n");
		output.println(messagesInBox);
		for (int i = 0; i < messagesInBox; i++)
			output.println(mailbox[i]);
	}

	private static void doSend(String[] mailbox, int messagesInBox,
			Scanner input) {
		/*
		 * Client has requested 'sending', so server must read message from this
		 * client and then place message into message box for other client (if
		 * there is room).
		 */
		System.out.println("Save: " + messagesInBox + ":" + mailbox);
		String message = input.nextLine();
		System.out.println("Save: " + message);
		if (messagesInBox == MAX_MESSAGES)
			System.out.println("\nMessage box full!");
		else
			mailbox[messagesInBox] = message;

	}

}

class InvalidClientException extends Exception {
	private static final long serialVersionUID = 2332406658548705914L;

	public InvalidClientException() {
		super("Invalid client name!");
	}

	public InvalidClientException(String message) {
		super(message);
	}
}

class InvalidRequestException extends Exception {
	private static final long serialVersionUID = -380342030208731540L;

	public InvalidRequestException() {
		super("Invalid request!");
	}

	public InvalidRequestException(String message) {
		super(message);
	}
}
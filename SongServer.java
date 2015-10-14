/* Author: Luigi Vincent
*  Socket that runs in conjunction with RasPi and receives message to play song.
*
*  TODO: Add flag to ensure at most one connection.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.awt.Desktop;

public class SongServer {
	private final static int PORT = 5290;
	private final static File TARGET = new File("funk.mp3");

	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(PORT)) {
			System.out.println(new Date() + "\nServer online.");

			while (true) {
				new Thread(new ClientHandler(server.accept())).start();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static class ClientHandler implements Runnable {
		private Socket socket;

		ClientHandler(Socket socket) {
			this.socket = socket;
			System.out.println("RasPi connected!");
		}

		@Override
		public void run() {
			try(
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
			){
				while (true) {
					String input = in.readLine();
					if (input == null || input.trim().isEmpty()) {
	                    continue;
	                }
	                if (input.startsWith("play")) {
	                	Desktop.getDesktop().open(TARGET);
	                }
				}
			} catch (IOException ioe) {
				System.out.println("RasPi disconnected!");
			}
		}
	}
}
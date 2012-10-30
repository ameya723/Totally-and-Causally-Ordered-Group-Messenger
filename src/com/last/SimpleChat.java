package com.last;

import java.io.*;

import com.last.ContentProv;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SimpleChat extends Activity {
	ServerSocket servSocket;
	Socket servAccepted;
	static Socket cliSocket;
	Socket forSequencer;
	Thread serverT = null;
	Handler handlerMain = new Handler();
	static TextView chatMsg;
	static EditText msgBox;
	Button send;
	Button test1;
	Button test2;
	static TelephonyManager telephony;
	static String phoneNo;
	static String phoneNoEmu1 = "15555215554";
	String phoneNoEmu2 = "15555215556";
	String phoneNoEmu3 = "15555215558";
	String phoneNoEmu4 = "15555215560";
	String phoneNoEmu5 = "15555215562";
	static String chat;
	String msg;
	static String temp[];
	static ArrayList<String> buffered_list;
	static List<Integer> socketConnList;
	int timestamp[] = new int[5];
	static StringBuffer stringBuff = new StringBuffer(20);
	static StringBuffer stringBuff_serv = new StringBuffer(20);
	static int seqno = 1;
	static int sequence_array[] = new int[10];
	public static int check;
	static String ipAdd = "10.0.2.2";
	public static final int portServer = 10000;
	static final int portEmu1 = 11108;
	static final int portEmu2 = 11112;
	static final int portEmu3 = 11116;
	static final int portEmu4 = 11120;
	static final int portEmu5 = 11124;
	int counter = 0;
	int i = 0;
	static int k;
	Boolean lock_id = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		chatMsg = (TextView) findViewById(R.id.chatMsg);
		chatMsg.setMovementMethod(new ScrollingMovementMethod());
		telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		chatMsg.setText("No messages from your friend");
		msgBox = (EditText) findViewById(R.id.msgBox);
		send = (Button) findViewById(R.id.sendButton);
		test1 = (Button) findViewById(R.id.test1Button);
		test2 = (Button) findViewById(R.id.test2Button);
		chatMsg.setMovementMethod(new ScrollingMovementMethod());
		phoneNo = telephony.getLine1Number();

		for (k = 0; k < 5; k++) {
			sequence_array[k] = 0;
		}
		buffered_list = new ArrayList<String>();
		socketConnList = new ArrayList<Integer>();
		socketConnList.add(portEmu1);
		socketConnList.add(portEmu2);
		socketConnList.add(portEmu3);
		 socketConnList.add(portEmu4);
		 socketConnList.add(portEmu5);

		send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Thread clientT = new Thread(new ClientThread(), "client thread");
				clientT.start();

			}
		});

		test1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread clientTest1 = new Thread(new ClientThreadtest1(),
						"client thread");
				clientTest1.start();

			}
		});

		test2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread clientTest2 = new Thread(new ClientThreadtest2(),
						"client thread");
				clientTest2.start();
			}
		});

		new ServerThread(this.getApplicationContext()).execute("hi");// Call to
																		// Server
																		// Asynctask

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {
			servSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Runnable serverHandler = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			chatMsg.setText(stringBuff_serv.toString());
			msgBox.setText("");
			stringBuff.equals("");

		}
	};

	Runnable clientHandler = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			chatMsg.setText(stringBuff.toString());
			msgBox.setText("");
		}
	};

	class ClientThread implements Runnable { // Client thread which performs
												// client opertaions
		public void run() {
			// TODO Auto-generated method stub
			String a;
			String sequence = "";
			try {
				if (phoneNoEmu1.equals(phoneNo)) {
					msg = msgBox.getText().toString();
					if (!msg.equals("")) {
						sequence_array[0]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}

						a = sequence + ":" + "5554" + ":" + i + ":" + msg;
						i++;
						multicast_msg(a);

						stringBuff.equals("");

					}
				} else if (phoneNoEmu2.equals(phoneNo)) {

					msg = msgBox.getText().toString();
					if (!msg.equals("")) {
						sequence_array[1]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}

						a = sequence + ":" + "5556" + ":" + i + ":" + msg;
						i++;
						multicast_msg(a);
						stringBuff.equals("");

					}
				} else if (phoneNoEmu3.equals(phoneNo)) {
					msg = msgBox.getText().toString();
					if (!msg.equals("")) {
						sequence_array[2]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}

						a = sequence + ":" + "5558" + ":" + i + ":" + msg;
						multicast_msg(a);
						stringBuff.equals("");

						i++;
					}
					 }else if (phoneNoEmu4.equals(phoneNo)) {
					 msg = msgBox.getText().toString();
					 if (!msg.equals("")) {
					 sequence_array[2]++;
					 sequence = sequence + sequence_array[0];
					 for (k = 1; k < 5; k++) {
					 sequence = sequence + "," + sequence_array[k];
					 }
					
					 a = sequence + ":" + "5560" + ":" + i + ":" + msg;
					 multicast_msg(a);
					 stringBuff.equals("");
					
					 i++;
					 }
					 }else if (phoneNoEmu5.equals(phoneNo)) {
					 msg = msgBox.getText().toString();
					 if (!msg.equals("")) {
					 sequence_array[2]++;
					 sequence = sequence + sequence_array[0];
					 for (k = 1; k < 5; k++) {
					 sequence = sequence + "," + sequence_array[k];
					 }
					
					 a = sequence + ":" + "5562" + ":" + i + ":" + msg;
					 multicast_msg(a);
					 stringBuff.equals("");
					
					 i++;
					 }
				}

			} catch (Exception e1) {
				chatMsg.setText("Error encountered");
			}
		}

	}

	static public void multicast_msg(String a) {// Multicasting
		// TODO Auto-generated method stub
		PrintWriter toServer;

		try {

			for (k = 0; k < socketConnList.size(); k++) {
				cliSocket = new Socket(ipAdd, socketConnList.get(k));
				toServer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(cliSocket.getOutputStream())),
						true);
				toServer.println(a);
				stringBuff.append(a).append(
						System.getProperty("line.separator"));
				// handlerMain.post(clientHandler);
				cliSocket.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ClientThreadtest1 implements Runnable { // Client thread for test case
													// 1

		public void run() {
			int t;
			String a;
			String sequence = "";
			try {
				if (phoneNoEmu1.equals(phoneNo)) {
					for (t = 0; t < 5; t++) {

						sequence_array[0]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}

						lock_id = true;
						a = "Data" + ":" + "5554" + ":" + i + ":" + "5554"
								+ "-" + i;
						i++;
						lock_id = false;
						multicast_msg(a);

						stringBuff.equals("");

						Thread.currentThread();

						Thread.sleep(3000);

					}
				} else if (phoneNoEmu2.equals(phoneNo)) {

					for (t = 0; t < 5; t++) {

						sequence_array[1]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}
						while (lock_id) {
						}
						lock_id = true;
						a = "Data" + ":" + "5556" + ":" + i + ":" + "5556"
								+ "-" + i;
						i++;
						lock_id = false;
						multicast_msg(a);
						stringBuff.equals("");

						Thread.currentThread();

						Thread.sleep(3000);

					}

				} else if (phoneNoEmu3.equals(phoneNo)) {
					for (t = 0; t < 5; t++) {
						sequence_array[2]++;
						sequence = sequence + sequence_array[0];
						for (k = 1; k < 5; k++) {
							sequence = sequence + "," + sequence_array[k];
						}

						lock_id = true;
						a = "Data" + ":" + "5558" + ":" + i + ":" + "5558"
								+ "-" + i;
						i++;
						lock_id = false;
						multicast_msg(a);
						stringBuff.equals("");

						Thread.currentThread();
						Thread.sleep(3000);
					}

				}
				 else if (phoneNoEmu4.equals(phoneNo)) {
				
				 for(t=0;t<5;t++){
				
				 sequence_array[1]++;
				 sequence = sequence + sequence_array[0];
				 for (k = 1; k < 5; k++) {
				 sequence = sequence + "," + sequence_array[k];
				 }
				 while(lock_id){}
				 lock_id=true;
				 a = "Data" + ":" + "5560" + ":" + i + ":"+ "5560" + "-" +i;
				 i++;
				 lock_id=false;
				 multicast_msg(a);
				 stringBuff.equals("");
				
				
				 Thread.currentThread();
				
				 Thread.sleep(3000);
				
				 }
				
				 }
				 else if (phoneNoEmu5.equals(phoneNo)) {
				
				 for(t=0;t<5;t++){
				
				 sequence_array[1]++;
				 sequence = sequence + sequence_array[0];
				 for (k = 1; k < 5; k++) {
				 sequence = sequence + "," + sequence_array[k];
				 }
				 while(lock_id){}
				 lock_id=true;
				 a = "Data" + ":" + "5562" + ":" + i + ":"+ "5562" + "-" +i;
				 i++;
				 lock_id=false;
				 multicast_msg(a);
				 stringBuff.equals("");
				
				
				 Thread.currentThread();
				
				 Thread.sleep(3000);
				
				 }
				
				 }
			} catch (Exception e1) {
				chatMsg.setText("Error encountered");
			}
		}

	}

	class ClientThreadtest2 implements Runnable { // Client thread for test case
													// 1

		public void run() {
			int t;
			String a;
			String sequence = "";
			try {
				if (phoneNoEmu1.equals(phoneNo)) {

					sequence_array[0]++;
					sequence = sequence + sequence_array[0];
					for (k = 1; k < 5; k++) {
						sequence = sequence + "," + sequence_array[k];
					}

					lock_id = true;
					a = "Test2" + ":" + "5554" + ":" + i + ":" + "5554" + "-"
							+ counter + ":" + counter;
					i++;
					lock_id = false;
					Thread.currentThread();
					Thread.sleep(3000);
					multicast_msg(a);

					stringBuff.equals("");

				} else if (phoneNoEmu2.equals(phoneNo)) {

					sequence_array[1]++;
					sequence = sequence + sequence_array[0];
					for (k = 1; k < 5; k++) {
						sequence = sequence + "," + sequence_array[k];
					}
					while (lock_id) {
					}
					lock_id = true;
					a = "Test2" + ":" + "5556" + ":" + i + ":" + "5556" + "-"
							+ counter + ":" + counter;
					i++;
					lock_id = false;
					Thread.currentThread();
					Thread.sleep(3000);
					multicast_msg(a);
					stringBuff.equals("");

				} else if (phoneNoEmu3.equals(phoneNo)) {
					sequence_array[2]++;
					sequence = sequence + sequence_array[0];
					for (k = 1; k < 5; k++) {
						sequence = sequence + "," + sequence_array[k];
					}

					lock_id = true;
					a = "Test2" + ":" + "5558" + ":" + i + ":" + "5558" + "-"
							+ counter + ":" + counter;
					i++;
					lock_id = false;
					Thread.currentThread();
					Thread.sleep(3000);
					multicast_msg(a);
					stringBuff.equals("");

				} else if (phoneNoEmu4.equals(phoneNo)) {

					sequence_array[1]++;
					sequence = sequence + sequence_array[0];
					for (k = 1; k < 5; k++) {
						sequence = sequence + "," + sequence_array[k];
					}
					while (lock_id) {
					}
					lock_id = true;
					a = "Test2" + ":" + "5560" + ":" + i + ":" + "5560" + "-"
							+ i;
					i++;
					lock_id = false;
					Thread.currentThread();
					Thread.sleep(3000);
					multicast_msg(a);
					stringBuff.equals("");

				} else if (phoneNoEmu5.equals(phoneNo)) {

					sequence_array[1]++;
					sequence = sequence + sequence_array[0];
					for (k = 1; k < 5; k++) {
						sequence = sequence + "," + sequence_array[k];
					}
					while (lock_id) {
					}
					lock_id = true;
					a = "Test2" + ":" + "5562" + ":" + i + ":" + "5562" + "-"
							+ i;
					i++;
					lock_id = false;
					Thread.currentThread();
					Thread.sleep(3000);
					multicast_msg(a);
					stringBuff.equals("");

				}
			} catch (Exception e1) {
				chatMsg.setText("Error encountered");
			}
		}

	}
}
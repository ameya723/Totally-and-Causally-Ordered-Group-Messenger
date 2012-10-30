package com.last;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.util.Log;

public class ServerThread extends AsyncTask<String, String, String> {

	static int portServer = 10000;
	static int k;
	private ServerSocket servSocket;
	Socket servAccepted;
	static Socket forSequencer;
	static String temp[];
	static String vector[];
	static int timestamp[] = new int[5];
	static String portStr;
	int check;
	int check2;
	int noofemu=5;
	String new_msg;
	String packet_check[];
	String chat = "";
	String caught;
	String toUI;
	int temp_seqno;
	static int seqno_seq = 1;
	Context context;
	String msg_count;
	ArrayList<String> seq_buff = new ArrayList<String>();
	static Boolean lock_queue = false;
	static Boolean lock_buff = false;
	static int db_key;
	static Boolean lock_seqno = false;
	int flag = 0;
	static ArrayList<String> accepted_list=new ArrayList<String>();
	int c = 1;
	int index;
	String temp_order="";

	public ServerThread(Context aContext) {
		// TODO Auto-generated constructor stub
		try {
			servSocket = new ServerSocket(SimpleChat.portServer);
			portStr = SimpleChat.telephony.getLine1Number().substring(
					SimpleChat.telephony.getLine1Number().length() - 4);
			this.context = aContext;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		SimpleChat.chatMsg.append("\n" + caught);
		// SimpleChat.msgBox.setText("");
		SimpleChat.stringBuff.equals("");

		insertintoCP(context);
	}

	@Override
	protected String doInBackground(String... params) {
		synchronized (servSocket) {

			if (servSocket != null) {
				try {
					// try {
					// servSocket.wait(3000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					while (true) {
						servAccepted = servSocket.accept();
						

						BufferedReader fromClient = new BufferedReader(
								new InputStreamReader(
										servAccepted.getInputStream()));
						chat = fromClient.readLine();
						packet_check = chat.split(":");
						if (packet_check[0].equals("order")) {
							
							// Thread total = new
							// Thread(Total_ordering(packet_check));
							temp_order = packet_check[1] + ":"
									+ packet_check[2];
							temp_seqno = Integer.parseInt(packet_check[3]);
							db_key=temp_seqno;
							while(lock_seqno){}
							lock_seqno=true;
							if (SimpleChat.seqno == temp_seqno) {
								while(lock_queue){}
								lock_queue=true;
								index = accepted_list.indexOf(temp_order);
								if (index > -1) {
									msg_count = accepted_list.get(index);
									caught = accepted_list.get(index + 1);
									accepted_list.remove(index);
									accepted_list.remove(index);
									publishProgress(caught, msg_count);
									while(lock_buff)
										lock_queue=true;
									int buff_index = seq_buff
											.indexOf(temp_seqno + c);
									while (buff_index > -1) {
										index = accepted_list
												.indexOf(buff_index + 1);
										caught = accepted_list.get(index + 1);
										accepted_list.remove(index);
										accepted_list.remove(index);
										seq_buff.remove(buff_index);
										seq_buff.remove(buff_index);

										publishProgress(caught, msg_count);
										c++;
										buff_index = seq_buff
												.indexOf(temp_seqno + c);
										lock_buff=false;
									}
									lock_queue=false;
									SimpleChat.seqno++;
								}
							} else {
								while(lock_buff)
								lock_queue=true;
								seq_buff.add(packet_check[3]);
								seq_buff.add(temp_order);
								lock_buff=false;
							}
							lock_seqno=false;
							
							servAccepted.close();
						}else if(packet_check[0].equals("Test2")){
							
							int tp;
							tp=check_vector_clock(chat);
							int incoming_port=(Integer.parseInt(temp[1])-5554)/2;
							int current_port=(Integer.parseInt(portStr)-5554)/2;

							if((incoming_port+1)%noofemu==current_port){
							if(Integer.parseInt(temp[4])<noofemu){
								
								int k=Integer.parseInt(temp[4])+1;
								tp=Integer.parseInt(temp[2]);
								if(Integer.parseInt(temp[4])==4){
									tp++;
								}
								new_msg="Test2"+":"+portStr+":"+tp+":"+portStr+"-"+tp+":"+ k;
								Log.d("msg",":"+new_msg);
								SimpleChat.multicast_msg(new_msg);
								
							}
							}
							if (SimpleChat.phoneNoEmu1
									.equals(SimpleChat.phoneNo)) {
								// Thread.sleep(2000);
								SimpleChat.stringBuff_serv
										.append("Sequencer :")
										.append(chat)
										.append(System
												.getProperty("line.separator"));
								Sequencer(chat);
								
							}
						
						}					
						else {

							check = check_vector_clock(chat);

							
							if (SimpleChat.phoneNoEmu1
									.equals(SimpleChat.phoneNo)) {
								// Thread.sleep(2000);
								SimpleChat.stringBuff_serv
										.append("Sequencer :")
										.append(chat)
										.append(System
												.getProperty("line.separator"));
								Sequencer(chat);
								
							}

							servAccepted.close();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
		}
		return null;
	}

	
	public static int check_vector_clock(String temp_chat) {
		// TODO Auto-generated method stub
		temp = temp_chat.split(":");
		vector = temp[0].split(",");
		
		while(lock_queue){}
		lock_queue=true;
		accepted_list.add(temp[1] + ":" + temp[2]);
		accepted_list.add(temp[1] + ":" + temp[3]);
		SimpleChat.stringBuff_serv.append(temp[1]).append(":").append(temp[3])
				.append(System.getProperty("line.separator"));
		lock_queue=false;
		
		return 1;
	}

	public static int buffermsg() {
		// TODO Auto-generated method stub
		int msgfrom;
		int identifier;
		int flag = 0;
		msgfrom = Integer.parseInt(temp[1]);
		identifier = (msgfrom - 5554) / 2;
		if (portStr.equals(temp[1])) {
			return 1;
		} else {
			for (k = 0; k < 5; k++) {
				if (k == identifier) {
					if (timestamp[k] == SimpleChat.sequence_array[k] + 1) {
						flag = 1;
					} else {
						flag = 0;
						break;
					}
				} else {
					if (SimpleChat.sequence_array[k] <= timestamp[k]) {
						flag = 1;
					} else {
						flag = 0;
						break;
					}
				}

			}
		}

		if (flag == 1) {
			SimpleChat.sequence_array[identifier]++;
		}
		return flag;
	}

	public static void Sequencer(String temp_msg) {

		// TODO Auto-generated method stub

		String msg_id = "";
		String forsplit[];
		String token = "";
		PrintWriter toemulators;
		int p;
		forsplit = temp_msg.split(":");
		msg_id = forsplit[1] + ":" + forsplit[2];
		while(lock_seqno){}
		lock_seqno=true;
		token = "order" + ":" + msg_id + ":" + seqno_seq;
		lock_seqno=false;
		try {
			for (k = 0; k < SimpleChat.socketConnList.size(); k++) {
				forSequencer = new Socket(SimpleChat.ipAdd,
						SimpleChat.socketConnList.get(k));
				if (forSequencer != null) {
					toemulators = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(
									forSequencer.getOutputStream())), true);
					toemulators.println(token);
					forSequencer.close();
				}
			}
			while(lock_seqno){}
				lock_seqno=true;
			seqno_seq++;
			lock_seqno=false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertintoCP(Context new_context) {
		// TODO Auto-generated method stub
		ContentValues new_value = new ContentValues();
		new_value.put(DatabaseContent.COLUMN_KEY, db_key);
		new_value.put(DatabaseContent.COLUMN_VAL, caught);
		new_context.getApplicationContext();
		Uri uri = context.getContentResolver().insert(ContentProv.cp_uri,
				new_value);
		Log.d("Data Inserted : ", " " + new_value);

	}

}

package com.wanli.classforereryone.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Array;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.Mmmm;
import com.wanli.swing.entities.OnlineUser;
import com.wanli.swing.frame.MessagePOP_UP;
import com.wanli.swing.service.DBService;
import com.wanli.swing.service.DBServiceUser;
import com.wanli.utils.StaticVariable;

public class ServerThread implements Runnable {

	// ���嵱ǰ�߳��������Socket
	private Socket s = null;
	// ���߳��������Socket����Ӧ��������
	BufferedReader br = null;
	// ���������Ϸ���˵Ŀͻ���IP��ַ
	private static String ipAddress = "";
	// �洢ѧ�����������
	private String question;
	// �����û����ݿ�
	private DBServiceUser dbServiceUser;
	// �����ɼ������ݿ�
	private DBService dbService;
	// ����ѧ���Ĵ�
	private String[] answers;
	
	public ServerThread(Socket s) throws IOException {
		this.s = s;
		// ��ʼ����Socket��Ӧ��������
		br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
		dbServiceUser = new DBServiceUser();
		dbService = new DBService();
		
	}
	
	@Override
	public void run() {
		
		String content = null;
		int index = 0;
		ipAddress = s.getInetAddress().toString().substring(1);
		// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
		while ((content = readFromClient()) != null) {
			
			String[] info = content.split(",");
//			System.out.println(Arrays.toString(info));
			switch(info[0]) {
				// ��ȡע����Ϣ
				case "1":
					if (dbServiceUser.addUser(info)) {
						sendToClient("1");
					} else {
						sendToClient("1-false");
					}
					break;
				// ��ȡ��¼��Ϣ
				case "2":
					if (dbServiceUser.getUserByNameAndPassword(info[1], info[2])) {
						// �пͻ������ӾͰ����ӵĿͻ���ʹ��map�洢
						try {
							System.out.println("�ͻ������ӳɹ�������");
							StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendToClient("2");
						System.out.println("��¼�ˡ���������");
						StaticVariable.users.get(ipAddress).setInetAddress(s.getInetAddress().toString().substring(1));
						StaticVariable.users.get(ipAddress).setUsername(info[1]);
					} else {
						sendToClient("2-false");
					}
					break;
				// ��ȡ�ش��������Ϣ
				case "3":
					if (StaticVariable.questions != null) {
						if (answers == null) {
							answers = new String[StaticVariable.questions.length - 1];
							answers[index] = info[2];
							index++;						
						} else {
							if (index < answers.length) {
								System.out.println(info[2]);
								answers[index] = info[2];
								if (index == answers.length - 1) {
									System.out.println(Arrays.toString(answers));
									dbService.addRecord(info[1], StaticVariable.tableName, answers);
								}
								index++;
							} else {
								index = 0;
							}							
						}
					}
//					System.out.println(info[2]);
					break;
				// ��ȡ���ʵ���Ϣ�����ҵ�����ʾ��ѧ������
				case "4":
					question = info[1] + ":::" + info[2]; 
					new Thread(new ListenningMessage(question)).start();
					StaticVariable.users.get(ipAddress).setContent(info[2]);
					System.out.println(info[0] + "," + info[1] + "," + info[2]);				
					break;
			}
			
		}
		// ����ѭ����������Socket��Ӧ�Ŀͻ����Ѿ��ر�
		// ɾ����Socket
		StaticVariable.quitSocket = s.getInetAddress().toString().substring(1);
		StaticVariable.users.remove(s.getInetAddress().toString().substring(1));
	}

	// �����ȡ�ͻ������ݵķ���
	private String readFromClient() {
		try {
			return br.readLine();
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("�ͻ����˳��ˡ�����");
		}
		return null;
	}
	
	/**
	 * �����пͻ��˷�����Ϣ
	 * @param msg
	 */
	public static void sendToAllClient(String msg) {
		// �������е������ӵĿͻ��ˣ�����Ϣ���͸����еĿͻ���
		for (Map.Entry<String, OnlineUser> user: StaticVariable.users.entrySet()) {
			try {
				System.out.println(msg);
				OnlineUser val = user.getValue();
                PrintWriter pw =val.getPw();  
                pw.println(msg);  
                pw.flush();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
		}
	}
	
	/**
	 * ��ͻ��˷�����Ϣ
	 * @param msg
	 */
	public void sendToClient(String msg) {
		System.out.println(msg);
        PrintWriter pw = null;
		try {
			pw = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		};
		if (pw != null) {
			pw.println(msg);  
			pw.flush();			
		}
	}
	
	public static String getIpAddress() {
		return ipAddress;
	}
	
}

/**
 * ���ͻ�������Ϣ���͹���ʱ��ִ���߳�
 * @author wanli
 *
 */
class ListenningMessage implements Runnable {

	private String question;// �ͻ����ύ������
	
	public ListenningMessage(String question) {
		this.question = question;
	}
	
	@Override
	public void run() {
		Display.getDefault().syncExec(new Runnable(){
			public void run() {
				// �ڱ�������һ��
				TableItem item = new TableItem(StaticVariable.askQuestions, SWT.NONE);
				item.setImage(new Image(StaticVariable.parent.getDisplay(), "image/unanswer.png"));
				item.setText(question);
				item.setFont(new Font(StaticVariable.parent.getDisplay(), "Arial", 20, SWT.NONE));
				StaticVariable.unanswerMap.put(item, true);
				StaticVariable.askQuestion.setText(StaticVariable.unanswerMap.size() + " ����");
				// ʹ�õ����ķ�ʽ���ѿͻ�������Ϣ���͹���
				MessagePOP_UP window = new MessagePOP_UP();
				window.open();
			}	
		});
	}
	
}

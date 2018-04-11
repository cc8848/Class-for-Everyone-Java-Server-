package com.wanli.classforereryone.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.Mmmm;
import com.wanli.swing.entities.OnlineUser;
import com.wanli.swing.frame.MessagePOP_UP;
import com.wanli.utils.StaticVariable;

public class ServerThread implements Runnable {

	// ���嵱ǰ�߳��������Socket
	Socket s = null;
	// ���߳��������Socket����Ӧ��������
	BufferedReader br = null;
	// ���������Ϸ���˵Ŀͻ���IP��ַ
	private static String ipAddress = "";
	// �洢ѧ�����������
	private String question;
	private 
	
	public ServerThread(Socket s) throws IOException {
		this.s = s;
		// ��ʼ����Socket��Ӧ��������
		br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
	}
	
	@Override
	public void run() {
		
		String content = null;
		// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
		while ((content = readFromClient()) != null) {
			
			String[] info = content.split(",");
			switch(info[0]) {
				// ��ȡע����Ϣ
				case "1":
					
					break;
				// ��ȡ��¼��Ϣ
				case "2":
					break;
				// ��ȡ�ش��������Ϣ
				case "3":
					break;
				// ��ȡ���ʵ���Ϣ
				case "4":
					break;
			}
			StaticVariable.users.get(s.getInetAddress().toString().substring(1)).setInetAddress(s.getInetAddress().toString().substring(1));
			StaticVariable.users.get(s.getInetAddress().toString().substring(1)).setUsername(info[0]);
			StaticVariable.users.get(s.getInetAddress().toString().substring(1)).setImei(info[1]);
			ipAddress = s.getInetAddress().toString().substring(1);
			// �����͵���Ϣ�ǿͻ��˷��͵�������Ϣ���򵯳���ʾ��
			if (info.length == 3) {
				question = info[0] + ":::" + info[2];				
				new Thread(new ListenningMessage(question)).start();
				StaticVariable.users.get(s.getInetAddress().toString().substring(1)).setContent(info[2]);
				System.out.println(info[0] + "," + info[1] + "," + info[2]);				
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
			e.printStackTrace();
		}
		return null;
	}
	
	// ��ͻ��˷�����Ϣ
	public static void sendToClient(String msg) {
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
				StaticVariable.askQuestion.setText(StaticVariable.unanswerNum + " ����");
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

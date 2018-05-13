package com.wanli.classforereryone.server;

import static org.hamcrest.CoreMatchers.startsWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.aliyuncs.exceptions.ClientException;
import com.wanli.swing.Mmmm;
import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.OnlineUser;
import com.wanli.swing.frame.MessagePOP_UP;
import com.wanli.swing.service.DBService;
import com.wanli.swing.service.DBServiceUser;
import com.wanli.utils.MailUtils;
import com.wanli.utils.Randomutil;
import com.wanli.utils.SmsUtils;
import com.wanli.utils.StaticVariable;

/**
 * ÿ���ͻ���ר���̣߳����Խ��տͻ��˷��͵���Ϣ��Ҳ������ͻ��˷�����Ϣ
 * @author wanli
 *
 */
public class ServerThread implements Runnable {

	// ���嵱ǰ�߳��������Socket
	private static Socket s = null;
	// ���߳��������Socket����Ӧ��������
	BufferedReader br = null;
	// ���������Ϸ���˵Ŀͻ���IP��ַ
	private static String ipAddress = "";
	// �洢ѧ�����������
	private String question;
	// �洢���н���
	private StringBuilder allClass = new StringBuilder();
	// �����û����ݿ�
	private DBServiceUser dbServiceUser;
	// �����ɼ������ݿ�
	private DBService dbService;
	// ���ɵ�6λ��֤��
	private String randomNum;
	// ��¼���û���
	private static String userName;
	
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
		ipAddress = s.getInetAddress().toString().substring(1);
		// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
		while ((content = readFromClient()) != null) {
			
			String[] info = content.split("#\\^");
			System.out.println(Arrays.toString(info));
			switch(info[0]) {
				// ��ȡע����Ϣ
				// �ͻ��˷��͹������ַ�����ʽ���������,�ֻ���,�ǳ�,����,����,ip��ַ
				case "1":
					
					if (dbServiceUser.addUser(info)) {
						// ע��ɹ�
						sendToClient("1", info[5]);
					} else {
						// ע��ʧ��
						sendToClient("1-false", info[5]);
					}
					break;
				// ��ȡ��¼��Ϣ
				// �ͻ��˷��͹������ַ�����ʽ���������,�ֻ�������,����,ip��ַ
				case "2":
//					System.out.println(Arrays.toString(info));
					userName = dbServiceUser.getUserByNameAndPassword(info[1], info[2]);
					if (userName != null) {
						// �пͻ������ӾͰ����ӵĿͻ���ʹ��map�洢
						System.out.println("�ͻ������ӳɹ�������");
//						try {
//							StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
//						} catch (IOException e) {
//							System.out.println("��¼ʧ��...");
//							e.printStackTrace();
//						}
						
						// ��¼�ɹ������ǳƷ��͸��ͻ���
						sendToClient("2#^" + userName, info[3]);
//						System.out.println("��¼�ˡ���������");
	
					} else {
						// ��¼ʧ��
						sendToClient("2-false", info[3]);
					}
					break;
				// ��ȡ�ش��������Ϣ
				// �ͻ��˷��͹������ַ�����ʽ���������,�ǳ�,����,ip��ַ
				case "3":
//					StaticVariable.correct.clear();
//					StaticVariable.error.clear();
//					StaticVariable.unResponse.clear();
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							// StaticVariable.questionSelect.getSelectionIndex() > 0��ʾ�Ѿ�ѡ����Ŀ�����Խ��д���
							if (StaticVariable.questionSelect.getSelectionIndex() > 0) {
								String[] userAnswers = info[2].split(" ");
								// �����û��ش�Ĵ�
								List<String> answerList = new ArrayList<>();
								StaticVariable.answers.put(info[1], "");
								StaticVariable.answers.replace(info[1], info[2]);
								int index = StaticVariable.questionSelect.getSelectionIndex();
//								String[] strs = StaticVariable.questionsMap.get(Integer.toString(index)).split(",");
								String[] strs = StaticVariable.questionsList.get(index - 1).split("#\\^");
								if (strs[0].equals("fill_in_the_blanks")) {
									// answers.length > 1 ��ʾ�ж����
									if ((strs.length - 2) > 1) {
										// ������ÿո��ֿ������˵�����Ŀո�
										for (int i = 0; i < userAnswers.length; i++) {
											if (!userAnswers[i].equals("")) {
												answerList.add(userAnswers[i]);
											}
										}
//										// ��ʼ���洢��ȷ�𰸸���������𰸸�����δ�ش������list
//										for (int i = 2; i < strs.length; i++) {
//											StaticVariable.correct.add(new Integer(0));
//											StaticVariable.error.add(new Integer(0));
//											StaticVariable.unResponse.add(new Integer(0));					
//										}
										// �ֱ����ش���ȷ�ͻش����ĸ���
										for (int i = 2; i < strs.length; i++) {
											if (answerList.size() > 1) {
												// �ж���ȷ���
												if (strs[i].equals(answerList.get(i - 2))) {
													// �ش���ȷ
													int value = StaticVariable.correct.get(i - 2).intValue();
													value++;
													StaticVariable.correct.set(i - 2, new Integer(value));
												} else {
													// �ش����
													int value = StaticVariable.error.get(i - 2).intValue();
													value++;
													StaticVariable.error.set(i - 2, new Integer(value));
												}
											} else {
												for (int j = 2; j < strs.length; j++) {
													if (j == 2) {
														if (strs[j].equals(answerList.get(j - 2))) {
															// �ش���ȷ
															int value = StaticVariable.correct.get(j - 2).intValue();
															value++;
															StaticVariable.correct.set(j - 2, new Integer(value));
														} else {
															// �ش����
															int value = StaticVariable.error.get(j - 2).intValue();
															value++;
															StaticVariable.error.set(j - 2, new Integer(value));
														}	
													} else {
														int value = StaticVariable.unResponse.get(j - 2).intValue();
														value++;
														StaticVariable.unResponse.set(j - 2, new Integer(value));
													}
												}
												break;
											}
										}
									} 
								} else {
									// ֻ��һ����
									answerList.add(info[2]);
									// �ж���ȷ���
									if (answerList.get(0).equals(strs[2])) {
										// �ش���ȷ
										int value = StaticVariable.correct.get(0).intValue();
										value++;
										StaticVariable.correct.set(0, new Integer(value));
									} else {
										// �ش����
										int value = StaticVariable.error.get(0).intValue();
										value++;
										StaticVariable.error.set(0, new Integer(value));
									}
									if (StaticVariable.options.get(info[2]) != null) {
										int option = StaticVariable.options.get(info[2]);
										option++;
										StaticVariable.options.replace(info[2], Integer.valueOf(option));
									}
//									System.out.println(StaticVariable.options);
								}
//								System.out.println("StaticVariable.answers:" + StaticVariable.answers);
//								System.out.println("answerList:" + answerList);
//								System.out.println("StaticVariable.correct:" + StaticVariable.correct);
//								System.out.println("StaticVariable.error:" + StaticVariable.error);
							}
						}
					});
//					System.out.println(info[2]);
					break;
				// ��ȡ���ʵ���Ϣ�����ҵ�����ʾ��ѧ������
				// �ͻ��˷��͹������ַ�����ʽ���������,�ǳ�,����
				case "4":
					// questionΪ������ʾ�����ʵ�����
					question = info[1] + ":::" + info[2]; 
					new Thread(new ListenningMessage(question)).start();
//					StaticVariable.users.get(ipAddress).setContent(info[2]);
//					System.out.println(info[0] + "," + info[1] + "," + info[2]);				
					break;
				// �������룬��������
				// �ͻ��˷��͹������ַ�����ʽ���������,�ֻ�������,����,ip��ַ
				case "5":
					// ������Ϊnull�����ʾ�ǲ�ѯ�ֻ��Ż������Ƿ��Ѿ�ע���
					if (info[2].equals("null")) {
						if (dbServiceUser.getByUsername(info[1])) {
							// ����ҵ����û���˵���Ѿ���ע��������ŷ�����֤��
							boolean result = info[1].matches("[0-9]+");
							randomNum = Randomutil.getRandom();
							if (result) {
								try {
									SmsUtils.sendSms(info[1], randomNum);
								} catch (ClientException e) {
									System.out.println("���ŷ���ʧ��");
									e.printStackTrace();
								}
							} else {
								MailUtils.sendMail(info[1], randomNum);
							}
							// ע������ͻ��˷�����ʾ��Ϣ��������֤�뷢�͸��ͻ���
							sendToClient("5#^" + randomNum, info[3]);
						} else {
							// δע������ͻ��˷���ʧ����ʾ
							sendToClient("5-false", info[3]);
						}
					} else {
						// �����벻��null�����ʾ���޸�����
						dbServiceUser.updatePassword(info[1], info[2]);
						sendToClient("5", info[3]);
					}
					break;
				// ��ͻ��˷��ͽ�����Ϣ
				// �����ʽ��6��ip��ַ
				case "6":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							if (StaticVariable.rooms.size() > 0) {
								allClass.setLength(0);
								allClass.append("6");
//						System.out.println(StaticVariable.rooms.size());
//								for (int i = 0; i < StaticVariable.rooms.size(); i++) {
//									if (i == 0) {
//										allClass.append("6#^" + StaticVariable.rooms.get(i).getText());	
//									} else {
////								System.out.println(StaticVariable.rooms.get(i).getText());
//										allClass.append("#^" + StaticVariable.rooms.get(i).getText());
//									}
//								}
								for (Map.Entry<String, TreeItem> item: StaticVariable.rooms.entrySet()) {
									allClass.append("#^" + item.getValue().getText());
								}
								
//								System.out.println(allClass.toString());
								// ��ͻ��˷��ͽ�����Ϣ
								sendToClient(allClass.toString(), info[1]);
							} else {
								sendToClient("6-false", info[1]);
							}
							
						}
					});
					break;
				// ��ȡ��Ŀ
				// �����ʽ����ţ�ip��ַ
				case "7":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							int index = StaticVariable.questionSelect.getSelectionIndex();
							System.out.println(index);
							if (index > 0) {
//						sendToClient(StaticVariable.questionsMap.get(Integer.toString(index)));
								sendToClient(StaticVariable.questionsList.get(index - 1), info[1]);
							} else {
								sendToClient("7-false", info[1]);
							}							
						}
					});
					break;
				// У��ͻ��˷��͹�������֤���Ƿ���ȷ
				// �����ʽ����ţ���֤�룬ip��ַ
				case "8":
					if (info[1].equals(randomNum)) {
						sendToClient("8", info[2]);
					} else {
						sendToClient("8-false", info[2]);
					}
					break;
				// ���տͻ��˷��͹����ļ�����ҵ���Ϣ
				// �����ʽ����ţ��������ƣ�ip��ַ
				case "9":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							TreeItem treeItem = new TreeItem(StaticVariable.rooms.get(info[2]), SWT.NONE);
							treeItem.setText(info[1]);
							StaticVariable.onlineUsers.put(ServerThread.getIpAddress(), treeItem);
							try {
								StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
								StaticVariable.users.get(ipAddress).setInetAddress(s.getInetAddress().toString().substring(1));
								StaticVariable.users.get(ipAddress).setUsername(info[1]);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
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
	
//	public static void sendToClient(ChoiceQuestion choice) {
//		String no = "1";
//		String question = "1111";
//		List<String> options = new ArrayList<>();
//		options.add("A.1");
//		options.add("B.2");
//		options.add("C.3");
//		options.add("D.4");
//		String answer = "A";
//		choice = new ChoiceQuestion(no, question, answer, options);
//		PrintWriter pw = null;
//		try {
//			pw = new PrintWriter(s.getOutputStream());
//		} catch (IOException e) {
//			e.printStackTrace();
//		};
//		if (pw != null) {
//			pw.println(choice);  
//			pw.flush();			
//		}
//	}
	
	/**
	 * ��ͻ��˷�����Ϣ
	 * @param msg
	 */
	public void sendToClient(String msg, String ipAdress) {
        PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(StaticVariable.usersSocket.get(ipAdress).getOutputStream(),"utf-8"),true);
//			System.out.println(">>>" + userName);
//			System.out.println(">>>" + StaticVariable.usersSocket.size());
//			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(),"utf-8"),true);
//			System.out.println(StaticVariable.usersSocket.get(userName).getInetAddress());
//			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		};
		if (pw != null) {
			pw.println(msg);  
			pw.flush();	
//System.out.println("������Ϣ");
		}
	}
	
	// ��ȡ���ӵĿͻ��˵�ip��ַ
	public static String getIpAddress() {
		return ipAddress;
	}
	
	public static String getUserName() {
		return userName;
	}
	
}

/**
 * ���ͻ�������Ϣ���͹���ʱ��ִ���̣߳���ʾ�ͻ��˵�������Ϣ
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

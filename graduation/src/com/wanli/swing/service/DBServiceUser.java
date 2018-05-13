package com.wanli.swing.service;

import java.util.List;

import com.wanli.swing.dao.DBDaoUser;

/**
 * ����DBDaoUser��ҵ��
 * @author wanli
 *
 */
public class DBServiceUser {

	private DBDaoUser daoUser = null;
	
	public DBServiceUser() {
		daoUser = new DBDaoUser();
	}
	
	/**
	 * ��graduation_user���ݿ��е�userbean�����һ����¼
	 * @param userInfo���û���Ϣ����
	 * @return
	 */
	public boolean addUser(String[] userInfo) {
		return daoUser.addUser(userInfo);
	}
	
	/**
	 * �����û����������ѯ�û�
	 * @param name:�û���
	 * @param password:����
	 * @return
	 */
	public String getUserByNameAndPassword(String name, String password) {
		return daoUser.getUserByNameAndPassword(name, password);
	}
	
	/**
	 * ͨ���ֻ������޸��ʺ�����
	 * @param parameter:���������ǵ绰���룬Ҳ����������
	 * @param password:������
	 */
	public void updatePassword(String parameter, String password) {
		daoUser.updatePassword(parameter, password);
	}
	
	/**
	 * �����ֻ��Ż�����������û�
	 * @param username
	 * @return
	 */
	public boolean getByUsername(String username) {
		return daoUser.getByUsername(username);
	}
	
	/**
	 * �����ֻ�������������ȡ�û����ǳ�
	 * @param username
	 * @return
	 */
	public String getNicknameByPhoneOrEmail(String username) {
		return daoUser.getNicknameByPhoneOrEmail(username);
	}
	
	/**
	 * ��ȡ���еİ༶��רҵ��Ϣ
	 * @return
	 */
	public List<String> getAllClass() {
		return daoUser.getAllClass();
	}
	
	/**
	 * ���Ӱ༶��רҵ
	 * @param className
	 */
	public void addClass(String className) {
		daoUser.addClass(className);
	}
}

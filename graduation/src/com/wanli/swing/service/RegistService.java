package com.wanli.swing.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.wanli.swing.dao.RegistDao;
import com.wanli.swing.entities.UserBean;

/**
 * �û�ģ���������
 * @author wanli
 *
 */
@Transactional
public class RegistService {

	//ע��RegistDao
	private RegistDao registDao;
	
	public void setRegistDao(RegistDao registDao) {
		this.registDao = registDao;
	}
	
	//�û�ע��
	public void addUser(UserBean bean) {
		registDao.insert(bean);
	}
	
	//����userbean��ѯ�û�
	public UserBean getUserByBean(UserBean bean) {
		return registDao.getUserByBean(bean);
	}
	
	//����username��password��ѯ�û�
	public boolean getUserByNameAndPassword(String name, String password) {
		return registDao.getUserByNameAndPassword(name, password);
	}
	
	//����username��ѯ�û�
	public UserBean getByUsername(String username) {
		return registDao.getByUsername(username);
	}
	
	//����nickname��ѯ�û�
	public UserBean getByNickname(String nickname) {
		return registDao.getByNickname(nickname);
	}
	
	//����email��ѯ�û�
	public UserBean getByEmail(String email) {
		return registDao.getByEmail(email);
	}
	/**
	 * �޸��ʺ�����
	 * @param parameter:���������ǵ绰���룬Ҳ����������
	 * @param password:������
	 */
	public void updatePassword(String parameter, String password) {
		registDao.updatePassword(parameter, password);
	}

}
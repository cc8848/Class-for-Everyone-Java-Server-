package com.wanli.swing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wanli.swing.entities.UserBean;
import com.wanli.utils.DBUtilsUser;
import com.wanli.utils.DbUtilsScoreTab;

/**
 * �û�ģ��־ò����
 * @author wanli
 *
 */
public class RegistDao {

	//ע��SessionFactory
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void insert(UserBean bean) {
		getSession().save(bean);
	}
	
	/**
	 * �����û�����ѯ�û�
	 * @param username
	 * @return
	 */
	public UserBean getByUsername(String username) {
		String hql = "from UserBean b where b.name = ?";
		List<UserBean> list = getSession().createQuery(hql).setString(0, username).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * �����ǳƲ�ѯ�û�
	 * @param nickname
	 * @return
	 */
	public UserBean getByNickname(String nickname) {
		String hql = "from UserBean b where b.nickname = ?";
		List<UserBean> list = getSession().createQuery(hql).setString(0, nickname).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * ���������ѯ�û�
	 * @param email
	 * @return
	 */
	public UserBean getByEmail(String email) {
		String hql = "from UserBean b where b.email = ?";
		List<UserBean> list = getSession().createQuery(hql).setString(0, email).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * ����userbean�����ѯ�û�
	 * @param bean
	 * @return
	 */
	public UserBean getUserByBean(UserBean bean) {
		
		String hql = "from UserBean b where b.name = ? and b.password = ?";
		@SuppressWarnings("unchecked")
		List<Object> list = getSession().createQuery(hql).setString(0, bean.getName())
					.setString(1, bean.getPassword()).list();
		if (list != null && list.size() > 0) {
			return (UserBean) list.get(0);
		}
		return null;
	}
	
	/**
	 * �����û����������ѯ�û�
	 * @param name:�û���
	 * @param password:����
	 * @return
	 */
	public boolean getUserByNameAndPassword(String name, String password) {
		boolean is_get = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from userbean where name = ? and password = ?";
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				is_get = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return is_get;
	}
	
	/**
	 * ͨ���ֻ������޸��ʺ�����
	 * @param parameter:���������ǵ绰���룬Ҳ����������
	 * @param password:������
	 */
	public void updatePassword(String parameter, String password) {
		PreparedStatement statement = null;
		String sql = "";
		boolean result = parameter.matches("[0-9]+");
		if (result) {
			sql = "update userbean set password = ? where name = ?";			
		} else {
			sql = "update userbean set password = ? where email = ?";
		}
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, password);
			statement.setString(2, parameter);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
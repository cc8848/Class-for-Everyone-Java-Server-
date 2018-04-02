package com.wanli.swing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wanli.utils.DbUtilsScoreTab;

public class DBDao {

	/**
	 * 创建表
	 * @param num：num值指定创建的表有多少列
	 */
	public void createTable(int num, String tableName) {
		List<String> titles = new ArrayList<>();
		String title = "title";
		String sql = "create table " + tableName + "(username char(30), ";
		PreparedStatement statement = null;
		Connection connection = DbUtilsScoreTab.getConnection();
		for (int i = 1; i <= num; i++) {
			titles.add(title + i);
			if (i == num) {
				sql = sql + titles.get(i - 1) + " char(10))";
				break;
			}
			sql = sql + titles.get(i - 1) + " char(10),";
			
		}
		try {
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("创建表失败，表已经存在！请修改文件名，以保证建表成功。");
		}
	}
	
	/**
	 * 获取指定表的数据
	 * @param tableName：表名
	 */
	public List<String[]> getScoreData(String tableName) {
		List<String[]> list = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from " + tableName;
//		System.out.println(sql);
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int colCount = metaData.getColumnCount();
			resultSet.beforeFirst();
			while (resultSet.next()) {
				String[] titles = new String[colCount];
				for (int i = 1; i <= colCount; i++) {
					titles[i - 1] = resultSet.getString(i);
				}
				list.add(titles);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取表的总列数
	 * @param tableName
	 * @return
	 */
	public int getTableColumn(String tableName) {
		int colCount =  0;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from " + tableName;
		Connection connection = DbUtilsScoreTab.getConnection();
		
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			colCount = metaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return colCount;
	}
	
	/**
	 * 获取graduation_scoretab数据库的所有表名
	 * @return:返回表名的list
	 */
	public List<String> getTableList() {
		List<String> tables = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "show tables";
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			int count = 0;
			while(resultSet.next()) {
				count++;
				tables.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	public static void main(String[] args) {
		DBDao dao = new DBDao();
		List<String> tables = dao.getTableList();
		for (String table: tables) {
			System.out.println(table);
		}
//		dao.createTable(4, "table1");
//		List<String[]> list = dao.getScoreData("table1");
//		for (String[] record: list) {
//			for (int i = 0; i < record.length; i++) {
//				System.out.print(record[i]);
//				if (i == record.length - 1) {
//					System.out.println();
//				}
//			}
//		}
//		System.out.println(dao.getTableColumn("table1"));
	}
	
}

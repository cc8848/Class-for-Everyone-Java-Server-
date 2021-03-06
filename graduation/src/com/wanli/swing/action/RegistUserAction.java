package com.wanli.swing.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;

import com.aliyuncs.exceptions.ClientException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.wanli.swing.entities.UserBean;
import com.wanli.swing.service.RegistService;
import com.wanli.utils.MailUtils;
import com.wanli.utils.Randomutil;
import com.wanli.utils.SmsUtils;

/**
 * 注册账号和找回密码的Action
 * @author wanli
 *
 */
public class RegistUserAction extends ActionSupport implements RequestAware, ModelDriven<UserBean>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//注入RegisterService
	private RegistService registService;
	//模型驱动使用的对象
	private UserBean bean;

	public void setRegistService(RegistService registService) {
		this.registService = registService;
	}
	
	public void prepareAddUser() {
		bean = new UserBean();
	}

	@Override
	public UserBean getModel() {
		bean = new UserBean();
		return bean;
	}

	private Map<String, Object> request;
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	@Override
	public void prepare() throws Exception {}
	
	//注册用户执行的方法
	public String addUser() {
		
		registService.addUser(bean);
		return "adduser";
	}
	//ajax进行异步校验用户名是否存在执行的方法
	public String findByName() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		//调用registerService进行查询
		UserBean existUser = registService.getByUsername(username);
		//获得response对象，向页面输出
		HttpServletResponse response = ServletActionContext.getResponse();
		//设置字符集编码，可向页面输出中文
		response.setContentType("text/html;charset=UTF-8");
		//判断
		if (existUser != null) {
			//查询到该用户：用户名已经存在
			response.getWriter().println("<font color='red'>该手机号已经注册，可直接登录</font>");
		} else {
			//没有查询到该用户：用户名可以使用
			response.getWriter().println("<font color='green'>手机号可以使用</font>");
		}
		return NONE;
	}
	
	//ajax进行异步校验昵称是否存在执行的方法
	public String findByNickname() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String nickname = new String(request.getParameter("nickname").getBytes("iso8859-1"),"UTF-8");
		System.out.println(nickname);
		//调用registerService进行查询
		UserBean existUser = registService.getByNickname(nickname);
		//获得response对象，向页面输出
		HttpServletResponse response = ServletActionContext.getResponse();
		//设置字符集编码，可向页面输出中文
		response.setContentType("text/html;charset=UTF-8");
		//判断
		if (existUser != null) {
			//查询到该用户：用户名已经存在
			response.getWriter().println("<font color='red'>昵称已经存在</font>");
		} else {
			//没有查询到该用户：用户名可以使用
			response.getWriter().println("<font color='green'>昵称可以使用</font>");
		}
		return NONE;
	}
	
	//ajax进行异步校验邮箱是否存在执行的方法
	public String findByEmail() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String email = request.getParameter("email");
		//调用registerService进行查询
		UserBean existUser = registService.getByEmail(email);
		//获得response对象，向页面输出
		HttpServletResponse response = ServletActionContext.getResponse();
		//设置字符集编码，可向页面输出中文
		response.setContentType("text/html;charset=UTF-8");
		//判断
		if (existUser != null) {
			//查询到该用户：用户名已经存在
			response.getWriter().println("<font color='red'>邮箱已经存在</font>");
		} else {
			//没有查询到该用户：用户名可以使用
			response.getWriter().println("<font color='green'>邮箱可以使用</font>");
		}
		return NONE;
	}
	
	//发送邮件获取验证码
	public String getVeriCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String toAddr = request.getParameter("email");
		String randomNum = Randomutil.getRandom();
//		MailUtils.sendMail(toAddr, randomNum);
		try {
			response.getWriter().println(randomNum);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("返回数据失败");
		}
		return NONE;
	}
	
	//发送短信验证码
	public String getSmsVeriCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String username = request.getParameter("username");
		String randomNum = Randomutil.getRandom();
//		try {
//			SmsUtils.sendSms(username, randomNum);
//		} catch (ClientException e) {
//			e.printStackTrace();
//			System.out.println("短信发送失败");
//		}
		try {
			response.getWriter().println(randomNum);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("返回数据失败");
		}
		return NONE;
	}
	
	//重置密码
	public String getBackPassword() {
		return "getBackPassword";
	}
	
	//密码重置成功
	public String resetPasswordSucc() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//parameter表示通过哪种方式修改密码，包括手机号和邮箱两种方式
		String parameter = request.getParameter("parameter");
		String password = request.getParameter("password");
		registService.updatePassword(parameter, password);
		return "resetPasswordSucc";
	}
}

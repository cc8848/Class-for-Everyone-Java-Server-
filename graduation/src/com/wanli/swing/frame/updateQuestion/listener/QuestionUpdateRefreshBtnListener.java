package com.wanli.swing.frame.updateQuestion.listener;

import java.io.File;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.wanli.swing.frame.listener.OpenFileDialogBtnListener;
import com.wanli.utils.StaticVariable;
import com.wanli.utils.XmlToJavaBean;

public class QuestionUpdateRefreshBtnListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
//		if (StaticVariable.questionsList.size() > 0) {
		// ��ָ�����ļ�
		File file = new File(StaticVariable.questionManagerFileName);
		// ��xml�ļ�תΪJavaBean
		new XmlToJavaBean(file);
		OpenFileDialogBtnListener.fillTable();			
//		}
	}
}

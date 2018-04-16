package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;

import com.wanli.utils.StaticVariable;

public class QuestionSelectComboListener implements SelectionListener {

	private int option = 65;							// ���ѡ��
	private String question;							// ��ȡ��Ŀ
	private String[] strs;								// ����ȡ����Ŀ���зָ�
	private StringBuffer showQues = new StringBuffer();	// ��ʾ��Ŀ
	private StyleRange style;
	
	public QuestionSelectComboListener() {
		// ����һ��style����
		style = new StyleRange();
		// �ӵ�һ���ַ���ʼ
		style.start = 0;
		// ����ѡ����:�����Ƿ���:���������:������»��߲��ҼӴ�
		style.length = 4;
		style.underline = true;
		style.font = new Font(StaticVariable.parent.getDisplay(), "Arial", 30, SWT.BOLD);
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
//		System.out.println(StaticVariable.questionSelect.getSelectionIndex());
//		System.out.println(StaticVariable.questionSelect.getText());
		// ��ȡ��ǰѡ���е��±�
		int index = StaticVariable.questionSelect.getSelectionIndex();
		// ��ȡ��ǰѡ���е��ı�
		String selected = StaticVariable.questionSelect.getText();
		if (index != 0) {
			// ��ȡ��Ŀ����
			String type = selected.substring(selected.indexOf("-") + 1);
			switch (type) {
			case "ѡ����":
				// ���StringBuffer
				showQues.setLength(0);
				// ��ȡ��Ŀ
				question = StaticVariable.questionsMap.get(Integer.toString(index));
				// �ָ���Ŀ
				strs = question.split(",");
				// ������Ŀ
				showQues.append("ѡ����:\n\n");
				showQues.append(strs[2]);
				for (int i = 4; i < strs.length; i++) {
					showQues.append("\n" + (char)option + "." + strs[i]);
					option++;
				}
				option = 65;
				// ��ʾ��Ŀ
				StaticVariable.text.setText(showQues.toString());
				// ������ʽ
				StaticVariable.text.setStyleRange(style);
				break;

			case "�Ƿ���":
				showQues.setLength(0);
				question = StaticVariable.questionsMap.get(Integer.toString(index));
				strs = question.split(",");
				showQues.append("�Ƿ���:\n\n");
				showQues.append(strs[2]);
				StaticVariable.text.setText(showQues.toString());
				StaticVariable.text.setStyleRange(style);
				break;
			case "�����":
				showQues.setLength(0);
				question = StaticVariable.questionsMap.get(Integer.toString(index));
				strs = question.split(",");
				showQues.append("�����:\n\n");
				showQues.append(strs[2]);
				StaticVariable.text.setText(showQues.toString());
				StaticVariable.text.setStyleRange(style);
				break;
			}
		}
	}

}

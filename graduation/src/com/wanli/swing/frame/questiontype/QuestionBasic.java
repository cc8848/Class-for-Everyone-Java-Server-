package com.wanli.swing.frame.questiontype;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.wanli.swing.frame.listener.NextOptionListener;
import com.wanli.utils.StaticVariable;

public class QuestionBasic {

	private Composite parent;
	private Composite child;
	
	public QuestionBasic(Composite parent, Composite child) {
		this.parent = parent;
		this.child = child;
		StaticVariable.nextOption = addComposite();
	}
	
	public Button addComposite() {
		child.setLayout(new GridLayout(2, false));
		GridData choiceGrid = new GridData(GridData.FILL_BOTH);
		choiceGrid.horizontalSpan = 2;
		child.setLayoutData(choiceGrid);
		
		// ��Ŀ
		Label questionLabel = new Label(child, SWT.NONE);
		questionLabel.setText("��Ŀ��");
		Text questionText = new Text(child, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		// ����questionTextΪ����ʽ���֣���ˮƽռ����
		GridData questionGrid = new GridData(GridData.FILL_BOTH);
		questionGrid.horizontalSpan = 2;
		questionText.setLayoutData(questionGrid);
		
		// ����ѡ��𰸵Ĳ���
		GridData optionGrid = new GridData(GridData.FILL_HORIZONTAL);

		// ��
		Label answerLabel = new Label(child, SWT.NONE);
		answerLabel.setText("�𰸣�");
		Text answerText = new Text(child, SWT.BORDER);
		answerText.setLayoutData(optionGrid);
		
		if (child instanceof TrueOrFalseComposite) {
			StaticVariable.trueOrFalseAllText.put("question", questionText);
			StaticVariable.trueOrFalseAllText.put("answer", answerText);
		}
		if (child instanceof FillInTheBlanksComposite) {
			StaticVariable.fillblanksAllText.put("question", questionText);
			StaticVariable.fillblanksAllText.put("answer", answerText);
		}
		
		// ��һ��
		Button nextOption = new Button(parent, SWT.NONE);
		nextOption.setText("��һ��");
		GridData nextGrid = new GridData(GridData.FILL_HORIZONTAL);
		nextGrid.horizontalSpan = 2;
		nextOption.setLayoutData(nextGrid);
		nextOption.addSelectionListener(new NextOptionListener());
		
		return nextOption;
	}
	
}

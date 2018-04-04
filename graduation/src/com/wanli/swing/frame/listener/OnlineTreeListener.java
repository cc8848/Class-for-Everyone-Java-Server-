package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.swing.frame.MainFrame;

/**
 * Ϊtree�ؼ��������¼�
 * @author wanli
 *
 */
public class OnlineTreeListener extends MouseAdapter{
	
	private Tree tree;				// �洢��ǰtree����
	private Composite parent;		// �洢��ǰ�����������Ϣ�Ķ���
	private Menu menu = null;		// �Ҽ��˵�
	
	public OnlineTreeListener(Tree tree, Composite parent) {
		this.tree = tree;
		this.parent = parent;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent event) {
		
	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		treeMouseDown(event);
	}
	
	protected void treeMouseDown(MouseEvent event) {
		// ����Ҽ��˵����������հ״�Ҳ�ᵯ���Ҽ��˵�
		if (menu != null) {
			menu.dispose();
		}
		TreeItem selected = tree.getItem(new Point(event.x, event.y));
		// ���ȡ���ڵ�ؼ�����������Ҽ�
		if (selected != null && event.button == 3) {
			if (selected.getParentItem() != null) {// ������Ǹ��ڵ�
				menu = new Menu(tree);// Ϊ�ڵ㽨POP UP�˵�
		        MenuItem newItem = new MenuItem(menu, SWT.PUSH);
		        newItem.setText("���ͼ�ʱ��Ϣ");
		        MenuItem newMemberItem = new MenuItem(menu, SWT.PUSH);
		        newMemberItem.setText("�鿴����");
		        MenuItem editItem = new MenuItem(menu, SWT.PUSH);
		        editItem.setText("�༭");
		        MenuItem deleteItem = new MenuItem(menu, SWT.PUSH);
		        deleteItem.setText("ɾ��");
		        tree.setMenu(menu);
			} else if(selected.getParentItem() == null) {// �Ǹ��ڵ�
				menu = new Menu(tree);// Ϊ�ڵ㽨POP UP�˵�
				MenuItem rename = new MenuItem(menu, SWT.PUSH);
				rename.setText("������");
				rename.addSelectionListener(new SelectionAdapter() {
					@Override
		        	public void widgetSelected(SelectionEvent e) {
						// ����һ���ɱ༭��TreeEditor����
		        		final TreeEditor editor = new TreeEditor(tree);
		        		editor.horizontalAlignment = SWT.LEFT;
		        		editor.grabHorizontal = true;
		        		editor.minimumWidth = 30;
		        		// �ͷ�֮ǰ�༭�Ŀؼ�
		        		Control oldEditor = editor.getEditor();
		        		if (oldEditor != null) {
		        			oldEditor.dispose();
		        		}
		        		// ����һ���ı�����Ϊ�༭�ڵ�ʱ���������
		        		Text newEditor = new Text(tree, SWT.NONE);
		        		// �����ڵ��ֵ��ֵ���ı���
		        		newEditor.setText(selected.getText());
		        		// ���ı����ֵ�ı�ʱ��Ҳ��Ӧ�İ�ֵ��ֵ�����ڵ�
		        		newEditor.addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent arg0) {
								Text text = (Text) editor.getEditor();
								editor.getItem().setText(text.getText());
							}
						});
		        		newEditor.selectAll();		// ѡ�������ı���
		        		newEditor.setFocus();		// ������������Ϊ���ı���
		        		// �����ڵ����ı���ڵ��
		        		editor.setEditor(newEditor, selected);
		        		// Ϊ�ı�����Ӽ����¼�
		        		newEditor.addListener(SWT.KeyDown, new Listener() {
							
							@Override
							public void handleEvent(Event event) {
								// ������Enter��ʱ�����ı����ֵ��ֵ�����ڵ㣬����ʹ�ı���ʧЧ
								if (event.keyCode == SWT.CR) {
									selected.setText(newEditor.getText());
									newEditor.dispose();
								}
							}
						});
		        	}
				});
		        MenuItem deleteClass = new MenuItem(menu, SWT.PUSH);
		        deleteClass.setText("ɾ������");
		        deleteClass.addSelectionListener(new SelectionAdapter() {
		        	
		        	@Override
		        	public void widgetSelected(SelectionEvent e) {
		        		MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO);
		        		messageBox.setText("ɾ������");
		        		messageBox.setMessage("ȷ��Ҫɾ�����������");
		        		if (messageBox.open() == SWT.YES) {
		        			MainFrame.rooms.remove(selected);
		        			System.out.println(MainFrame.rooms.size());
			        		selected.dispose();
		        		}
		        	}
		        	
		        });
		        tree.setMenu(menu);
			}
		}
		
	}
	
}

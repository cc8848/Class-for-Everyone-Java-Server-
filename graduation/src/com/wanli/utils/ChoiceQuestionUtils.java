package com.wanli.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ѡ���⹤��
 * @author wanli
 *
 */
public class ChoiceQuestionUtils {

	private int optionCount;
	private Map<String, Integer> options;
	private int ascoll = 65; 						// ������ĸ��ascoll�룬Ĭ��Ϊ��ĸE��ascoll��
	
	public ChoiceQuestionUtils(int optionCount) {
		this.optionCount = optionCount;
		options = new HashMap<>();
	}
	
	public void createMap() {
		for (int i = 0; i < optionCount; i++) {
			options.put((char)ascoll + "", new Integer(0));
			ascoll++;
		}
		ascoll = 65;
	}
	
	public Map<String, Integer> getOptions() {
		return options;
	}

	private void systemout() {
		System.out.println(options);
	}
	
	public static void main(String[] args) {
		ChoiceQuestionUtils utils = new ChoiceQuestionUtils(5);
		utils.createMap();
		utils.systemout();
	}
	
}

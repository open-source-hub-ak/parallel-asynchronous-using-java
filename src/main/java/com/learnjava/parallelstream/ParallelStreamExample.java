package com.learnjava.parallelstream;

import static com.learnjava.util.CommonUtil.delay;

import java.util.List;
import java.util.stream.Collectors;

import com.learnjava.util.CommonUtil;
import com.learnjava.util.DataSet;
import com.learnjava.util.LoggerUtil;

public class ParallelStreamExample {

	public List<String> stringTransform(List<String> namesList) {
		return namesList.parallelStream().map(ParallelStreamExample::addNameLengthTransform)
				.collect(Collectors.toList());

	}

	public static void main(String[] args) {

		List<String> namesList = DataSet.namesList();

		CommonUtil.startTimer();
		ParallelStreamExample example = new ParallelStreamExample();
		List<String> rs = example.stringTransform(namesList);
		LoggerUtil.log(" Result - " + rs);
		CommonUtil.timeTaken();
	}

	private static String addNameLengthTransform(String name) {
		delay(500);
		return name.length() + " - " + name;
	}

}

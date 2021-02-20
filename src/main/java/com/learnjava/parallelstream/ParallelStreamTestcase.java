package com.learnjava.parallelstream;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.learnjava.util.CommonUtil;
import com.learnjava.util.DataSet;

class ParallelStreamTestcase {
	
	ParallelStreamExample parallelStreamExample = new ParallelStreamExample();

	@Test
	void stringTransform() {
		
		//given
		List<String> namesList = DataSet.namesList();
		
		//when
		CommonUtil.startTimer();
		List<String> resultList = parallelStreamExample.stringTransform(namesList);
		CommonUtil.timeTaken();
		
		//then
		assertEquals(4, resultList.size());
		resultList.forEach(name->{
			assertTrue(name.contains("-"));
		});
		
		
	}

}

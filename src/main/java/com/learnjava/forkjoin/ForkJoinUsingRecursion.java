package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

	private List<String> inputList;

	public ForkJoinUsingRecursion(List<String> inputList) {
		this.inputList = inputList;
	}

	public static void main(String[] args) {

		stopWatch.start();
		List<String> resultList = new ArrayList<>();
		List<String> names = DataSet.namesList();
		log("names : " + names);

		ForkJoinPool forkJoinPool = new ForkJoinPool();
		ForkJoinUsingRecursion forkJoinUsingRecursion = new ForkJoinUsingRecursion(names); // this is forkjoin task..
		// this invoke is where forkjoin task is added in shared Queue.
		// and then all the worker thread poll the sshared Queue and one of thread will
		// add it in shared WorkQueue
		//
		resultList = forkJoinPool.invoke(forkJoinUsingRecursion);

		/*
		 * names.forEach((name) -> { String newValue = addNameLengthTransform(name);
		 * resultList.add(newValue); });
		 */
		stopWatch.stop();
		log("Final Result : " + resultList);
		log("Total Time Taken : " + stopWatch.getTime());
	}

	private static String addNameLengthTransform(String name) {
		delay(500);
		return name.length() + " - " + name;
	}

	@Override
	protected List<String> compute() {

		// base condition
		if (inputList.size() <= 1) {
			List<String> resultList = new ArrayList<>();
			inputList.forEach(name -> resultList.add(addNameLengthTransform(name)));
			return resultList;
		}

		// This is where actual work happens
		// all the fork join operation is going to happen here

		// split list in to half
		int midpoint = inputList.size() / 2;

		// leftside of input list
		ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(inputList.subList(0, midpoint)).fork();

		// update the inputlit with other remaining side
		inputList = inputList.subList(midpoint, inputList.size());

		// recursion call
		List<String> rightResult = compute();

		List<String> leftresult = leftInputList.join();

		leftresult.addAll(rightResult);

		return leftresult;
	}
}

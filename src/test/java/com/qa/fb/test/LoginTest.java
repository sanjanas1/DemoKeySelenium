package com.qa.fb.test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qa.fb.KeywordExec.Execution;

@Listeners(CustomListener.class)
public class LoginTest {
	Execution exec;
	@Test
	public void logintest() throws IOException, InterruptedException

	{
		exec= new Execution();
		exec.Test_scenario_Execution("Sheet1");
		
	}

}

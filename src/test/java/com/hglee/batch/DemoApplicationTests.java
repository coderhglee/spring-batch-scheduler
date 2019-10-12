package com.hglee.batch;

import com.hglee.batch.service.AsyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private JobLauncher jobLauncher;


	@Autowired
	private Job job;

	@Autowired
	AsyncService asyncService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testLaunchJob() throws Exception {
		JobExecution jobExecution=null;
		try{

			JobParameters param =
					new JobParametersBuilder()
							.addLong("runTime", System.currentTimeMillis()).toJobParameters();

			jobExecution = jobLauncher.run(job, param);
			//Async 로 수행되는 경우 Exit Status는 UNKNOWN으로 설정 됨
			System.out.println(jobExecution.getExitStatus().getExitCode());
//			assertEquals("UNKNOWN", jobExecution.getExitStatus().getExitCode());
			Thread.sleep(4000);

		}catch (InterruptedException ie){
			ie.printStackTrace();
		}
//		asyncService.findUser("hglee",);
//		assertTrue(personService.getReturnedCount() > 0);
//		assertEquals(personService.getReturnedCount(), personService.getReceivedCount()) ;
		assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}

}

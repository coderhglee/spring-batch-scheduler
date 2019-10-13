package com.hglee.batch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.hglee.batch.domain.News;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void contextLoads() throws Exception{

//		String url = String.format("https://api.github.com/users/%s", "coderhglee");
		String url = String.format("https://chosun-robonews.dunamu.com/v2/news/json?limit=%s", "100");
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		System.out.println(response.toString());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(response.getBody());

		JsonNode news = root.path("res").path("channel").path("news");
		ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<News>>() {});

		List<News> newsList = objectReader.readValue(news);

		System.out.println(newsList.size());

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

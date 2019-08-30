package com.pb.payhub.citidemo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pb.payhub.citidemo.workflow.RatesObtainingWorkflow;

@SpringBootApplication
public class CitiDemoApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger("com.pb.payhub.citidemo");
	
	@Autowired
	private RatesObtainingWorkflow ratesWorkflow;

	public static void main(String[] args) {
		SpringApplication.run(CitiDemoApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
    	List<String> arguments = Arrays.asList(args);
    	logger.info("Starting CitiDemoApplication with parameters: {}", arguments);
    	
    	ratesWorkflow.flow();
    	
    }
}

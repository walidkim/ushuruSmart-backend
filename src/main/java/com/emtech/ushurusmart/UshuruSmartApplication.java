package com.emtech.ushurusmart;

import com.emtech.ushurusmart.config.LoggerSingleton;
import com.emtech.ushurusmart.etims.service.SampleDataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class  UshuruSmartApplication  extends LoggerSingleton {
	public static  void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UshuruSmartApplication.class, args);

		// Now context is properly initialized, and you can use it to retrieve beans
		SampleDataInitializer sampleDataInitializer = context.getBean(SampleDataInitializer.class);
		sampleDataInitializer.initSampleData();
	}

}

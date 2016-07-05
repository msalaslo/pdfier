package com.pdfa.msl.webaj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pdfa.msl.webaj.configuration.WebSecurityConfigurationApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebSecurityConfigurationApplication.class)
@WebAppConfiguration
public class PdfaWebajApplicationTests {

	@Test
	public void contextLoads() {
	}

}

package com.msl.pdfier.pdfua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.msl.pdfier.pdfua.gen.config.HtmlToPdfConfiguration;

@SpringBootApplication
@ComponentScan ("com")
public class FlyingSaucerPdfUaApplication implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(FlyingSaucerPdfUaApplication.class);

	@Autowired
	HtmlToPdfConfiguration htmlToPdfGenConfiguration;
	
    public static void main(String[] args) {
        SpringApplication.run(FlyingSaucerPdfUaApplication.class, args);
    }
	
    @Override
	public void run(String... args) {
    	//IMPORTANT: DO NOT Remove the use of the configuration here, should be used from the app module to load properties properly
		logger.info("htmlToPdfGenConfiguration.isAddLocalFonts():" + htmlToPdfGenConfiguration.isAddLocalFonts());
		logger.info("htmlToPdfGenConfiguration.isSanitizePdf():" + htmlToPdfGenConfiguration.isSanitizePdf());
		logger.info("htmlToPdfGenConfiguration.isSaveGeneratedPdf():" + htmlToPdfGenConfiguration.isSaveGeneratedPdf());
	}

}

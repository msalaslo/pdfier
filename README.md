# The PDFIER Project

## About

Multimodule project to generate PDF from HTML.
Basicaly it receives a URL (or a HTML) and transfor the HTML to PDF
It is based on several PDF generation libraries: IText, Flying Saucer and openhtmltopdf
Technically is based in SpringBoot, the application is structured in a multimodule maven project
Three flavors: 
- Flying Saucer with OpenPDF
- Flying Saucer custom to support PDF/UA
- openhtmltopdf

Divided in three kinds od projects:
- app: Rest API to invoke the application, one for each flavor
- gen: PDF generation, one for each flavor
- commons: commons classes

## Technical Stack:

- Java 1.8+
- Maven 3.6+
- Spring boot 2.3.3.RELEASE


Test on the browser via Rest API
-------------------

```sh
http://localhost:8080/pdfier-app/saveaspdfua?url=https://webaim.org/training/
```

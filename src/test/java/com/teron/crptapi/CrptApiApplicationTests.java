package com.teron.crptapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CrptApiApplicationTests {

	@Test
	void contextLoads() {

		int threadCnt = 20;
		TimeUnit timeUnit = TimeUnit.SECONDS;
		int threadAccessCnt = 5;

		CrptApi crptApi = new CrptApi(timeUnit, threadAccessCnt);
		CrptApi.CrptDocument crptDocument = createDocument(crptApi);




		ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
		for (int i = 0; i < 1; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						crptApi.createDocument(crptDocument, "");
					} catch (IOException e) {
						throw new RuntimeException(e);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					} catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
			});
			if (i % 7 == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { }
			}
		}
		System.out.println("Here: " + Thread.currentThread().getId());
		executorService.shutdown();
	}

	private CrptApi.CrptDocument createDocument(CrptApi crptApi) {
		CrptApi.CrptDocument crptDocument = crptApi.new CrptDocument();
		CrptApi.Description description = crptApi.new Description("string");
		CrptApi.Product product = crptApi.new Product();

		crptDocument.setDescription(description);
		crptDocument.setDocId("string");
		crptDocument.setDocStatus("string");
		crptDocument.setDocType("LP_INTRODUCE_GOODS");
		crptDocument.setImportRequest(true);
		crptDocument.setOwnerInn("string");
		crptDocument.setParticipantInn("string");
		crptDocument.setProductionDate(LocalDateTime.of(2020, 1, 23, 00, 00));
		crptDocument.setProductionType("string");
		crptDocument.setRegDate(LocalDateTime.of(2020, 1, 23, 00, 00));
		crptDocument.setRegNumber("string");

		product.setCertificateDocument("string");
		product.setCertificateDocumentDate(LocalDateTime.of(2020, 1, 23, 00, 00));
		product.setCertificateDocumentNumber("string");
		product.setOwnerInn("string");
		product.setProducerInn("string");
		product.setProductionDate(LocalDateTime.of(2020, 1, 23, 00, 00));
		product.setTnvedCode("string");
		product.setUitCode("string");
		product.setUituCode("string");

		List<CrptApi.Product> products = new ArrayList<>();
		products.add(product);

		crptDocument.setProducts(products);

		return crptDocument;
	}

}

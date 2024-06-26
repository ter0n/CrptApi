package com.teron.crptapi;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private String postRequestUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";

    private final int requestLimit;

    private final TimeUnit timeUnit;

    private final long timeInNanos;

    private LinkedList<Long> accessionsTimeStack;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.timeInNanos = this.timeUnit.toNanos(1);
        accessionsTimeStack = new LinkedList<>();
    }

    public HttpResponse<String> createDocument(CrptDocument document, String sign) throws IOException, InterruptedException, URISyntaxException {
        if (!getAccess()) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String docJson = objectMapper.writeValueAsString(document);

        HttpRequest request  = HttpRequest.newBuilder()
                .uri(new URI(postRequestUrl))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(docJson))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private synchronized boolean getAccess() {
        long currentTime = System.nanoTime();

        if (accessionsTimeStack.size() < requestLimit) {
            accessionsTimeStack.addLast(currentTime);
            return true;
        } else {
            if (currentTime - accessionsTimeStack.peek() > timeInNanos) {
                accessionsTimeStack.pop();
                accessionsTimeStack.addLast(currentTime);
                return true;
            }
        }
        return false;
    }

    public String getPostRequestUrl() {
        return postRequestUrl;
    }

    public void setPostRequestUrl(String postRequestUrl) {
        this.postRequestUrl = postRequestUrl;
    }

    public class CrptDocument {

        @JsonProperty("description")
        private Description description;

        //        @JsonAlias("doc_id")
        @JsonProperty("doc_id")
        private String docId;

        @JsonProperty("doc_status")
        private String docStatus;

        @JsonProperty("doc_type")
        private String docType; //"LP_INTRODUCE_GOODS"

        @JsonProperty("importRequest")
        private boolean importRequest;

        @JsonProperty("owner_inn")
        private String ownerInn;

        @JsonProperty("participant_inn")
        private String participantInn;

        @JsonProperty("production_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime productionDate;

        @JsonProperty("production_type")
        private String productionType;

        @JsonProperty("products")
        private List<Product> products;

        @JsonProperty("reg_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime regDate;

        @JsonProperty("reg_number")
        private String regNumber;

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public LocalDateTime getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(LocalDateTime productionDate) {
            this.productionDate = productionDate;
        }

        public String getProductionType() {
            return productionType;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public LocalDateTime getRegDate() {
            return regDate;
        }

        public void setRegDate(LocalDateTime regDate) {
            this.regDate = regDate;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }
    }

    public class Description {
        @JsonProperty("participantInn")
        private String participantInn;

        public Description() {
        }

        public Description(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }

    public class Product {
        @JsonProperty("certificate_document")
        private String certificateDocument;

        @JsonProperty("certificate_document_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime certificateDocumentDate;

        @JsonProperty("certificate_document_number")
        private String certificateDocumentNumber;

        @JsonProperty("owner_inn")
        private String ownerInn;

        @JsonProperty("producer_inn")
        private String producerInn;

        @JsonProperty("production_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime productionDate;

        @JsonProperty("tnved_code")
        private String tnvedCode;

        @JsonProperty("uit_code")
        private String uitCode;

        @JsonProperty("uitu_code")
        private String uituCode;

        public String getCertificateDocument() {
            return certificateDocument;
        }

        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }

        public LocalDateTime getCertificateDocumentDate() {
            return certificateDocumentDate;
        }

        public void setCertificateDocumentDate(LocalDateTime certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }

        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }

        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public LocalDateTime getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(LocalDateTime productionDate) {
            this.productionDate = productionDate;
        }

        public String getTnvedCode() {
            return tnvedCode;
        }

        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }

        public String getUitCode() {
            return uitCode;
        }

        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }

        public String getUituCode() {
            return uituCode;
        }

        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }
    }

}

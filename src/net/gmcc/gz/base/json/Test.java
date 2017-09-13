package net.gmcc.gz.base.json;

public class Test {


//    public static void main(String[] args) {
//        JsonRequest jsonRequest = new JsonRequest();
//        RequestHeader requestHeader = new RequestHeader();
//        requestHeader.setChannel(new Channel("room2017", "123456"));
//        requestHeader.setEncode(new Encode(System.currentTimeMillis(), String.valueOf(System.currentTimeMillis())));
//
//        PageInfo pageInfo = new PageInfo(1, 15, 20, 100);
//        String requestContent = "{\"businessId\":\"147974623841\",\"businessType\":\"系统公告\",\"fileType\":\"澄清\",\"oriFilename\":\"文件名\"}";
//        Requestbody body1 = new Requestbody("101100001", new RequestElement(pageInfo, requestContent));
//
//        Requestbody body2 = new Requestbody("101100002", new RequestElement(pageInfo, requestContent));
//
//        List<Requestbody> bodyList = new ArrayList<>();
//        bodyList.add(body1);
//        bodyList.add(body2);
//
//        jsonRequest.setRequestbody(bodyList);
//        jsonRequest.setRequestHeader(requestHeader);
////        Announcement a1 = new Announcement();
////        a1.setId(11111L);
////        a1.setContents("公告内容");
//        System.out.println(JSON.toJSON(jsonRequest).toString());
//
////        System.out.println(JSON.parseObject("{\"requestBody\":[{\"pageInfo\":{\"page\":1,\"pageSize\":15,\"totalPages\":20,\"totalRecord\":100},\"requestElement\":\"{\\\"businessId\\\":\\\"147974623841\\\",\\\"businessType\\\":\\\"系统公告\\\",\\\"fileType\\\":\\\"澄清\\\",\\\"oriFilename\\\":\\\"文件名\\\"}\"},{\"pageInfo\":{\"page\":1,\"pageSize\":15,\"totalPages\":20,\"totalRecord\":100},\"requestElement\":\"{\\\"businessId\\\":\\\"147974623841\\\",\\\"businessType\\\":\\\"系统公告\\\",\\\"fileType\\\":\\\"澄清\\\",\\\"oriFilename\\\":\\\"文件名\\\"}\"}],\"requestHeader\":{\"channel\":{\"key\":\"room2017\",\"value\":\"123456\"},\"encode\":{\"decstr\":\"1504838675827\",\"timestamp\":1504838675827}}}\n",
////                JsonRequest.class));
//
//
//
//
//        JsonResponse jsonResponse = new JsonResponse();
//        jsonResponse.setResponseHeader(new ResponseHeader(new ResponseState("0", "success")));
//
//        String responseContent = "{\"businessId\":\"147974623841\",\"businessType\":\"系统公告\",\"fileType\":\"澄清\",\"oriFilename\":\"文件名\"}";
//        Responsebody body3 = new Responsebody("101100001", new ResponseElement(pageInfo, responseContent));
//
//        Responsebody body4 = new Responsebody("101100002", new ResponseElement(pageInfo, responseContent));
//
//        List<Responsebody> bodyList2 = new ArrayList<>();
//        bodyList2.add(body3);
//        bodyList2.add(body4);
//
//        jsonResponse.setResponseBody(bodyList2);
//        System.out.println(JSON.toJSON(jsonResponse));
//    }


    public static void main(String[] args) {
//        JsonRequest jsonRequest = new JsonRequest();
//        RequestHeader requestHeader = new RequestHeader("room2017", "123456", System.currentTimeMillis(), UUID.randomUUID().toString());
//        jsonRequest.setRequestHeader(requestHeader);
//        PageInfo pageInfo = new PageInfo(1, 15, 20, 100);
////        jsonRequest.setRequestbody(new Requestbody(pageInfo, JSON.parseObject("{\"businessId\":\"147974623841\",\"businessType\":\"系统公告\",\"fileType\":\"澄清\",\"oriFilename\":\"文件名\"}",
////        Attachment.class)));
////        Announcement a1 = new Announcement();
////        a1.setId(11111L);
////        a1.setContents("公告内容");
//        System.out.println(JSON.toJSON(jsonRequest).toString());

//        System.out.println(JSON.parseObject("{\"requestBody\":[{\"pageInfo\":{\"page\":1,\"pageSize\":15,\"totalPages\":20,\"totalRecord\":100},\"requestElement\":\"{\\\"businessId\\\":\\\"147974623841\\\",\\\"businessType\\\":\\\"系统公告\\\",\\\"fileType\\\":\\\"澄清\\\",\\\"oriFilename\\\":\\\"文件名\\\"}\"},{\"pageInfo\":{\"page\":1,\"pageSize\":15,\"totalPages\":20,\"totalRecord\":100},\"requestElement\":\"{\\\"businessId\\\":\\\"147974623841\\\",\\\"businessType\\\":\\\"系统公告\\\",\\\"fileType\\\":\\\"澄清\\\",\\\"oriFilename\\\":\\\"文件名\\\"}\"}],\"requestHeader\":{\"channel\":{\"key\":\"room2017\",\"value\":\"123456\"},\"encode\":{\"decstr\":\"1504838675827\",\"timestamp\":1504838675827}}}\n",
//                JsonRequest.class));




//        JsonResponse jsonResponse = new JsonResponse();
//        jsonResponse.setResponseHeader(new ResponseHeader(new ResponseState("0", "success")));
//
//        String responseContent = "{\"businessId\":\"147974623841\",\"businessType\":\"系统公告\",\"fileType\":\"澄清\",\"oriFilename\":\"文件名\"}";
//        Responsebody body3 = new Responsebody("101100001", new ResponseElement(pageInfo, responseContent));
//
//        Responsebody body4 = new Responsebody("101100002", new ResponseElement(pageInfo, responseContent));
//
//        List<Responsebody> bodyList2 = new ArrayList<>();
//        bodyList2.add(body3);
//        bodyList2.add(body4);
//
//        jsonResponse.setResponseBody(bodyList2);
//        System.out.println(JSON.toJSON(jsonResponse));
    }

}

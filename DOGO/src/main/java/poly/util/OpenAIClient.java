package poly.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class OpenAIClient {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "qwen/qwen2.5-vl-72b-instruct:free";

    public static String getAIResponse(String userMessage) throws Exception {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(15000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + API_KEY);

            org.json.JSONObject requestBody = new org.json.JSONObject();
            requestBody.put("model", MODEL);
            requestBody.put("temperature", 0.7);

            org.json.JSONArray messages = new org.json.JSONArray();
            messages.put(new org.json.JSONObject().put("role", "user").put("content", userMessage));
            requestBody.put("messages", messages);

            httpPost.setEntity(new StringEntity(requestBody.toString(), "UTF-8"));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                org.json.JSONObject jsonResponse = new org.json.JSONObject(responseString);
                if (jsonResponse.has("choices")) {
                    return jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                } else if (jsonResponse.has("error")) {
                    throw new Exception("OpenRouter trả về lỗi: " + jsonResponse.getJSONObject("error").getString("message"));
                } else {
                    throw new Exception("Phản hồi không hợp lệ từ OpenRouter: " + responseString);
                }
            }
        } catch (Exception e) {
            // Sửa lại để trả về lỗi chi tiết
            throw new Exception("Không thể kết nối AI. Chi tiết: " + e.getMessage(), e);
        }
    }
    
    // Phương thức streaming response
    public static void getAIResponseStream(String userMessage, Consumer<String> onChunk, Consumer<String> onComplete, Consumer<String> onError) {
        new Thread(() -> {
            try {
                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(15000)
                        .build();

                try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build()) {
                    HttpPost httpPost = new HttpPost(API_URL);
                    httpPost.setHeader("Content-Type", "application/json");
                    httpPost.setHeader("Authorization", "Bearer " + API_KEY);

                    org.json.JSONObject requestBody = new org.json.JSONObject();
                    requestBody.put("model", MODEL);
                    requestBody.put("temperature", 0.7);
                    requestBody.put("stream", true);

                    org.json.JSONArray messages = new org.json.JSONArray();
                    messages.put(new org.json.JSONObject().put("role", "user").put("content", userMessage));
                    requestBody.put("messages", messages);

                    httpPost.setEntity(new StringEntity(requestBody.toString(), "UTF-8"));

                    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line;
                        StringBuilder fullResponse = new StringBuilder();
                        
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String data = line.substring(6);
                                if (data.equals("[DONE]")) {
                                    break;
                                }
                                
                                try {
                                    org.json.JSONObject jsonData = new org.json.JSONObject(data);
                                    if (jsonData.has("choices") && jsonData.getJSONArray("choices").length() > 0) {
                                        org.json.JSONObject choice = jsonData.getJSONArray("choices").getJSONObject(0);
                                        if (choice.has("delta") && choice.getJSONObject("delta").has("content")) {
                                            String content = choice.getJSONObject("delta").getString("content");
                                            fullResponse.append(content);
                                            onChunk.accept(content);
                                        }
                                    }
                                } catch (Exception e) {
                                    // Bỏ qua lỗi parse JSON
                                }
                            }
                        }
                        
                        onComplete.accept(fullResponse.toString());
                    }
                }
            } catch (Exception e) {
                onError.accept("Không thể kết nối AI. Chi tiết: " + e.getMessage());
            }
        }).start();
    }
    
    // Phương thức mới để hỏi AI về thông tin sản phẩm (không có ảnh)
    public static String getProductInfoAI(String productName, String productDescription, String productPrice, String productSize, String productStock) throws Exception {
        String prompt = String.format(
            "Bạn là một chuyên gia tư vấn nội thất. Hãy phân tích và đưa ra lời khuyên về sản phẩm sau:\n" +
            "Tên sản phẩm: %s\n" +
            "Mô tả: %s\n" +
            "Giá: %s\n" +
            "Kích thước: %s\n" +
            "Tồn kho: %s\n\n" +
            "Hãy đưa ra:\n" +
            "1. Phân tích ưu điểm của sản phẩm\n" +
            "2. Gợi ý cách sử dụng và bố trí\n" +
            "3. So sánh giá trị với giá tiền\n" +
            "4. Lời khuyên cho khách hàng\n" +
            "Trả lời bằng tiếng Việt, ngắn gọn và hữu ích.",
            productName != null ? productName : "Không có thông tin",
            productDescription != null ? productDescription : "Không có mô tả",
            productPrice != null ? productPrice + " VNĐ" : "Không có giá",
            productSize != null ? productSize : "Không có thông tin",
            productStock != null ? productStock + " sản phẩm" : "Không có thông tin"
        );
        
        return getAIResponse(prompt);
    }
    
    // Phương thức streaming cho sản phẩm (không có ảnh)
    public static void getProductInfoAIStream(String productName, String productDescription, String productPrice, String productSize, String productStock, Consumer<String> onChunk, Consumer<String> onComplete, Consumer<String> onError) {
        String prompt = String.format(
            "Bạn là một chuyên gia tư vấn nội thất. Hãy phân tích và đưa ra lời khuyên về sản phẩm sau:\n" +
            "Tên sản phẩm: %s\n" +
            "Mô tả: %s\n" +
            "Giá: %s\n" +
            "Kích thước: %s\n" +
            "Tồn kho: %s\n\n" +
            "Hãy đưa ra:\n" +
            "1. Phân tích ưu điểm của sản phẩm\n" +
            "2. Gợi ý cách sử dụng và bố trí\n" +
            "3. So sánh giá trị với giá tiền\n" +
            "4. Lời khuyên cho khách hàng\n" +
            "Trả lời bằng tiếng Việt, ngắn gọn và hữu ích.",
            productName != null ? productName : "Không có thông tin",
            productDescription != null ? productDescription : "Không có mô tả",
            productPrice != null ? productPrice + " VNĐ" : "Không có giá",
            productSize != null ? productSize : "Không có thông tin",
            productStock != null ? productStock + " sản phẩm" : "Không có thông tin"
        );
        
        getAIResponseStream(prompt, onChunk, onComplete, onError);
    }
} 
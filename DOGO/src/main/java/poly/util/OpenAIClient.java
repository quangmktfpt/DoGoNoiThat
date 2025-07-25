package poly.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OpenAIClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

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
            requestBody.put("model", "gpt-3.5-turbo");
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
                    throw new Exception("OpenAI trả về lỗi: " + jsonResponse.getJSONObject("error").getString("message"));
                } else {
                    throw new Exception("Phản hồi không hợp lệ từ OpenAI: " + responseString);
                }
            }
        } catch (Exception e) {
            // Sửa lại để trả về lỗi chi tiết
            throw new Exception("Không thể kết nối AI. Chi tiết: " + e.getMessage(), e);
        }
    }
} 
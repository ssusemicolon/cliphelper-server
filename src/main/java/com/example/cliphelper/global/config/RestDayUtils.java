package com.example.cliphelper.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestDayUtils {
    @Value("${public-data.rest-day.encoding-service-key}")
    private String serviceKey;

    @Value("${public-data.rest-day.url}")
    private String requestUrl;

    public boolean isTodayRestDay() {
        LocalDate now = LocalDate.now();
        String todayDate = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        try {
            String response = requestRestDayListOfMonth(now.getYear(), now.getMonthValue());
            Map<String, Object> result = jsonToMap(response);

            List<HashMap<String, Object>> restDayList = getRestDayList(result);
            for (HashMap<String, Object> restDay : restDayList) {
                String restDayDate = (String) restDay.get("locdate");
                if (todayDate.equals(restDayDate)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("=====IOException 예외 발생=====");
            return true;
        } catch (NullPointerException e) {
            System.out.println("=====NullPointerException 예외 발생=====");
            return true;
        }

        return false;
    }

    private String requestRestDayListOfMonth(int year, int month) throws IOException {
        try {
            URL url = new URL(String.format("%s?serviceKey=%s&solYear=%s&solMonth=%s&_type=json",
                    requestUrl,
                    year,
                    String.format("%02d", month)));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String response;
            while ((response = rd.readLine()) != null) {
                sb.append(response);
            }
            rd.close();
            conn.disconnect();

            return response;
        } catch(IOException e) {
            System.out.println("=====공공데이터 포털의 공휴일 오픈 API 이용 중 예외 발생=====");
            e.printStackTrace();
            throw new IOException("=====공공데이터 포털의 공휴일 오픈 API 이용 중 예외 발생=====");
        }
    }

    private Map<String, Object> jsonToMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(json, Map.class);
            System.out.println(map);
        } catch (JsonProcessingException e) {
            System.out.println("=====json to Map 변환 시 예외 발생=====");
            e.printStackTrace();
        }

        return map;
    }

    private List<HashMap<String, Object>> getRestDayList(Map<String, Object> map) {
        Map<String, Object> response = (Map<String, Object>) map.get("response");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        int restDayCount = (int)body.get("totalCount");
        if (restDayCount == 0) {
            return null;
        }

        HashMap<String, Object> items = (HashMap<String, Object>) body.get("items");
        List<HashMap<String, Object>> item = (ArrayList<HashMap<String, Object>>) items.get("item");
        return item;
    }
}

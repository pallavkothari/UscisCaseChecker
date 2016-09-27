package com.pkothari.usciscasechecker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pkothari on 9/26/16.
 */
public class UscisCaseStatusChecker {
    private static final String APP_RECEIPT_NUM = "appReceiptNum";
    private static final String USCIS_CHECK_STATUS_URL = "https://egov.uscis.gov/casestatus/mycasestatus.do";
    private final OkHttpClient client = new OkHttpClient();

    public String check(String receiptNum) throws IOException {
        RequestBody form = new FormBody.Builder().add(APP_RECEIPT_NUM, receiptNum).build();
        Request request = new Request.Builder().url(USCIS_CHECK_STATUS_URL).post(form).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("unexpected response : " + response);
        String resp = response.body().string();
        return parse(resp);
    }

    private String parse(String html) {
        Document doc = Jsoup.parse(html);
        String currentStatus = doc.getElementsByTag("h1").first().text(); //doc.getElementsByClass("current-status-sec").text();
        String details = doc.getElementsByTag("p").first().text();
        return "Checked on " + new Date() + "\n\n " + currentStatus + "\n\n" +  details;
    }
}

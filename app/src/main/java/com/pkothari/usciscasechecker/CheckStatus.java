package com.pkothari.usciscasechecker;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckStatus extends AppCompatActivity {

    public static final String receipt = "MSC1690171684";
    private static ExecutorService networkThread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkStatus();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        checkStatus();
    }

    private void checkStatus() {
        try {
            networkThread.submit(new Runnable() {
                @Override
                public void run() {
                    TextView view = (TextView) findViewById(R.id.textview);
                    try {
                        view.setText(new UscisCaseStatusChecker().check(receipt));
                    } catch (IOException e) {
                        view.setText(e.getLocalizedMessage());
                    }
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

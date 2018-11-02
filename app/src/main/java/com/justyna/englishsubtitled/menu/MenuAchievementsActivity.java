package com.justyna.englishsubtitled.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Achievement;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenuAchievementsActivity extends AppCompatActivity {
    private TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_single_list);

        title = findViewById(R.id.title);
        title.setText(R.string.achievements);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            achievements = new AchievementsRetriever().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error while downloading an achievements list.");
        }

        // specify an adapter
        adapter = new AchievementsAdapter(achievements);
        recyclerView.setAdapter(adapter);
    }

    private static class AchievementsRetriever extends AsyncTask<Void, Void, List<Achievement>> {
        @Override
        protected List<Achievement> doInBackground(Void... voids) {
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Achievement>> achievements =
                    restTemplate.exchange(baseUrl + "/achievements",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<List<Achievement>>() {
                            });

            return achievements.getBody();
        }
    }
}
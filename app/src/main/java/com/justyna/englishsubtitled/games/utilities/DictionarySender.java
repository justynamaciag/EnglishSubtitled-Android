package com.justyna.englishsubtitled.games.utilities;

import android.os.AsyncTask;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.LessonsActivity;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class DictionarySender {


    public Boolean addToDict(Translation translation, LessonsActivity lessonsActivity){

        Boolean result;
        try {
            result = new AddDictionary(lessonsActivity).execute(translation).get();
        }catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            result = false;
        }
        return result;
    }

    private class AddDictionary extends AsyncTask<Translation, Void, Boolean>{

        private WeakReference<LessonsActivity> activityReference;

        AddDictionary(LessonsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Translation... translations) {
            if(translations.length < 1) return false;
            Translation translation = translations[0];
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(translation);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            ResponseEntity<String> result =
                    restTemplate.exchange(baseUrl + "/bookmarks/", HttpMethod.PUT, entity, String.class);

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(activityReference.get(), "Dodano do słownika", Toast.LENGTH_SHORT).show();
        }
    }

}
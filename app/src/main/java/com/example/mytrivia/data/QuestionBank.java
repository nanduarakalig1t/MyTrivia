package com.example.mytrivia.data;

import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mytrivia.controller.AppController;
import com.example.mytrivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.example.mytrivia.controller.AppController.TAG;

public class QuestionBank {
    private static final String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    ArrayList<Question> questionArrayList = new ArrayList<>();

    public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                Question question = new Question(
                                    response.getJSONArray(i).getString(0),
                                    response.getJSONArray(i).getBoolean(1));
                                questionArrayList.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (callback != null) {
                            callback.processFinished(questionArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })  ;
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }

}

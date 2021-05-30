package com.example.webdata;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void TextClick(View view){
        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        processDataText(result);
                    }
                });
    }

    private void processDataText(String data){
        try {
            JSONObject json = new JSONObject(data);
            JSONObject value = json.getJSONObject("value");
            String joke = value.getString("joke");

            TextView output = (TextView) findViewById(R.id.InsertText);
            output.setText(joke);
        } catch (JSONException e){
            Log.wtf("json", e);
        }
    }

    public void ImageClick(View view){
        Ion.with(this)
                .load("http://api.thecatapi.com/api/images/get?format=xml&results_per_page=3")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        ImageProcess(result);
                    }
                });
    }

    private void ImageProcess(String data){
        try {
            JSONObject json = XML.toJSONObject(data);
            JSONArray array = json.getJSONObject("response")
                    .getJSONObject("data")
                    .getJSONObject("images")
                    .getJSONArray("image");

            GridLayout grid = (GridLayout) findViewById(R.id.grid);
            grid.removeAllViews();

            for (int i = 0; i < array.length(); i++) {
                String url = array.getJSONObject(i).getString("url");
                Log.v("url", url);
                ImageView imgView = new ImageView(this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                imgView.setLayoutParams(params);
                Picasso.with(this).load(url).resize(500, 500).into(imgView);
                grid.addView(imgView);
            }
        } catch (JSONException e){
            Log.wtf("json", e);
        }
    }
}
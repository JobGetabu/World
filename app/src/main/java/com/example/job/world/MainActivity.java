package com.example.job.world;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.job.world.Adapter.CountryAdapter;
import com.example.job.world.model.Country;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.job.world.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    public static final String TAG = MainActivity.class.getSimpleName();
    private ListView lvJson;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        toolbar.setLogo(R.drawable.wold_icon);
        toolbar.setTitle("World");
        toolbar.setCollapsible(true);
        setSupportActionBar(toolbar);


        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
           .cacheOnDisk(true)
           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions)
           .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        recyclerView = (RecyclerView) findViewById(R.id.rv_json);

    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.referesh_menu:{
                new JsonTask().execute("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
                return true;
            }
            default:{
                 return super.onOptionsItemSelected(item);
            }
        }
    }

     public class JsonTask extends AsyncTask<String,String,List<Country>>{

        List<Country> countryList = new ArrayList<>();

        @Override
        protected List<Country> doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection connection =null;
            BufferedReader reader =null;


            StringBuffer mbuffer = new StringBuffer();

            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                if (url != null) {
                    connection = (HttpURLConnection) url.openConnection();
                }
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line="";
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentJson= new JSONObject(finalJson);
                JSONArray parentArray = parentJson.getJSONArray("worldpopulation");
                for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = (JSONObject) parentArray.get(i);
                Country countryObj = new Country();
                countryObj.setRank(finalObject.getInt("rank"));
                countryObj.setName(finalObject.getString("country"));
                countryObj.setPopulation(finalObject.getString("population"));
                countryObj.setFlag(finalObject.getString("flag"));

                countryList.add(countryObj);
                }
                Log.d(TAG, "doInBackground: collected data :"+countryList.toString());
                return countryList;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                connection.disconnect();
                }
                try {
                    if (reader != null) {
                    reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Country> results) {
            super.onPostExecute(results);

            if (results != null) {
                Log.d(TAG, "onPostExecute: results size is"+results.size());
                Log.d(TAG, "doInBackground: my json result"+countryList.toString());

                //TODO create a recycle view
                CountryAdapter myadapter = new CountryAdapter(getApplicationContext(), results);
                recyclerView.setAdapter(myadapter);
            } else {
                Toast.makeText(getApplicationContext(),"null list",Toast.LENGTH_SHORT).show();
            }

        }

    }

}

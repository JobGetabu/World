package com.example.job.world;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.job.world.model.Country;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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



    private ListView lvJson;
    public static final String TAG = MainActivity.class.getSimpleName();


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

        lvJson = (ListView) findViewById(R.id.lv_json);
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
                Myadapter myadapter = new Myadapter(getApplicationContext(),R.layout.country_item, results);
                lvJson.setAdapter(myadapter);
            } else {
                Toast.makeText(getApplicationContext(),"null list",Toast.LENGTH_SHORT).show();
            }

        }

    }
    public  class Myadapter extends ArrayAdapter{

        List<Country> returnCountryList;
        LayoutInflater inflater;
        int resource;

        public Myadapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Country> objects) {
            super(context, resource, objects);

            this.resource = resource;
            this.returnCountryList = objects;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ImageView countryFlag;
            TextView tvRank ;
            TextView tvCountry_name ;
            TextView tvPopulation ;
            final ProgressBar progressBar;

            if(convertView == null){
                convertView = inflater.inflate(resource, null);

            }
                countryFlag = (ImageView) convertView.findViewById(R.id.country_image);
                tvRank = (TextView) convertView.findViewById(R.id.rank);
                tvCountry_name = (TextView) convertView.findViewById(R.id.country_name);
                tvPopulation = (TextView) convertView.findViewById(R.id.population);
                progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);


            if (returnCountryList.get(position) != null) {


                Log.d(TAG, "getView: ranks is :"+returnCountryList.get(position).getRank());
                Log.d(TAG, "getView: name is :"+returnCountryList.get(position).getName());
                Log.d(TAG, "getView: population is :"+returnCountryList.get(position).getPopulation());
                //TODO set image up using Universal Image Loader Library
                // countryFlag.setImageDrawable(returnCountryList.get(position).setFlag();
                ImageLoader.getInstance().displayImage(returnCountryList.get(position).getFlag(), countryFlag, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setVisibility(view.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(view.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(view.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressBar.setVisibility(view.GONE);
                    }
                }); // Default options will be used


                tvRank.setText(String.valueOf(returnCountryList.get(position).getRank()));
                tvCountry_name.setText(returnCountryList.get(position).getName());
                tvPopulation.setText(returnCountryList.get(position).getPopulation());

            }
            return convertView;
        }
    }
}

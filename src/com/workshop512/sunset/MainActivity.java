package com.workshop512.sunset;

import it.octograve.jmetarparser.Cloud.CloudType;
import it.octograve.jmetarparser.Metar;
import it.octograve.jmetarparser.MetarParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.worshop512.sunset.R;

public class MainActivity extends Activity {

	private TextView tvOfficialSunsetTime;
	private TextView tvCivilSunsetTime;
	private TextView tvNauticalSunsetTime;
	private TextView tvAstroSunsetTime;
	private TextView tvETA;
	private TextView tvMetarString;
	
	private WebView webView1;
	

    String urlmetar = "http://weather.noaa.gov/pub/data/observations/metar/stations/";
    String urltaf = "http://weather.noaa.gov/pub/data/forecasts/taf/stations/";
	String airportcode = "KAUS";
	String str ;
	String metarStr;
	InputStream content=null;
	Metar mMetar;  //object for Metar info
	
	private Calendar officialSunset ;
	private Calendar astronomicalSunset;
	private Calendar nauticalSunset;
	private Calendar civilSunset;

	private SimpleDateFormat date_format1, date_format2;
	
    private LocationManager locationManager;
    private LocationListener listenerCoarse;
    private LocationListener listenerFine;
     
    // Holds the most up to date location.
    private Location currentLocation;
     
    // Set to false when location services are
    // unavailable.
    private boolean locationAvailable = true;
    
	//Things I'm intersted in -
	final String KEY_METAR = "METAR";
	final String KEY_LAT = "latitude";
	final String KEY_LON = "longitude";
	final String KEY_TC = "temp_c";
	final String KEY_DP = "dewpoint_c";
	final String KEY_VIZ = "visibility_statue_mi";
	final String KEY_ALTP = "altim_in_hg";
	final String KEY_SEALVP = "sea_level_pressure_mb";
	final String KEY_SKYCOND = "sky_condition";

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//register the location listener(s)
		registerLocationListeners();
		
		setContentView(R.layout.activity_main);
		
		//Get these fields so they can be used in the button handlers -
		tvETA = (TextView) findViewById(R.id.tvETA);
		tvOfficialSunsetTime = (TextView) findViewById(R.id.tvOfficialSunsetTime);
		tvCivilSunsetTime = (TextView) findViewById(R.id.tvCivilSunsetTime);
		tvNauticalSunsetTime = (TextView) findViewById(R.id.tvNauticalSunsetTime);
		tvAstroSunsetTime = (TextView) findViewById(R.id.tvAstroSunsetTime);
		tvMetarString = (TextView) findViewById(R.id.tvMetarString);
		
		webView1 = (WebView) findViewById(R.id.webView1);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
        // Make sure that when the activity has been suspended to background, 
        // the device starts getting locations again
        registerLocationListeners();
		
	    date_format1 = new SimpleDateFormat("h:mm:ss a z");
	    date_format2 = new SimpleDateFormat("h:mm a");

		Calendar calNow = Calendar.getInstance();
		
		//sunset calculator takes a special BigDecimal format location
		//com.luckycatlabs.sunrisesunset.dto.Location loc = new com.luckycatlabs.sunrisesunset.dto.Location("33.0", "-97.0");

		//SunriseSunsetCalculator sunsetCalculator = new SunriseSunsetCalculator(loc, calNow.getTimeZone().getDisplayName()) ;// "CST");


		if (locationAvailable) {

			SunriseSunsetCalculator sunsetCalculator = 
					new SunriseSunsetCalculator(
							new com.luckycatlabs.sunrisesunset.dto.Location(
									currentLocation.getLatitude(), 
									currentLocation.getLongitude()), 
									calNow.getTimeZone().getDisplayName()) ;


			//String officialSunriseForDate = sunsetCalculator.getOfficialSunriseForDate(calendar);

			//calSunset = sunsetCalculator.getOfficialSunsetCalendarForDate(calNow);
			officialSunset = sunsetCalculator.getOfficialSunsetCalendarForDate(calNow);
			astronomicalSunset = sunsetCalculator.getAstronomicalSunsetCalendarForDate(calNow);
			nauticalSunset = sunsetCalculator.getNauticalSunsetCalendarForDate(calNow);
			civilSunset = sunsetCalculator.getCivilSunsetCalendarForDate(calNow);


			tvETA.setText("Current Time: " + date_format1.format(calNow.getTime()));

			tvOfficialSunsetTime.setText("Official Sunset: " 	+ date_format2.format(officialSunset.getTime()));
			tvCivilSunsetTime.setText	("Civil Sunset: " 		+ date_format2.format(civilSunset.getTime()));
			tvNauticalSunsetTime.setText("Nautical Sunset: " 	+ date_format2.format(nauticalSunset.getTime()));
			tvAstroSunsetTime.setText	("Astronomical Sunset: "+ date_format2.format(astronomicalSunset.getTime()));
			
			//tvMetarString.setText		("METAR: "+ getMetarString() );
			
			getMETARinfo();
			
			//getMetarString();
			
			new FetchMETARStringAsyncTask().execute();
			
			
		}

		else {
			//do nothing if no locationAvailable
		}


        
	}

	
	
	private void getMETARinfo() {
		
		
		String metarString = "KAUS 131453Z 00000KT 10SM FEW150 29/18 A2997 RMK AO2 SLP137 T02940178 53002";
		Metar mMetar =  Metar.parseMetar(metarString);
		
		webView1 = (WebView) findViewById(R.id.webView1);
		//webView1.getSettings().setJavaScriptEnabled(true);
		//webView1.loadUrl("http://www.google.com");
		
		//get a METAR report from NOAA
		webView1.loadUrl("http://aviationweather.gov/adds/metars/?station_ids=KAUS&std_trans=translated&chk_metars=on&hoursStr=most+recent+only&chk_tafs=on&submitmet=Submit");

	}


	private String getMetarString() {

		new Thread(new Runnable() {

		    @Override
		    public void run() {
				try {
					String url = urlmetar + airportcode + ".TXT";
					metarStr = "";
					Log.d("metar","Metar url: "+ url);
					HttpGet httpGet = new HttpGet(url);
					HttpClient httpclient = new DefaultHttpClient();
					// Execute HTTP Get Request
					HttpResponse response = httpclient.execute(httpGet);
					content = response.getEntity().getContent();
					BufferedReader r = new BufferedReader(new InputStreamReader(content));
					StringBuilder total = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
						total.append(line);
					} 
					//strip the first 16chars (date info)
					metarStr = total.toString().substring(16); //textDisplayMetar.append("\n" + total.toString().substring(16) + "\n");
					Log.d("metar","Metar string: "+ metarStr);


		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();
		
		//Write the METAR string to the screen
		tvMetarString.setText		("METAR: "+ metarStr );
		
		
		//Create the METAR string into an object
		//Metar mMetar =  Metar.parseMetar(metarStr);

		
		return metarStr;
		
	}
	
	private class FetchMETARStringAsyncTask extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... params) {
	         
	     	try {
				String url = urlmetar + airportcode + ".TXT";
				metarStr = "";
				Log.d("metar","Metar url: "+ url);
				HttpGet httpGet = new HttpGet(url);
				HttpClient httpclient = new DefaultHttpClient();
				// Execute HTTP Get Request
				HttpResponse response = httpclient.execute(httpGet);
				content = response.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(content));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				} 
				//strip the first 16chars (date info)
				metarStr = total.toString().substring(16); //textDisplayMetar.append("\n" + total.toString().substring(16) + "\n");
				
				
				
				Log.d("metar","Metar string: "+ metarStr);
				

				
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	         
	         return metarStr;
	     }

	     protected void onPostExecute(String result) {
	         // here you have the result
	 		//Write the METAR string to the screen
	 		tvMetarString.setText		("METAR: "+ metarStr );
	 		
			//parse the METAR string
			try {
				mMetar =  Metar.parseMetar(metarStr);
			} catch (MetarParsingException e) {
				e.printStackTrace();
			}

			estimateSunsetRating(mMetar);

	     }

	 }

	/*

	private class LoadExampleTask extends
	AsyncTask<Void, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// code here
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

			XMLParser parser = new XMLParser(); // the parser create as seen in the Gist from GitHub
			String xml = parser.getXmlFromUrl(URL); // getting XML from URL
			Document doc = parser.getDomElement(xml); // getting DOM element

			NodeList nl = doc.getElementsByTagName(KEY_METAR);

			// looping through all song nodes <venue>
			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				map.put(KEY_ID, parser.getValue(e, KEY_ID));
				map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
				map.put(KEY_DESCRIPTION, parser.getValue(e, KEY_DESCRIPTION));
				map.put(KEY_LOCATION, parser.getValue(e, KEY_LOCATION.toString()));
				map.put(KEY_TAGS, parser.getValue(e, KEY_TAGS.toString()));
				map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
				map.put(KEY_THUMB_LARGE_URL, parser.getValue(e, KEY_THUMB_LARGE_URL));

				// adding HashList to ArrayList
				exampleList.add(map);
			}

			return exampleList;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);

			try {
				// Getting adapter by passing xml data ArrayList
				adapter = new YourExampleAdapter(getActivity(), exampleList);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	*/
	void getMetarXML(String station) {

		//station = "KAUS";
		//Example string -
		// http://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=KAUS&hoursBeforeNow=1

		//Things I'm intersted in -
		double mlat;
		double mlong;
		double temp_c;
		double dewpoint_c;
		double visibility_statue_mi;
		double altim_in_hg;
		double sea_level_pressure_mb;
		float sky_cover;
		double could_base_ft_agl;








	}

	//Called in result of METAR fetch
	// METAR object has been populated
	private void estimateSunsetRating(Metar mMetar){

		int sunsetRating;



			for (int i=0;i<mMetar.getClouds().size() ;i++) {
				Log.d("rating","Cloud Type/Height: "+ mMetar.getClouds().get(i).getType().toString() +"/"+ mMetar.getClouds().get(i).getHeight());
			}
			//doens't alwasy have visibility
			try {
				Log.d("rating","Visibility: "+ mMetar.getVisibility().getValue() + mMetar.getVisibility().getMeasure() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("rating","Pressure: "+ mMetar.getPressure().getValue() +" "+ mMetar.getPressure().getMeasure() );
			Log.d("rating","Clouds(overcast?): "+ mMetar.getClouds().contains(CloudType.OVC) );
			Log.d("rating","Clouds(broken?): "+ mMetar.getClouds().contains(CloudType.BKN) );
			Log.d("rating","Clouds(few?): "+ mMetar.getClouds().contains(CloudType.FEW) );
			Log.d("rating","Clouds(scattered?): "+ mMetar.getClouds().contains(CloudType.SCT) );


		//Toast sunset rating
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onPause() {
        // Make sure that when the activity goes to
        // background, the device stops getting locations
        // to save battery life.
        locationManager.removeUpdates(listenerCoarse);
        locationManager.removeUpdates(listenerFine);
        super.onPause();
    } 
    /**
    *   Creates LocationListeners
    */
    private void createLocationListeners() {
        listenerCoarse = new LocationListener() {
            public void onStatusChanged(String provider,
                int status, Bundle extras) {
                switch(status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    locationAvailable = false;
                    break;
                case LocationProvider.AVAILABLE:
                    locationAvailable = true;
                }
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
            public void onLocationChanged(Location location) {
                currentLocation = location;
                if (location.getAccuracy() > 1000 &&
                    location.hasAccuracy())
                    locationManager.removeUpdates(this);
            }
        };
        listenerFine = new LocationListener() {
            public void onStatusChanged(String provider,
                int status, Bundle extras) {
                switch(status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    locationAvailable = false;
                    break;
                case LocationProvider.AVAILABLE:
                    locationAvailable = true;
                }
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
            public void onLocationChanged(Location location) {
                currentLocation = location;
                if (location.getAccuracy() > 1000
                    && location.hasAccuracy())
                    locationManager.removeUpdates(this);
            }
        };
    }
    
    
    private void registerLocationListeners() {
        locationManager = (LocationManager)
            getSystemService(LOCATION_SERVICE);
         
        // Initialize criteria for location providers
        Criteria fine = new Criteria();
        fine.setAccuracy(Criteria.ACCURACY_FINE);
        Criteria coarse = new Criteria();
        coarse.setAccuracy(Criteria.ACCURACY_COARSE);
         
        // Get at least something from the device,
        // could be very inaccurate though
        currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(fine, true));
         
        if (listenerFine == null || listenerCoarse == null)
            createLocationListeners();
             
        // Will keep updating about every 500 ms until
        // accuracy is about 1000 meters to get quick fix.
        locationManager.requestLocationUpdates(
            locationManager.getBestProvider(coarse, true),
            500, 1000, listenerCoarse);
        // Will keep updating about every 500 ms until
        // accuracy is about 50 meters to get accurate fix.
        locationManager.requestLocationUpdates(
            locationManager.getBestProvider(fine, true),
            500, 50, listenerFine);
        
    }
    
    
}

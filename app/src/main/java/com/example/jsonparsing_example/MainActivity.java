package com.example.jsonparsing_example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity{
 

ProgressDialog loading;
Context mcontext;
JSONObject alldata=new JSONObject();
String no_record="";
TextView title;
String ID,Catname;
Button back;
MainActivity all_stuff = null;
String lodingmsg,connmsg;
ArrayList<Sectionsub_method> actorsList;
Button post;
	SectionsubAdapter adapter;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		  mcontext=this;
         loading=new ProgressDialog(mcontext);


		listview = (ListView)findViewById(R.id.listsection);
		mcontext=this;


		
		 if (isTablet(mcontext)) 
	        {
	        	Log.e("tablet","tablet");
	        	AppConstant.istablet=1;
	        }
	        else 
	        {
	        	AppConstant.istablet=0;
	        	Log.e("Phone","Phone");
	        }
		
		try{
			loading.setMessage(lodingmsg);
			loading.setTitle(connmsg);
			loading.show();
			loading.setCancelable(false);
			SyncMethod("https://affordplan.com/apps/webservice.php?action=get_dummy_appointment_list&u_userid=1&u_rolecode=SUPERADMIN");
		}
		catch (Exception e) 
	 	{
			e.printStackTrace();
	 	}
	
		listview.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) 
			{

				Intent i = new Intent(getApplicationContext(),contactpage.class);
				startActivity(i);
				/*String Item_Name =actorsList.get(position).gettitle();
				String Item_ID = actorsList.get(position).getId();
				String Item_desc =actorsList.get(position).getdesc();
				Log.i("Item_desc",Item_desc);
				Intent i = new Intent(getApplicationContext(),sectiondetaile.class);
				i.putExtra("postid", Item_ID);
				i.putExtra("id", ID);
				i.putExtra("name",Catname);
				i.putExtra("idescription", Item_desc);
				i.putExtra("title",Item_Name );
				
				startActivity(i);*/
				
				// Toast.makeText(getApplicationContext(), Item_ID, 0).show();
				 //Toast.makeText(getApplicationContext(), Item_Price, 0).show();		
			}
		});
		
		
		
		
		
		
	}


	
	 public static boolean isTablet(Context context) {
	        return (context.getResources().getConfiguration().screenLayout
	                & Configuration.SCREENLAYOUT_SIZE_MASK)
	                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	    }
	
	 private boolean isNetworkAvailable() 
		{
			boolean connected = false;
			if(AppConstant.istablet == 1)
			{
				ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)	 
				 {
				       // Log.d("mobile",connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState().toString());
				        Log.d("wifi",connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState().toString());
				        connected = true;
				   }
				    else
				        connected = false;	
			}
			else
			{
			ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			 if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
			            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
			 {
			        Log.d("mobile",connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState().toString());
			        Log.d("wifi",connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState().toString());
			        connected = true;
			   }
			    else
			        connected = false;
			}
			return connected;
		}

	 //Method for call url and get data		
	 	public void SyncMethod(final String GetUrl)
	 	{
	 		  Log.i("Url.............",GetUrl);
	 		  final Thread background = new Thread(new Runnable() 
	 		  {
	 		      // After call for background.start this run method call
	 		      public void run() 
	 		      {
	 		          try 
	 		          {
	 		        	  String url=GetUrl;
	 		        	  String SetServerString = "";
	 		        	  // document all_stuff = null;
	 		        	
						  SetServerString=fetchResult(url);
	 		              threadMsg(SetServerString);
	 		          }
	 		          catch (Throwable t) 
	 		          {
	 		        	  Log.e("Animation", "Thread  exception " + t);
	 		          }
	 		      }
	 		      private void threadMsg(String msg) 
	 		      {
	 		      	
	 		          if (!msg.equals(null) && !msg.equals("")) 
	 		          {
	 		              Message msgObj = handler11.obtainMessage();
	 		              Bundle b = new Bundle();
	 		              b.putString("message", msg);
	 		              msgObj.setData(b);
	 		              handler11.sendMessage(msgObj);
	 		          }
	 		      }
	 		      // Define the Handler that receives messages from the thread and update the progress
	 		     private final Handler handler11 = new Handler()
	 		      {
	 		          public void handleMessage(Message msg) 
	 		          {
	 		        	 try
	 		        	 {
	 		              String aResponse = msg.getData().getString("message");

							loading.cancel();
							 JSONObject get_res=new JSONObject(aResponse);

							 JSONObject res_data=new JSONObject();

							 actorsList = new ArrayList<Sectionsub_method>();
	 		                 JSONArray array=new JSONArray();

	 		                    		if(get_res.getString("message").equalsIgnoreCase("Appointments fetched Successfully"))
	 		                    		{
	 		                    			array=get_res.getJSONArray("Appointments");
											Log.e("Exam","screen>>"+array+"");
	 		                    			for(int aa=0;aa<array.length();aa++)
		 		                   		  	{
	 		                    		
	 		                    					actorsList.add(new Sectionsub_method(array.getJSONObject(aa).getString("email"),array.getJSONObject(aa).getString("fullname"),array.getJSONObject(aa).getString("full_address"),array.getJSONObject(aa).getString("mobileno"),array.getJSONObject(aa).getString("appointment_date"),array.getJSONObject(aa).getString("appointment_time"),array.getJSONObject(aa).getString("hospital_name"),array.getJSONObject(aa).getString("speciality")));

	 		                    			
											}
											adapter = new SectionsubAdapter(getApplication(),actorsList);
											listview.setAdapter(adapter);
											adapter.notifyDataSetChanged();
	 		                    		}
	 		                    		if(res_data.getString("messages").equalsIgnoreCase("No Category Found"))
	 		                    		{
	 		                    			Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();
	 		                    		}
	 		        	 }
	 		        	 catch(Exception e)
	 		        	 {
	 		        		 
	 		        	 }
	 		                      
	 		          }
	 		      };
	 		  });
	 		 
	 		  background.start();
	 	 }
				
public String fetchResult(String urlString) throws JSONException
		{
			StringBuilder builder;
			BufferedReader reader;
			URLConnection connection=null;
			URL url=null;
			String line;
			builder=new StringBuilder();
			reader=null;
			try {
				url=new URL(urlString);
				connection=url.openConnection();
				
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line=reader.readLine())!=null)
				{
					builder.append(line);
				}
				//Log.d("DATA", builder.toString());
			} catch (Exception e) {

			}
			//JSONArray arr=new JSONArray(builder.toString());
			return builder.toString();
		}
	 
	
	
}

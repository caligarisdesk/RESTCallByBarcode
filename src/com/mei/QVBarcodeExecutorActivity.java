package com.mei;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mei.IntentIntegrator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class QVBarcodeExecutorActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View v = findViewById(R.id.btnChangeImage);
        v.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
    	IntentIntegrator x = new IntentIntegrator(this);
		x.initiateScan();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	try
    	{
    		final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    		Runnable runnable = new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				processScan(scanResult.getContents());
    			}
    		};
    		new Thread(runnable).start();
    	}
    	catch(Exception e){;}
    }
    
    private void processScan(String command)
	{
    	String answer;
		try
		{
			HttpURLConnection con = null;
			URL url = new URL(command);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
				
			con.connect();
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			answer = reader.readLine();
			reader.close();
		}
		catch (Exception e){
			answer = e.getMessage();
		}
	}
}
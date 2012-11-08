package com.example;

import android.app.Activity;
import android.os.Bundle;


import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.net.wifi.WifiInfo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WIFIScanActivity extends Activity {
	TextView mainText;
	WifiManager mainWifi;
	WifiReceiver receiverWifi;
	WifiInfo wifiInf;
	List<ScanResult> wifiList;
	StringBuilder sb = new StringBuilder();
	String macAddr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainText = (TextView) findViewById(R.id.mainText);
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiInf = mainWifi.getConnectionInfo();
		macAddr = wifiInf.getMacAddress();

		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi.startScan();
		mainText.setText("\nStarting Scan...\n");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Refresh");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		mainWifi.startScan();
		mainText.setText("Starting Scan");
		return super.onMenuItemSelected(featureId, item);
	}

	protected void onPause() {
		unregisterReceiver(receiverWifi);
		super.onPause();
	}

	protected void onResume() {
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			sb = new StringBuilder();
			sb.append("My mac addr" + macAddr + "\n");
			wifiList = mainWifi.getScanResults();
			for(int i = 0; i < wifiList.size(); i++){
				sb.append(new Integer(i+1).toString() + ".");
				sb.append((wifiList.get(i)).toString());
				sb.append("\n");
			}
			
			mainText.setText(sb);
		}
	}
}


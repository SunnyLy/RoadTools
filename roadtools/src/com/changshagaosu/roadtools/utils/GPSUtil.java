package com.changshagaosu.roadtools.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.changshagaosu.roadtools.RoadToolsApplication;

public class GPSUtil {
	private LocationManager locationManager = null;
	private LocationListener gpsListener = null;
	private LocationListener netListener = null;
	private Location currentLocation;
	private static final int CHECK_INTERVAL = 1000 * 30;

	private static GPSUtil instance = null;

	public static GPSUtil getInstance() {
		if (instance == null) {
			instance = new GPSUtil();
		}
		return instance;
	}

	public GPSUtil() {
		locationManager = (LocationManager) RoadToolsApplication.getContext().getSystemService(
				Context.LOCATION_SERVICE);
	}

	public void start() {
		registerLocationListener();
	}

	private void registerLocationListener() {
		netListener = new MyLocationListner();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 
				10000, 15, netListener);
		gpsListener = new MyLocationListner();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10000, 15, gpsListener);
		
	}

	private class MyLocationListner implements LocationListener {
		public void onLocationChanged(Location location) {
			if (currentLocation != null) {
				if (isBetterLocation(location, currentLocation)) {
					currentLocation = location;
					showLocation(location);
				}
			} else {
				currentLocation = location;
				showLocation(location);
			}
			if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
				locationManager.removeUpdates(this);
			}
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	private void showLocation(Location location) {
		Intent intent = new Intent();
		intent.setAction("com.changshagaosu.roadtools.utils.GPSUtil");
		intent.putExtra("Latitude", location.getLatitude());
		intent.putExtra("Longitude", location.getLongitude());
		intent.putExtra("Accuracy", location.getAccuracy());
		intent.putExtra("Bearing", location.getBearing());
		intent.putExtra("Altitude", location.getAltitude());
		intent.putExtra("Speed", location.getSpeed());
		RoadToolsApplication.getContext().sendBroadcast(intent);
	}

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			return true;
		}

		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public void stop() {
		if (locationManager != null) {
			if (gpsListener != null) {
				locationManager.removeUpdates(gpsListener);
			}
		}
	}

	public boolean isProviderEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}

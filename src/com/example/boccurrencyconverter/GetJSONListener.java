package com.example.boccurrencyconverter;

import org.json.JSONArray;

public interface GetJSONListener {
	public void onRemoteCallComplete(JSONArray json);
}
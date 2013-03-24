package com.boc.currencyconverter;

import org.json.JSONArray;

public interface GetJSONListener {
	public void onRemoteCallComplete(JSONArray json);
}
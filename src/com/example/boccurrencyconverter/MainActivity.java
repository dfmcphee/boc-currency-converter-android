package com.example.boccurrencyconverter;

import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	JSONArray rates;
	
	BigDecimal fromAmount;
	BigDecimal toAmount;
	
	BigDecimal fromRate;
	BigDecimal toRate;
	
	double value;
	
	int fromSelected;
	int toSelected;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		fromSelected = 0;
		toSelected = 1;

		
		View view = this.findViewById(android.R.id.content);
		final Activity activity = this;
		
		// Setup event to dismiss keyboard when background is touched
		view.setOnTouchListener(new View.OnTouchListener() {  
			@Override
		    public boolean onTouch (View v, MotionEvent event)
		    {
		        hideSoftKeyboard(activity);
		        return false;
		    }
		});
		
		// Setup event to pick currency from
		final Button fromButton = (Button) findViewById(R.id.selectConvertFrom);
        fromButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showSelectFrom(v);
            }
        });
        
        // Setup event to pick currency to
        final Button toButton = (Button) findViewById(R.id.selectConvertTo);
        toButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showSelectTo(v);
            }
        });
        
        // Setup event to pick currency to
        final ImageButton swapButton = (ImageButton) findViewById(R.id.swapButton);
        swapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	swapCurrencies(v);
            }
        });
        
        // Setup on change event for from value
        EditText fromInput = (EditText)findViewById(R.id.fromInput);
        fromInput.addTextChangedListener(new TextWatcher() {
        	@Override
        	public void afterTextChanged(Editable s) {
        		try {
        			value = Double.parseDouble(s.toString());
        			calculateExchangeRate();
        		} catch(NumberFormatException e) {
        			// TODO Auto-generated catch block
    				e.printStackTrace();
        		}
            }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
        });

		JSONClient client = new JSONClient(this, l);
		String url = "http://exchng.ca/rates?%7b%22$sort%22:%7b%22order%22:1%7d%7d";
		client.execute(url);
	}
 
    GetJSONListener l = new GetJSONListener(){
 
		@Override
		public void onRemoteCallComplete(JSONArray jsonFromNet) {
			rates = jsonFromNet;

			EditText fromInput = (EditText) findViewById(R.id.fromInput);
			
			try {
				fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
				fromInput.setText("1.00");
				toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
				calculateExchangeRate();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void calculateExchangeRate() {
		try {
			fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
			toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
			EditText fromInput = (EditText) findViewById(R.id.fromInput);
			double value = Double.parseDouble(fromInput.getText().toString());
			double exchangeRate = fromRate.doubleValue() / toRate.doubleValue();
			double answer = value * exchangeRate;
		
			EditText answerText = (EditText) findViewById(R.id.answer);
			answerText.setText(String.format("%.2f", answer));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void swapCurrencies(View v) {
		// Swap 
		int tempSelected = toSelected;
		toSelected = fromSelected;
		fromSelected = tempSelected;
		
		Button selectFromButton = (Button) findViewById(R.id.selectConvertFrom);
		String labelTo = (String) selectFromButton.getText();
	    
	    Button selectToButton = (Button) findViewById(R.id.selectConvertTo);
	    String labelFrom = (String) selectToButton.getText();
	    
	    selectFromButton.setText(labelFrom);
	    selectToButton.setText(labelTo);
		
		calculateExchangeRate();
	}
	
	public void showSelectFrom(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    
	    // This activity implements OnMenuItemClickListener
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	            MainActivity.this.selectNewFrom(item);
	            return true;
	        }
	    });
	    
	    for (int i=0; i < rates.length(); i++) {
	    	try {
	    		int order = Integer.parseInt(rates.getJSONObject(i).getString("order").toString());
				popup.getMenu().add(0, i, order, rates.getJSONObject(i).getString("label").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    popup.show();
	}
	
	public void showSelectTo(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    
	    // This activity implements OnMenuItemClickListener
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	            MainActivity.this.selectNewTo(item);
	            return true;
	        }
	    });
	    
	    for (int i=0; i < rates.length(); i++) {
	    	try {
	    		int order = Integer.parseInt(rates.getJSONObject(i).getString("order").toString());
				popup.getMenu().add(0, i, order, rates.getJSONObject(i).getString("label").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    popup.show();
	}
	
	public boolean selectNewFrom(MenuItem item) {
	    fromSelected = item.getItemId();
	    
	    Button button = (Button) findViewById(R.id.selectConvertFrom);
	    button.setText(item.getTitle());
	    
	    try {
			fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
			calculateExchangeRate();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	public boolean selectNewTo(MenuItem item) {
	    toSelected = item.getItemId();
	    
	    Button button = (Button) findViewById(R.id.selectConvertTo);
	    button.setText(item.getTitle());
	    
	    try {
			toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
			calculateExchangeRate();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}

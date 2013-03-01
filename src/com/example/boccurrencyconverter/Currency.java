package com.example.boccurrencyconverter;

import java.math.BigDecimal;


public class Currency {
	private String label;
    private String shortcode;
 
    public Currency(String label, String shortcode) {
        this.label = label;
        this.shortcode = shortcode;
    }
 
    public String getLabel() {
        return label;
    }
 
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getShortcode() {
        return shortcode;
    }
 
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
}
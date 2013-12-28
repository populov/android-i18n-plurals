package com.test.plurals;

import java.util.ArrayList;
import java.util.Locale;

import com.seppius.i18n.plurals.PluralResources;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TestPluralsActivity extends Activity 
{
    private static final int TESTS_COUNT = 123;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        selectLanguage(getBaseContext().getResources().getConfiguration().locale.getLanguage());
        
        class LanguageDesciptor
        {
            String name;
            String code;
            
            LanguageDesciptor(String name, String code)
            {
                this.name = name;
                this.code = code;
            }
            
            public String toString( )
            {
                return name + " (" + code + ")";
            }
        };
        
        final ArrayList<LanguageDesciptor> languages = new ArrayList<LanguageDesciptor>();
        languages.add( new LanguageDesciptor("English"    , "en") );  // PluralRules_One
        //languages.add( new LanguageDesciptor("French"     , "fr") );  // PluralRules_French
        //languages.add( new LanguageDesciptor("Czech"      , "cs") );  // PluralRules_Czech
        languages.add( new LanguageDesciptor("Russian"    , "ru") );  // PluralRules_Balkan
        //languages.add( new LanguageDesciptor("Latvian"    , "lv") );  // PluralRules_Latvian
        //languages.add( new LanguageDesciptor("Lithuanian" , "lt") );  // PluralRules_Lithuanian
        //languages.add( new LanguageDesciptor("Irish"      , "ga") );  // PluralRules_Two
        //languages.add( new LanguageDesciptor("Hindi"      , "hi") );  // PluralRules_Zero
        //languages.add( new LanguageDesciptor("Thai"       , "th") );  // PluralRules_None
        //languages.add( new LanguageDesciptor("Arabic"     , "ar") );  // PluralRules_Arabic
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<LanguageDesciptor> adapter = new ArrayAdapter<LanguageDesciptor>(this, android.R.layout.simple_spinner_item, languages.toArray(new LanguageDesciptor [0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
			@Override
			public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long selected) 
			{
				selectLanguage( languages.get(position).code );
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
        });
        
        spinner.setSelection(1);
        
    }
    
    private void selectLanguage( String language  ) 
    {
        Configuration config = getResources().getConfiguration();
        
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        
        StringBuilder text_after = new StringBuilder();
        StringBuilder text_before = new StringBuilder();
        	
        PluralResources plural_resources = null; 
        try 
        {
        	plural_resources = new PluralResources(getResources());
        } 
        catch (SecurityException e) 
        {
        	Log.e("", "result:" + e );
        	e.printStackTrace();
        } 
        catch (NoSuchMethodException e) 
        {
        	Log.e("", "result:" + e );
        	e.printStackTrace();
        } 

        for ( int i = 0; i <= TESTS_COUNT; i++ ) 
        {
    		try 
    		{
    			text_after.append( plural_resources.getQuantityString( R.plurals.parrot_count, i, i) );
    		} 
    		catch (android.content.res.Resources.NotFoundException e) 
    		{
    			Log.e("", "result:" + e );
    			e.printStackTrace();
    			text_after.append( e.getMessage() ); 
    		}
    		text_after.append("\n"); 

        	text_before.append(getResources().getQuantityString( R.plurals.parrot_count, i, i ));
        	text_before.append("\n"); 
        }
        
        TextView text_before_view = (TextView)findViewById(R.id.text_before);
        text_before_view.setText(text_before);

        TextView text_after_view = (TextView)findViewById(R.id.text_after);
        text_after_view.setText(text_after);
    }
}
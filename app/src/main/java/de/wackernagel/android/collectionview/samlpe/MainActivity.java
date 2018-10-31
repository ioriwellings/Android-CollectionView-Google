package de.wackernagel.android.collectionview.samlpe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.wackernagel.android.collectionview.sample.R;

public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.sample_collection_activity_main );
		
		if( savedInstanceState == null ) {
			getSupportFragmentManager().beginTransaction().add( R.id.container, new CollectionFragment() ).commit();
		}
	}
	
}

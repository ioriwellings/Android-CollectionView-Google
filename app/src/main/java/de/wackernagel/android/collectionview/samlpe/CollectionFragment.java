package de.wackernagel.android.collectionview.samlpe;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.samples.apps.iosched.ui.widget.CollectionView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import de.wackernagel.android.collectionview.sample.R;

public class CollectionFragment extends Fragment implements LoaderCallbacks<Cursor> {
	public static final int LOADER_ID = 1;
	
	private CollectionView collectionView;
	private TextView emptyView;
	private ProgressBar progressView;
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		return inflater.inflate( R.layout.sample_collection_fragment_collection, container, false );
	}
	
	@Override
	public void onViewCreated( View view, Bundle savedInstanceState ) {
		collectionView = ( CollectionView ) view.findViewById( R.id.collection_view );
		emptyView = (TextView) view.findViewById( R.id.empty_text );
		progressView = (ProgressBar) view.findViewById( R.id.loading );
	}
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
		showProgessView();
		getLoaderManager().initLoader( LOADER_ID, null, this );
	}
	
	@Override
	public Loader<Cursor> onCreateLoader( int id, Bundle args ) {
		if( id == LOADER_ID ) {
			return new CollectionCursorLoader( getActivity() );
		}
		return null;
	}

	@Override
	public void onLoadFinished( Loader<Cursor> loader, Cursor cursor ) {
		if( loader.getId() == LOADER_ID ) {
			if( cursor != null && cursor.moveToFirst() ) {
				hideEmptyView();
				
				final CollectionAdapter adapter = new CollectionAdapter( getActivity(), cursor );
	         collectionView.setCollectionAdapter(adapter);
	         collectionView.updateInventory( adapter.getInventory() );
			} else {
				showEmptyView();
			}
		}
	}

	@Override
	public void onLoaderReset( Loader<Cursor> loader ) {
		if( loader.getId() == LOADER_ID ) {
			
		}
	}

	public void animateReload() {
      ViewHelper.setAlpha( collectionView, 0);
      ViewPropertyAnimator.animate( collectionView ).alpha(1).setDuration( 250 ).setInterpolator(new DecelerateInterpolator());
  }
	
	private void hideEmptyView() {
      emptyView.setVisibility(View.GONE);
      progressView.setVisibility(View.GONE);
  }
	
	private void showProgessView() {
		emptyView.setVisibility( View.GONE );
		progressView.setVisibility( View.VISIBLE );
	}
	
	private void showEmptyView() {
		emptyView.setText( "Empty" );
		emptyView.setVisibility( View.VISIBLE );
		progressView.setVisibility( View.GONE );
	}
}

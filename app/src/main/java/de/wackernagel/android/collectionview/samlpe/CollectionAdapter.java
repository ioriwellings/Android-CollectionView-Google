package de.wackernagel.android.collectionview.samlpe;

import com.google.samples.apps.iosched.ui.widget.CollectionView;
import com.google.samples.apps.iosched.ui.widget.CollectionViewCallbacks;

import de.wackernagel.android.collectionview.sample.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionAdapter extends CursorAdapter implements CollectionViewCallbacks {
	private Context mContext;
	private Cursor mCursor;
	
	private static final int HERO_GROUP_ID = 0;
	
	public CollectionAdapter( Context context, Cursor c ) {
		super( context, c, 0 );
		mContext = context;
		mCursor = c;
	}
	
	public CollectionView.Inventory getInventory() {
      CollectionView.Inventory inventory = new CollectionView.Inventory();
      CollectionView.InventoryGroup inventoryGroup = null;
      int actualGroupId = -1;
      int p_gid = 1;
      int p_group = 2;
      
      mCursor.moveToFirst();
      do {
      	int groupId = mCursor.getInt( p_gid );
      	String groupName = mCursor.getString( p_group );
      	
      	// Create a new InventoryGroup or increase the size of the actual one.
      	if( actualGroupId != groupId ) {
      		
      		// Add the previous created InventoryGroup to the Inventory.
      		// !! We do this at the latest point because the InventoryGroup
      		//		is cloned after it was added to the Inventory otherwise it
      		//		would not register the InventoryGroup size increasment.
      		if( inventoryGroup != null ) {
      			inventory.addGroup( inventoryGroup );
      		}
      		
      		// Create the new InventoryGroup
      		inventoryGroup = new CollectionView.InventoryGroup( groupId )
      			.setDisplayCols( 1 )
      			.setDataIndexStart( mCursor.getPosition() )
      			.setShowHeader( true )
      			.setHeaderLabel( groupName )
      			.setItemCount( 1 );
      		actualGroupId = groupId;
      	} else {
      		// or increase the size of the existing one
      		inventoryGroup.incrementItemCount();
      	}
      	
      } while( mCursor.moveToNext() );
      
      // Don't forget to add the last created InventoryGroup
      inventory.addGroup( inventoryGroup );
      
      // or use an alternative way
      /*
      // setup hero hashtag
      inventory.addGroup(new CollectionView.InventoryGroup( HERO_GROUP_ID )
              .setDisplayCols(1)
              .setItemCount(1)
              .setHeaderLabel( "Hero View" )
              .setShowHeader(true));

      // setup other hashtags
      inventory.addGroup(new CollectionView.InventoryGroup( 1 )
              .setDisplayCols(1)
              .setItemCount(mCursor.getCount() - 1)
              .setDataIndexStart(1)
              .setShowHeader(false));
      */
      
      return inventory;
  }

	@Override
	public View newCollectionHeaderView( Context context, ViewGroup parent ) {
		return new TextView( context );
	}

	@Override
	public void bindCollectionHeaderView( Context context, View view, int groupId, final String headerLabel ) {
		( (TextView) view ).setText( headerLabel );
		view.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				Toast.makeText( mContext.getApplicationContext(), headerLabel, Toast.LENGTH_SHORT ).show();
			}
		} );
	}

	@Override
	public View newCollectionItemView( Context context, int groupId, ViewGroup parent ) {
		return isHeroView(groupId) ? newHeroView(context, parent) : newView(context, null, parent);
	}

	private View newHeroView( Context context, ViewGroup parent ) {
		return LayoutInflater.from(context).inflate(R.layout.sample_collection_list_item_hero, parent, false);
	}

	private boolean isHeroView(int groupId) {
      return groupId == HERO_GROUP_ID;
  }

	@Override
	public void bindCollectionItemView( Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag ) {
		setCursorPosition(dataIndex);
      if (isHeroView(groupId)) {
          bindHeroView(view, context, mCursor);
      } else {
          bindView(view, context, mCursor);
      }		
	}

	private void bindHeroView( View view, Context context, Cursor cursor ) {
		final String hashtag = cursor.getString(cursor.getColumnIndex( "item" ));
      ((TextView) view.findViewById(R.id.name)).setText(hashtag);
      view.setBackgroundColor( context.getResources().getColor( R.color.card_gray ) );
      view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
         	 Toast.makeText( mContext.getApplicationContext(), hashtag, Toast.LENGTH_SHORT ).show();
          }
      });
	}

	private void setCursorPosition( int position ) {
		if (!mCursor.moveToPosition(position)) {
         throw new IllegalStateException("couldn't move cursor to position " + position);
     }
	}

	@Override
	public void bindView( View view, Context context, Cursor cursor ) {
		ViewHolder holder = (ViewHolder) view.getTag();
      final String hashtag = cursor.getString( cursor.getColumnIndex( "item" ));
      view.setBackgroundColor( context.getResources().getColor( cursor.getPosition() % 2 == 0 ? R.color.blue_light : R.color.blue_dark ) );
      view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText( mContext.getApplicationContext(), hashtag, Toast.LENGTH_SHORT ).show();
          }
      });

      final String desc = cursor.getString(cursor.getColumnIndex( "group" ));
      holder.name.setText(hashtag.replace("#io14 ", ""));
      if (!TextUtils.isEmpty(desc)) {
          holder.description.setVisibility(View.VISIBLE);
          holder.description.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
            	  Toast.makeText( mContext.getApplicationContext(), desc, Toast.LENGTH_SHORT ).show();
              }
          });
      } else {
          holder.description.setVisibility(View.GONE);
      }
	}

	@Override
	public View newView( Context context, Cursor cursor, ViewGroup parent ) {
		View view = LayoutInflater.from(context).inflate(R.layout.sample_collection_list_item, parent, false);
      ViewHolder holder = new ViewHolder();
      assert view != null;
      holder.name = (TextView) view.findViewById(R.id.name);
      holder.description = (ImageButton) view.findViewById(R.id.description);
      view.setTag(holder);
      return view;
	}

	private static final class ViewHolder {
      TextView name;
      ImageButton description;
  }

	@Override
	public boolean isViewEnabled( int position, int groupId, boolean isHeader ) {
		return true;
	}

	@Override
	public void onNewEmptyView( Context context, View emptyView, int groupId ) {
	}

	@Override
	public void onNewRowView( Context context, LinearLayout rowView, int row, int groupId ) {		
	}
}

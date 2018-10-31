package de.wackernagel.android.collectionview.samlpe;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.CursorLoader;

public class CollectionCursorLoader extends CursorLoader {
	public static final String[] PROJECTION = { "_id", "group_id", "group", "item" };
	private long id = 0;

	public CollectionCursorLoader( Context context ) {
		super( context );
	}
	
	@Override
	public Cursor loadInBackground() {
		final MatrixCursor cursor = new MatrixCursor( PROJECTION );
		addRow( cursor, 0, "Hero", "I'm a Hero" );
		addRow( cursor, 1, "Intro", "First" );
		addRow( cursor, 1, "Intro", "Second" );
		addRow( cursor, 1, "Intro", "Third" );
		addRow( cursor, 2, "Content", "1" );
		addRow( cursor, 2, "Content", "2" );
		addRow( cursor, 3, "Footer", "Big" );
		addRow( cursor, 3, "Footer", "Foot" );
		return cursor;
	}

	private void addRow( MatrixCursor cursor, long gid, String group, String item ) {
		cursor.addRow( new Object[]{ id, gid, group, item } );
		id++;
	}

}

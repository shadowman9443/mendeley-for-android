/*
 *  Mendroid: a Mendeley Android client
 *  Copyright 2011 Martin Paul Eve <martin@martineve.com>
 *
 *  This file is part of Mendroid.
 *
 *  Mendroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Mendroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Mendroid.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

package com.martineve.mendroid.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.martineve.mendroid.MendeleyForAndroid;
import com.martineve.mendroid.R;
import com.martineve.mendroid.data.MendeleyCollectionsProvider;
import com.martineve.mendroid.data.MendeleyDatabase;
import com.martineve.mendroid.sync.MendeleySyncAdapter;

public class CollectionsActivity extends ListActivity {
	private static String TAG = "com.martineve.mendroid.activity.CollectionsActivity";
	ArrayList<HashMap<String,String>> c_list = new ArrayList<HashMap<String,String>>();  

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.collections);
		
		// determine if a sync is in process
		TextView emptySync = (TextView) findViewById(R.id.emptySync);
		if(MendeleySyncAdapter.preFetchedArray != null)
		{
			emptySync.setText(Html.fromHtml(getString(R.string.sync_in_progress)));
		}

		Cursor c = managedQuery(MendeleyCollectionsProvider.COLLECTIONS_URI, null, null, null, MendeleyDatabase.COLLECTION_NAME + " asc");

		String[] from = new String[] {MendeleyDatabase.COLLECTION_NAME, MendeleyDatabase.COLLECTION_SIZE, MendeleyDatabase.COLLECTION_TYPE, MendeleyDatabase._ID};

		int[] to = new int[] { R.id.collection_entry, R.id.collection_size, R.id.collection_private_status, R.id.collection_post_id };

		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.collection_item, c, from, to);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// gets a Cursor, set to the correct record
		Cursor c = (Cursor)l.getItemAtPosition(position);
		
		// launch the author intent
		Log.v(TAG, "Launching collection authors intent.");
		Intent launchCollectionAuthors = new Intent(CollectionsActivity.this, CollectionAuthorsActivity.class);
		launchCollectionAuthors.putExtra("collection_id", c.getString(c.getColumnIndex(MendeleyDatabase._ID)));
		startActivity(launchCollectionAuthors);
		
		super.onListItemClick(l, v, position, id);
	}
}

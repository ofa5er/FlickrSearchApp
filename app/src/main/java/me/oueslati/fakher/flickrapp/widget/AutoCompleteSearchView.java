package me.oueslati.fakher.flickrapp.widget;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import me.oueslati.fakher.flickrapp.R;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class AutoCompleteSearchView extends SearchView {
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    public AutoCompleteSearchView(Context context) {
        super(context);
        initialize();
    }

    public AutoCompleteSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        mSearchAutoComplete = (SearchAutoComplete) findViewById(
                android.support.v7.appcompat.R.id.search_src_text);

        mSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSearchAutoComplete.setText(adapterView.getItemAtPosition(i).toString());

                //Place cursor at the end
                mSearchAutoComplete.setSelection(
                        adapterView.getItemAtPosition(i).toString().length());
            }
        });
        loadSearchHistory();

    }

    private void loadSearchHistory() {
        SharedPreferences settings = getContext().getSharedPreferences("keywords_history", 0);
        int size = settings.getInt("history_size", 0);
        if (size != 0) {
            String[] history = new String[size];

            for (int i = 0; i < size; i++) {
                history[i] = settings.getString(String.valueOf(i + 1), "empty");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                    R.layout.search_history_dropdown, history);
            mSearchAutoComplete.setAdapter(adapter);
        }
    }

    /**
     * Save the keywords in the SharedPreferences keywords_history
     *
     * @param newKeyword the keyword to save in the search history
     */
    public void saveKeywordsHistory(String newKeyword) {
        SharedPreferences settings = this.getContext().getSharedPreferences("keywords_history", 0);
        int size = settings.getInt("history_size", 0);
        String[] history = new String[size + 1];
        boolean repeat = false;
        for (int i = 0; i < size; i++) {
            history[i] = settings.getString(String.valueOf(i + 1), "empty");
            if (history[i].equals(newKeyword)) {
                repeat = true;
            }
        }
        if (!repeat) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(String.valueOf(size + 1), newKeyword);
            editor.putInt("history_size", size + 1);
            editor.apply();
            history[size] = newKeyword;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                    R.layout.search_history_dropdown, history);
            mSearchAutoComplete.setAdapter(adapter);
        }
    }

}

package ru.avdeev.android.a6_1_2_recovery_of_activity_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import com.google.android.material.behavior.SwipeDismissBehavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String KEY1="key1";
    private static final String KEY2="key2";
    private static final String LARGETEXT="large_text";
    private SwipeRefreshLayout swipeRefresh;
    private ListView list;
    private ListAdapter listContentAdapter;
    private SimpleAdapter listSimpleAdapter;
    List<Map<String, String>> values;
    List<Map<String, String>> result;
    ArrayList<Integer> deleteListIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY1)) {
            deleteListIndex = savedInstanceState.getIntegerArrayList(KEY1);
        } else {
            deleteListIndex = new ArrayList<>();
        }
        result = new ArrayList<>();
        list = findViewById(R.id.list);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        saveText(getString(R.string.large_text));
        listContentAdapter = createAdapter(savedInstanceState);
        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deleteLine = getResources().getString(R.string.large_text);
                deleteTextString(deleteLine);
                values.remove(i);
                deleteListIndex = new ArrayList<>();
                deleteListIndex.add(i);
                listSimpleAdapter.notifyDataSetChanged();
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                recreate();
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(KEY1,deleteListIndex);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        deleteListIndex = savedInstanceState.getIntegerArrayList(KEY1);
    }

    @NonNull
    private ListAdapter createAdapter(Bundle inputDelArrayListInteger) {
        values= prepareContent(KEY1,KEY2,inputDelArrayListInteger);
        listSimpleAdapter = new SimpleAdapter(this, values, android.R.layout.simple_list_item_2, new String[]{KEY1,KEY2}, new int[]{android.R.id.text1,android.R.id.text2});
        return listSimpleAdapter;
    }

    @NonNull
    private List<Map<String, String>> prepareContent(String key1, String key2, Bundle inputDelArrayListInteger) {
        String[] text=loadText().split("\n\n");
        List <String> textString = new ArrayList<String>();
        for (int j=0; j<text.length;j++) {
            textString.add(text[j]);
        }
        if (!deleteListIndex.isEmpty()) {
            for (int i=0;i<deleteListIndex.size(); i++) {
               textString.remove(deleteListIndex.get(i));
            }
        }
        for (int i=0; i<textString.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY1, textString.get(i));
            map.put(KEY2, textString.get(i).length()+"");
            result.add(map);
        }

        return result;
    }
    private void saveText (String text) {
        getSharedPreferences(LARGETEXT,MODE_PRIVATE)
                .edit()
                .putString(KEY1,text)
                .apply();
    }
    private String loadText() {
        return getSharedPreferences(LARGETEXT,MODE_PRIVATE).getString(KEY1,"");
    }
    private void deleteTextString (String text) {
        getSharedPreferences(LARGETEXT,MODE_PRIVATE)
                .edit()
                .remove(text)
                .apply();
    }
}
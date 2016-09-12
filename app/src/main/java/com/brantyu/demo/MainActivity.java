package com.brantyu.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.brantyu.byrecyclerview.BYRecyclerView;
import com.brantyu.byrecyclerview.adapter.DisplayableItem;
import com.brantyu.byrecyclerview.adapter.OnLoadMoreListener;
import com.brantyu.demo.model.Advertisement;
import com.brantyu.demo.model.Cat;
import com.brantyu.demo.model.Dog;
import com.brantyu.demo.model.Gecko;
import com.brantyu.demo.model.Snake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    BYRecyclerView mBYRecyclerView;
    MainAdapter mAdapter;
    int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBYRecyclerView = (BYRecyclerView) findViewById(R.id.recyclerView);
        mBYRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, getAnimals());
        mBYRecyclerView.setAdapter(mAdapter);
        mBYRecyclerView.setOnRefreshListener(this);
        mAdapter.setMore(R.layout.load_more,this);
        mAdapter.setNoMore(R.layout.no_more);
        mAdapter.setError(R.layout.load_more_error);

        findViewById(R.id.reptielsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReptilesActivity.class));
            }
        });
    }

    private List<DisplayableItem> getAnimals() {
        List<DisplayableItem> animals = new ArrayList<>();

        animals.add(new Cat("American Curl"));
        animals.add(new Cat("Baliness"));
        animals.add(new Cat("Bengal"));
        animals.add(new Cat("Corat"));
        animals.add(new Cat("Manx"));
        animals.add(new Cat("Nebelung"));
        animals.add(new Dog("Aidi"));
        animals.add(new Dog("Chinook"));
        animals.add(new Dog("Appenzeller"));
        animals.add(new Dog("Collie"));
        animals.add(new Snake("Mub Adder", "Adder"));
        animals.add(new Snake("Texas Blind Snake", "Blind snake"));
        animals.add(new Snake("Tree Boa", "Boa"));
        animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
        animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
        animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
        animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());

        Collections.shuffle(animals);
        return animals;
    }

    @Override
    public void onRefresh() {

        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mAdapter.clear();
                mAdapter.addAll(getAnimals());
                mCount=0;
            }
        }.execute();

    }

    @Override
    public void onLoadMore() {
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mCount++;
                switch (mCount){
                    case 1:
                        mAdapter.addAll(getAnimals());
                        break;
                    case 2:
                        mAdapter.pauseMore();
                        break;
                    case 3:
                        mAdapter.addAll(getAnimals());
                        break;
                    case 4:
                        mAdapter.stopMore();
                        break;
                }

            }
        }.execute();
    }

    @Override
    public void onReloadMore() {
        onLoadMore();
    }
}

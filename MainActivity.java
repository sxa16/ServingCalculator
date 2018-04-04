package com.example.jianingsun.servingsizecalculator;



import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Array of options --> ArrayAdapter --> ListView

// list view: {views: pot_item.xml}

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GETTINGNAME = 10;
    public static final int EDIT_POT = 8;
    public static final String POTSTOBESAVED = "current list";
    public static final String NAME_POTS = "NamePots";
    public static final String LIST_SIZE = "list size";
    public static int index = 8;
    private PotCollection myPots = new PotCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPreviousData(MainActivity.this);
        setupAddBtn();
        setupDeleteBtn();
        populateListView();
        registerClickCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater addMenu=getMenuInflater();
        addMenu.inflate(R.menu.actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId=item.getItemId();
        if(itemId==R.id.addAPot){
            Intent intent = SecondActivity.MakeIntent(MainActivity.this );

            startActivityForResult(intent, REQUEST_CODE_GETTINGNAME);
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupDeleteBtn() {
        Button btn=(Button)findViewById(R.id.deleteBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPots.countPots()==0){
                    Toast.makeText(MainActivity.this, "You have nothing to delete", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Please tap the pot you want to delete", Toast.LENGTH_SHORT)
                            .show();
                    ListView list = (ListView) findViewById(R.id.listViewMain);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            myPots.deletePot(position);
                            populateListView();
                            Toast.makeText(MainActivity.this, "Click on finish deleting button when you finish", Toast.LENGTH_SHORT)
                                    .show();
                            Button btn = (Button) findViewById(R.id.deleteBtn);
                            btn.setText("FINISH DELETING");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Button btn = (Button) findViewById(R.id.deleteBtn);
                                    btn.setText("DELETE");
                                    registerClickCallback();
                                }
                            });

                        }
                    });
                }

            }
        });
        saveMyPots();
    }
    private void setupAddBtn() {
        Button AddPot = (Button) findViewById(R.id.addBtn);
        AddPot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SecondActivity.MakeIntent(MainActivity.this );
                startActivityForResult(intent, REQUEST_CODE_GETTINGNAME);
            }
        });
    }

    private void populateListView() {
        String[] myPotList = myPots.getPotDescriptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.potslist,
                myPotList);
        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GETTINGNAME:
                if (resultCode == Activity.RESULT_OK) {
                    //getting the pot
                    String newName = SecondActivity.getNewPotName(data);
                    String newWeight = SecondActivity.getNewPotWeight(data);

                    Log.i("My Calculator", "data passed");

                    addNewPot(newName, newWeight);
                    saveMyPots();
                } else
                    Log.i("My Calculator", "my app canceled");
        }
        switch (requestCode) {
            case EDIT_POT:
                if (resultCode == Activity.RESULT_OK) {
                    int pos = index;
                    String modifiedName = SecondActivity.getNewPotName(data);
                    String newpotWeight = SecondActivity.getNewPotWeight(data);
                    int modifiedWeight = Integer.parseInt(newpotWeight);
                    Pot editedPot = new Pot(modifiedName, modifiedWeight);
                    myPots.changePot(editedPot, pos);
                    populateListView();
                    saveMyPots();
                }

        }
    }

    private void addNewPot(String name,String weight) {
        int newWeight=Integer.parseInt(weight);

        Pot newPot = new Pot(name, newWeight);
        myPots.addPot(newPot);
        populateListView();
        saveMyPots();

    }

    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Pot potClicked =myPots.getPot(position);
                Intent intent=CalculateActivity.makeLaunchIntent(MainActivity.this, potClicked);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = SecondActivity.MakeIntent(MainActivity.this );
                Pot potSelected=myPots.getPot(position);
                intent.putExtra("editname",potSelected.getName());
                intent.putExtra("editweight",potSelected.getWeightInG());
                index=position;
                startActivityForResult(intent, EDIT_POT);
                return true;
            }
        });
        setupDeleteBtn();

    }
    public boolean saveMyPots(){
        int sizeOfPots=myPots.countPots();
        SharedPreferences PotsToSave = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = PotsToSave.edit();
        editor.putInt("list size",sizeOfPots);
        int index=0;
        while(index<sizeOfPots){
            editor.remove(LIST_SIZE+index);
            Pot currentPots = myPots.getPot(index);
            editor.putString(NAME_POTS +index, currentPots.getName());
            editor.putInt(POTSTOBESAVED+index,currentPots.getWeightInG());
            index++;
        }
        return editor.commit();

    }
   public void getPreviousData(Context context) {
        SharedPreferences potsSaved = PreferenceManager.getDefaultSharedPreferences(context);
        int sizeOfList = potsSaved.getInt(LIST_SIZE, 0);
        int index=0;
        while( index < sizeOfList) {
            String name = potsSaved.getString(NAME_POTS + index, "g");
            int weight = potsSaved.getInt(POTSTOBESAVED + index, 0);
            Pot previousPot=new Pot(name,weight);
            myPots.addPot(previousPot);
            index++;

        }
        populateListView();

    }

}
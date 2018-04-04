package com.example.jianingsun.servingsizecalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends AppCompatActivity {

    public static final String POTCLICKED_NAME = "Potclicked Name";
    public static final String POTCLICKED_WEIGHT = "Potclicked Weight";
    public static final int DEFAULT_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        setupBackBtn();
        setupPotName();
        setupPotWeight();
        setupFoodWeight();
        setupServingWeight();

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
        if(itemId==R.id.backAction)
            finish();
        return super.onOptionsItemSelected(item);

    }

    private void setupServingWeight() {

        EditText serving = (EditText) findViewById(R.id.editServings);

        serving.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Intent intent = getIntent();
                EditText serving = (EditText) findViewById(R.id.editServings);
                String servings = serving.getText().toString();
                if(servings.isEmpty()||servings.startsWith("-")||servings.startsWith("0")){
                    Toast.makeText(CalculateActivity.this,"Please enter a positive integer",Toast.LENGTH_SHORT)
                            .show();
                    setupServingWeight();
                }
                else {
                    int numOfServings = Integer.parseInt(servings);
                    setupFoodWeight();
                    EditText withFood = (EditText) findViewById(R.id.editWeight);
                    String weightWithFood = withFood.getText().toString();

                    int totalWeight = Integer.parseInt(weightWithFood);
                    int weight = getNewPotWeight(intent);
                    int foodweight = totalWeight - weight;
                    if (foodweight >= 0) {
                        int servingWeight = foodweight / numOfServings;
                        TextView textview = (TextView) findViewById(R.id.weightPerServing);
                        textview.setText(" " + servingWeight);
                    }
                }

            }
        });
    }

    private void setupFoodWeight() {
        EditText withFood=(EditText)findViewById(R.id.editWeight);
        withFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Intent intent=getIntent();
                EditText withFood=(EditText)findViewById(R.id.editWeight);
                String weightWithFood=withFood.getText().toString();

                if(weightWithFood.isEmpty()||weightWithFood.startsWith("-")||weightWithFood.startsWith("0")){
                    Toast.makeText(CalculateActivity.this,"Please enter a positive integer",Toast.LENGTH_SHORT)
                            .show();
                    setupFoodWeight();
                }
                else{
                    int totalWeight = Integer.parseInt(weightWithFood);
                    int weight = getNewPotWeight(intent);
                    int foodweight = totalWeight - weight;
                    TextView textView = (TextView) findViewById(R.id.foodWeight);
                    textView.setText(" " + foodweight);
                    EditText serving= (EditText)findViewById(R.id.editServings);
                    String servings=serving.getText().toString();
                    if(!servings.equals("")) {
                        int numOfServings=Integer.parseInt(servings);
                        if(foodweight>=0){
                        int servingWeight = foodweight / numOfServings;
                        TextView text = (TextView) findViewById(R.id.weightPerServing);
                        text.setText(" " + servingWeight);
                        }
                    }
                }
            }
        });
  
    }
    private void setupPotWeight() {
        Intent intent=getIntent();
        int weight=getNewPotWeight(intent);
        TextView weightText=(TextView)findViewById(R.id.PotWeight);
        weightText.setText(" "+weight);
    }

    private void setupPotName() {
        Intent intent=getIntent();
        String name=getNewPotName(intent);
        TextView nameText=(TextView)findViewById(R.id.PotName);
        nameText.setText(name);
    }

    private void setupBackBtn() {
        Button btn=(Button)findViewById(R.id.backbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static Intent makeLaunchIntent(Context context,Pot pot){
        Intent intent=new Intent(context,CalculateActivity.class);
        intent.putExtra(POTCLICKED_NAME,pot.getName());
        intent.putExtra(POTCLICKED_WEIGHT,pot.getWeightInG());
        return intent;
    }
    public static String getNewPotName(Intent intent){
        return intent.getStringExtra(POTCLICKED_NAME);
    }
    public static int getNewPotWeight(Intent intent){
        return intent.getIntExtra(POTCLICKED_WEIGHT, DEFAULT_VALUE);
    }
}


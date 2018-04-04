package com.example.jianingsun.servingsizecalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    public static final String NAME = "Name";
    public static final String WEIGHT = "Weight";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setOnEndActivity();
        setOnOkBtn();
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
        if(itemId==R.id.cancelAction){
            finish();
        }

        else if(itemId==R.id.OkAction){ EditText potsNewName=(EditText)findViewById(R.id.editPotName);
            String NewName=potsNewName.getText().toString();
            EditText potsNewWeight=(EditText)findViewById(R.id.editPotWeight);
            String NewWeight=potsNewWeight.getText().toString();
            //error checking
            boolean valid=true;
            if(NewName.length()<=1){
                Toast.makeText(SecondActivity.this,"Please enter a name longer than one character",Toast.LENGTH_SHORT)
                        .show();
                valid=false;
                //setOnOkBtn();
            }
            if(NewWeight.isEmpty()||NewWeight.startsWith("-")){
                Toast.makeText(SecondActivity.this,"Please enter a positive integer",Toast.LENGTH_SHORT)
                        .show();
                valid=false;
                // setOnOkBtn();
            }
            //Passing data back
            if(valid) {
                Intent intent = new Intent();
                intent.putExtra(NAME, NewName);
                intent.putExtra(WEIGHT, NewWeight);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);

    }

    private void setOnOkBtn() {
        Button btn=(Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract the data from the UI
                EditText potsNewName=(EditText)findViewById(R.id.editPotName);
                String NewName=potsNewName.getText().toString();
                EditText potsNewWeight=(EditText)findViewById(R.id.editPotWeight);
                String NewWeight=potsNewWeight.getText().toString();
                //error checking
                boolean valid=true;
                if(NewName.length()<=1){
                    Toast.makeText(SecondActivity.this,"Please enter a name longer than one character",Toast.LENGTH_SHORT)
                            .show();
                    valid=false;
                    //setOnOkBtn();
                }
                if(NewWeight.isEmpty()||NewWeight.startsWith("-")){
                    Toast.makeText(SecondActivity.this,"Please enter a positive integer",Toast.LENGTH_SHORT)
                            .show();
                    valid=false;
                   // setOnOkBtn();
                }
                //Passing data back
                if(valid) {
                    Intent intent = new Intent();
                    intent.putExtra(NAME, NewName);
                    intent.putExtra(WEIGHT, NewWeight);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void setOnEndActivity() {
        Button btn=(Button)findViewById(R.id.endActivity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent MakeIntent(Context context){

        return new Intent(context,SecondActivity.class);

    }

    public static String getNewPotName(Intent intent){
        return intent.getStringExtra(NAME);
    }
    public  static String getNewPotWeight(Intent intent){
       return intent.getStringExtra(WEIGHT);
    }


}



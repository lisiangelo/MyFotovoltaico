package it.android.j940549.fotovoltaico_material_design;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashActivity extends AppCompatActivity {
    //LinearLayout image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
      //  image= (LinearLayout) findViewById(R.id.splash);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //cassaforteaperta.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent vaiaHome =new Intent(getBaseContext(), HomeActivityNav.class);
                startActivity(vaiaHome);
                finish();
            }
        },2000);


    }

}

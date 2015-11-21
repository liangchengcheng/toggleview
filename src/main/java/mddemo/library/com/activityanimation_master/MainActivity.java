package mddemo.library.com.activityanimation_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SwitchView switchView= (SwitchView) findViewById(R.id.ll_ok);
        switchView.setOnSwitchChangedListener(new SwitchView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChanged(View view, int value) {
                StringBuffer msg = new StringBuffer("当前状态为:");
                if(value == switchView.OFF)
                    msg.append("关");
                else
                    msg.append("开");
                //弹出提示框
                Toast toast = Toast.makeText(MainActivity.this, msg.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 80);
                toast.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

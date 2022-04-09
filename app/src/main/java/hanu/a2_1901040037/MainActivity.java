package hanu.a2_1901040037;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hanu.a2_1901040037.asyntask.GetProductsAsyncTask;

public class MainActivity extends AppCompatActivity {

    // replace onActivityForResult ( Deprecated)
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> { });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        Changing the StrictMode really just tells the app -
             do not remind me of me writing bad code.
             It can work for debugging or in some cases
             where you know what you are doing but generally it is not the way to go.
         Refer to <a href="https://stackoverflow.com/questions/22395417/error-strictmodeandroidblockguardpolicy-onnetwork"></a>
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new GetProductsAsyncTask(MainActivity.this).execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCard:
                findViewById(R.id.btnCard).setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                    activityResultLauncher.launch(intent);
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
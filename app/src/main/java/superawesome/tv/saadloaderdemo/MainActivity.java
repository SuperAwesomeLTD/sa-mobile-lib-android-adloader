package superawesome.tv.saadloaderdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.logging.Logger;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.saadloader.SALoaderInterface;
import tv.superawesome.lib.samodelspace.saad.SAResponse;
import tv.superawesome.lib.sasession.SASession;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

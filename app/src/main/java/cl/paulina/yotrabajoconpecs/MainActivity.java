package cl.paulina.yotrabajoconpecs;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView btnPanelPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion);
        btnPanelPrincipal = findViewById(R.id.to_pdc);
        getSupportActionBar().hide();
    }

    public void menu_principal(View view){
        startActivity(new Intent(getApplicationContext(), menu_lateral.class));
    }
}

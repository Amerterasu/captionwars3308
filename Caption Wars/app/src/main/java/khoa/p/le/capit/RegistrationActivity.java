package khoa.p.le.capit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final String TAG = "REGISTRATION_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        final EditText email = (EditText) findViewById(R.id.email_editView);
        final EditText password = (EditText) findViewById(R.id.password_editView);
        final EditText password_confirm = (EditText) findViewById(R.id.confirm_editView);
        Button registerButton = (Button) findViewById(R.id.signup_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordConfrim = password_confirm.getText().toString();

                if(emailStr != null && passwordStr != null && passwordConfrim != null){
                    if(passwordStr.equals(passwordConfrim)){
                        auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Log.v(TAG, "Created User");
                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                    finish();
                                }
                                else
                                    Log.e(TAG, "Sign In Failed");
                            }
                        });
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistrationActivity.this, "Email/Password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

package shantanu.passbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etPassword;
    private EditText etEmail;
    private Button bLogin;
    private Button bRegister;
    private Button bForgot;
    private TextView tvError;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                startLogin();
            }
        });

        bForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                intent.putExtra("email", etEmail.getText().toString().trim());
                startActivity(intent);
            }
        });

    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bForgot = (Button) findViewById(R.id.bForgot);
        bRegister = (Button) findViewById(R.id.bRegister);
        tvError = (TextView) findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void startLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            tvError.setText("Fields cannot be blank!!!");
            tvError.setVisibility(View.VISIBLE);
        } else {
            progressDialog.setMessage("Verifying Credentials");
            progressDialog.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        String TAG = "AUTHENTICATION";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                tvError.setText(task.getException().getMessage());
                                tvError.setVisibility(View.VISIBLE);
                            }
                        }


                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: CALLED");
    }
}

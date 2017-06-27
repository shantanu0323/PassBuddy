package shantanu.passbuddy;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";

    private EditText etEmail;
    private Button bReset;
    private ProgressDialog progressDialog;
    private TextView tvError;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvError.setVisibility(View.GONE);
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Sending Password Reset Email");
                    progressDialog.show();

                    try {
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                            tvError.setText("We have sent you instructions to reset your password!!!");
                                            tvError.setVisibility(View.VISIBLE);
                                            tvError.setTextColor(Color.parseColor("#0d8"));
                                        } else {
                                            tvError.setText("Failed to send email due to : " + task.getException().getMessage());
                                            tvError.setVisibility(View.VISIBLE);
                                            tvError.setTextColor(Color.parseColor("#d00"));
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: SENDING ERROR : ", e);
                    }
                }
            }
        });
    }

    private void init() {
        String email = getIntent().getStringExtra("email");
        etEmail = (EditText) findViewById(R.id.etEmail);
        if (email != null) {
            etEmail.setText(email);
        }
        bReset = (Button) findViewById(R.id.bReset);
        tvError = (TextView) findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
    }
}

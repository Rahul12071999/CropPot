package com.rahul.farmerproject;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth fa;
    FirebaseAuth.AuthStateListener authState;
    EditText email,pass;
    String Email,Pass;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fa = FirebaseAuth.getInstance();
        email = findViewById(R.id.em);
        pass = findViewById(R.id.pass);

    }

    public void loginClick(View view){
        Email=email.getText().toString().trim();
        Pass=pass.getText().toString().trim();
        System.out.println(Email+" "+Pass);
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(LoginActivity.this,"Enter your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Pass)){
            Toast.makeText(LoginActivity.this,"Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }
        fa.signInWithEmailAndPassword(Email,Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //FirebaseUser user=fa.getCurrentUser();
                            Intent it=new Intent(LoginActivity.this,FarmerDashboard.class);
                            startActivity(it);
                        }
                        else
                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
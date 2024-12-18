package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<String> items;
    private EditText editText;
    private Button addItemButton;
    private ImageView profileButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        boolean loginSuccess = getIntent().getBooleanExtra("login_success", false);
        if (loginSuccess) {
            Toast.makeText(this, "Login Successful. Welcome to TaskMaster!", Toast.LENGTH_LONG).show();
        }

        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.list);
        editText = findViewById(R.id.edit_text);
        addItemButton = findViewById(R.id.button);
        profileButton = findViewById(R.id.profileButton);

        items = new ArrayList<>();
        customAdapter = new CustomAdapter(this, items);
        listView.setAdapter(customAdapter);

        addItemButton.setOnClickListener(v -> {
            String newItem = editText.getText().toString();
            if (!newItem.isEmpty()) {
                items.add(newItem);
                customAdapter.notifyDataSetChanged();
                editText.setText("");
            } else {
                Toast.makeText(TodoListActivity.this, "Please enter an item", Toast.LENGTH_SHORT).show();
            }
        });

        profileButton.setOnClickListener(this::showProfilePopup);
    }

    private void showProfilePopup(View view) {
        PopupMenu popupMenu = new PopupMenu(TodoListActivity.this, view);
        getMenuInflater().inflate(R.menu.profile_popup_menu, popupMenu.getMenu());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName() != null ? user.getDisplayName() : "User";

            MenuItem nameItem = popupMenu.getMenu().add(0, 12346, Menu.NONE, name);
            nameItem.setEnabled(true);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                logout();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void logout() {
        mAuth.signOut();

        Toast.makeText(TodoListActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(TodoListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

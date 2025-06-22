package com.example.contactmanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText, noteEditText;
    private Button saveBtn;
    private Spinner tagSpinner;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        noteEditText = findViewById(R.id.noteEditText);
        saveBtn = findViewById(R.id.saveBtn);
        tagSpinner = findViewById(R.id.tagSpinner);

        db = AppDatabase.getInstance(this);

        // Настройка спиннера
        String[] tags = {"Без категории", "Работа", "Семья", "Друзья"};
        ArrayAdapter<String> adapterTags = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        adapterTags.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapterTags);

        saveBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String note = noteEditText.getText().toString().trim();
            String tag = tagSpinner.getSelectedItem().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Имя и телефон обязательны", Toast.LENGTH_SHORT).show();
                return;
            }

            Contact contact = new Contact();
            contact.name = name;
            contact.phone = phone;
            contact.email = email;
            contact.note = note;
            contact.tag = tag;

            db.contactDao().insert(contact);
            Toast.makeText(this, "Контакт сохранён", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

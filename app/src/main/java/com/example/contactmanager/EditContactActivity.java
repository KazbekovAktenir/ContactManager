package com.example.contactmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class EditContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText, noteEditText;
    private Button updateBtn, callBtn, smsBtn;
    private Spinner tagSpinner;
    private AppDatabase db;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        noteEditText = findViewById(R.id.noteEditText);
        updateBtn = findViewById(R.id.updateBtn);
        callBtn = findViewById(R.id.callBtn);
        smsBtn = findViewById(R.id.smsBtn);
        tagSpinner = findViewById(R.id.tagSpinner);

        db = AppDatabase.getInstance(this);

        // Настройка спиннера
        String[] tags = {"Без категории", "Работа", "Семья", "Друзья"};
        ArrayAdapter<String> adapterTags = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        adapterTags.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapterTags);

        // Получаем контакт из интента
        contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact == null) {
            finish();
            return;
        }

        // Устанавливаем данные
        nameEditText.setText(contact.name);
        phoneEditText.setText(contact.phone);
        emailEditText.setText(contact.email);
        noteEditText.setText(contact.note);

        // Устанавливаем текущий тег
        int tagIndex = Arrays.asList(tags).indexOf(contact.tag);
        tagSpinner.setSelection(tagIndex >= 0 ? tagIndex : 0);

        updateBtn.setOnClickListener(v -> {
            contact.name = nameEditText.getText().toString().trim();
            contact.phone = phoneEditText.getText().toString().trim();
            contact.email = emailEditText.getText().toString().trim();
            contact.note = noteEditText.getText().toString().trim();
            contact.tag = tagSpinner.getSelectedItem().toString();

            db.contactDao().update(contact);
            Toast.makeText(this, "Контакт обновлён", Toast.LENGTH_SHORT).show();
            finish();
        });

        callBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.phone));
            startActivity(intent);
        });

        smsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("smsto:" + contact.phone));
            intent.putExtra("sms_body", "Привет, " + contact.name + "!");
            startActivity(intent);
        });
    }
}

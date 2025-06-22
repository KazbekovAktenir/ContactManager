package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private AppDatabase db;
    private EditText searchEditText;
    private Button addContactBtn;
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        addContactBtn = findViewById(R.id.addContactBtn);
        filterSpinner = findViewById(R.id.filterSpinner); // 👈 Не забудь объявить эту переменную вверху

        // Инициализация RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Contact> allContacts = db.contactDao().getAll();
        adapter = new ContactAdapter(allContacts, this, db);
        recyclerView.setAdapter(adapter);

        // Поиск
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Contact> filtered = db.contactDao().search(s.toString());
                adapter.setList(filtered);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Добавление контакта
        addContactBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
        });

        // Фильтрация по категории
        String[] tags = {"Все", "Работа", "Семья", "Друзья"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = tags[position];
                if (selectedTag.equals("Все")) {
                    adapter.setList(db.contactDao().getAll());
                } else {
                    adapter.setList(db.contactDao().getByTag(selectedTag));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    // Обновление списка при возвращении
    @Override
    protected void onResume() {
        super.onResume();
        List<Contact> updatedContacts = db.contactDao().getAll();
        adapter.setList(updatedContacts);
    }

    // Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_az) {
            adapter.setList(db.contactDao().getAllSortedAZ());
            return true;

        } else if (id == R.id.sort_za) {
            adapter.setList(db.contactDao().getAllSortedZA());
            return true;

        } else if (id == R.id.delete_all) {
            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение")
                    .setMessage("Удалить все контакты?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        db.contactDao().deleteAll();
                        adapter.setList(db.contactDao().getAll());
                        Toast.makeText(this, "Все контакты удалены", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
            return true;

        } else if (id == R.id.filter_all) {
            adapter.setList(db.contactDao().getAll());
            return true;

        } else if (id == R.id.filter_work) {
            adapter.setList(db.contactDao().getByTag("Работа"));
            return true;

        } else if (id == R.id.filter_family) {
            adapter.setList(db.contactDao().getByTag("Семья"));
            return true;

        } else if (id == R.id.filter_friends) {
            adapter.setList(db.contactDao().getByTag("Друзья"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

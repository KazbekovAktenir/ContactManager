package com.example.contactmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private AppDatabase db;
    private Context context;

    public ContactAdapter(List<Contact> contactList, Context context, AppDatabase db) {
        this.contactList = contactList;
        this.context = context;
        this.db = db;
    }

    public void setList(List<Contact> updatedList) {
        this.contactList = updatedList;
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, emailTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);

            // Долгое нажатие на элемент для удаления
            itemView.setOnLongClickListener(v -> {
                Contact contact = contactList.get(getAdapterPosition());

                new AlertDialog.Builder(context)
                        .setTitle("Удаление")
                        .setMessage("Удалить контакт " + contact.name + "?")
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            db.contactDao().delete(contact);
                            contactList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(context, "Контакт удалён", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
                return true;
            });
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.name);
        holder.phoneTextView.setText(contact.phone);
        holder.emailTextView.setText(contact.email);

        // Обработка обычного клика — редактирование
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("contact", contact);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

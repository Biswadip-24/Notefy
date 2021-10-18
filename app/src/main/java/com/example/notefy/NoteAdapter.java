package com.example.notefy;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>
{

    private Context mContext;
    private List<Note> mNotes;
    private OnItemClickListener mListener;

    public NoteAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    private String cropTitle(String t){
        String ns = "";
        for(int i = 0; i < t.length(); i++){
            if(ns.length() == 20){
                ns += "...";
                break;
            }
            ns = ns + t.charAt(i);
        }
        return ns;
    }

    private String cropBody(String b){
        String ns = "";
        for(int i = 0; i < b.length(); i++){
            if(ns.length() == 250){
                ns += "...";
                break;
            }
            ns = ns + b.charAt(i);
        }
        return ns;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent , false);
        return new NoteViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position)
    {
        String Note_title = cropTitle(mNotes.get(position).getTitle());
        String Note_body = cropBody(mNotes.get(position).getBody());
        holder.title.setText(Note_title);
        holder.body.setText(Note_body);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void filterList(ArrayList<Note> filteredList) {
        mNotes = filteredList;
        notifyDataSetChanged();
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView title;
        TextView body;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView_title);
            body = itemView.findViewById(R.id.textView_body);

            int[] note_colors = mContext.getResources().getIntArray(R.array.notes);
            int index = (int)(Math.random() * (note_colors.length));

            CardView cardView = itemView.findViewById(R.id.CardView_item);
            cardView.setCardBackgroundColor(note_colors[index]);

            title.setTextColor(mContext.getResources().getColor(R.color.black));
            body.setTextColor(mContext.getResources().getColor(R.color.black));

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(mNotes.get(position));
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem edit = contextMenu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mListener != null){
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1 :
                            mListener.onItemClick(mNotes.get(position));
                            return true;
                        case 2 :
                            mListener.onDeleteClick(mNotes.get(position).getKey());
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
        void onDeleteClick(String key);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}

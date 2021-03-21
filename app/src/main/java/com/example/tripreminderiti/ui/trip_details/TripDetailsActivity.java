package com.example.tripreminderiti.ui.trip_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderiti.R;
import com.example.tripreminderiti.database.TripDatabase;
import com.example.tripreminderiti.database.note.Note;
import com.example.tripreminderiti.database.trip.Trip;
import com.example.tripreminderiti.ui.upcoming_trips.UpcomingTripsFragment;
import com.google.android.material.textfield.TextInputLayout;

public class TripDetailsActivity extends AppCompatActivity {

    private TextInputLayout nameEd;
    private TextInputLayout startEd;
    private TextInputLayout endEd;
    private TextInputLayout dateEd;
    private TextInputLayout timeEd;
    private Button editBtn;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private boolean isEditable = false;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        nameEd = findViewById(R.id.details_ed_name);
        startEd = findViewById(R.id.details_ed_startPoint);
        endEd = findViewById(R.id.details_ed_endPoint);
        dateEd = findViewById(R.id.details_ed_Date);
        timeEd = findViewById(R.id.details_ed_Time);
        editBtn = findViewById(R.id.details_btn_edit);

        notesRecyclerView = findViewById(R.id.details_rv_note);
        notesAdapter = new NotesAdapter();
        notesRecyclerView.setAdapter(notesAdapter);

        currentTrip = (Trip) getIntent().getSerializableExtra(UpcomingTripsFragment.UPCOMING_DETAILS_EXTRA);
        nameEd.getEditText().setText(currentTrip.getName());
        startEd.getEditText().setText(currentTrip.getStartPoint());
        endEd.getEditText().setText(currentTrip.getEndPoint());
        dateEd.getEditText().setText(currentTrip.getDate());
        timeEd.getEditText().setText(currentTrip.getTime());

        notesAdapter.changeData(TripDatabase.getInstance(this).noteDao().getNotes(currentTrip.getId()));

        notesAdapter.setNoteDeleteClickListener = new NotesAdapter.NoteDeleteClickListener() {
            @Override
            public void onClick(Note note) {
                TripDatabase.getInstance(TripDetailsActivity.this).noteDao().delete(note);
                notesAdapter.changeData(TripDatabase.getInstance(TripDetailsActivity.this).noteDao().getNotes(currentTrip.getId()));
            }
        };

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    changeBehaviour(isEditable);
                    isEditable = true;
                } else {
                    changeBehaviour(isEditable);
                    Trip trip = new Trip(currentTrip.getId(), nameEd.getEditText().getText().toString(), startEd.getEditText().getText().toString(),
                            endEd.getEditText().getText().toString(), dateEd.getEditText().getText().toString(),
                            timeEd.getEditText().getText().toString());
                    TripDatabase.getInstance(TripDetailsActivity.this).tripDao().updateTrip(trip.getName(), trip.getStartPoint(), trip.getEndPoint(), trip.getDate(), trip.getTime(), trip.getId());
                    isEditable = false;
                }
            }
        });


    }

    public void changeBehaviour(Boolean isEditable) {
        if (!isEditable) {
            nameEd.getEditText().setEnabled(true);
            startEd.getEditText().setEnabled(true);
            endEd.getEditText().setEnabled(true);
            dateEd.getEditText().setEnabled(true);
            timeEd.getEditText().setEnabled(true);
            editBtn.setText("update");
        } else {
            nameEd.getEditText().setEnabled(false);
            startEd.getEditText().setEnabled(false);
            endEd.getEditText().setEnabled(false);
            dateEd.getEditText().setEnabled(false);
            timeEd.getEditText().setEnabled(false);
            editBtn.setText("edit");
        }
    }
}
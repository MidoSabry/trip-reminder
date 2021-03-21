package com.example.tripreminderiti.ui.upcoming_trips;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderiti.AddTripActivity;
import com.example.tripreminderiti.R;
import com.example.tripreminderiti.database.TripDatabase;
import com.example.tripreminderiti.database.note.Note;
import com.example.tripreminderiti.database.trip.Trip;
import com.example.tripreminderiti.ui.trip_details.TripDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class UpcomingTripsFragment extends Fragment {

    public static final String UPCOMING_DETAILS_EXTRA = "UPCOMING_DETAILS_EXTRA";
    private FloatingActionButton addBtn;
    private List<Trip> tripsData;
    private UpcomingTripAdapter upcomingTripAdapter;
    private RecyclerView recyclerView;
    private AlertDialog addNoteDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        addBtn = root.findViewById(R.id.home_btn_addTrip);
        recyclerView = root.findViewById(R.id.home_rv_trips);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        upcomingTripAdapter = new UpcomingTripAdapter();
        recyclerView.setAdapter(upcomingTripAdapter);
        tripsData = TripDatabase.getInstance(getActivity()).tripDao().getAll();
        upcomingTripAdapter.changeData(tripsData);
        upcomingTripAdapter.setAddNoteClickListener = new UpcomingTripAdapter.AddNoteClickListener() {
            @Override
            public void onClick(Trip trip) {
                Toast.makeText(getActivity(), trip.getName(), Toast.LENGTH_SHORT).show();
                showAddNoteDialog(trip.getId());
            }
        };

        upcomingTripAdapter.setTripClickListener = new UpcomingTripAdapter.TripClickListener() {
            @Override
            public void onClick(Trip trip) {
                Intent intent = new Intent(getActivity(), TripDetailsActivity.class);
                intent.putExtra(UPCOMING_DETAILS_EXTRA, trip);
                startActivity(intent);
            }
        };

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTripActivity.class));
            }
        });

    }

    public void showAddNoteDialog(int id) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.dialog_add_note, null);
        addNoteDialog = new AlertDialog.Builder(getActivity()).create();
        addNoteDialog.setView(view);
        TextInputLayout title = view.findViewById(R.id.ed_note_ttle);
        TextInputLayout description = view.findViewById(R.id.ed_note_disc);
        view.findViewById(R.id.btn_add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "" + title.getEditText().getText(), Toast.LENGTH_SHORT).show();
                TripDatabase.getInstance(getActivity()).noteDao().insertNote(new Note(id, title.getEditText().getText().toString(), description.getEditText().getText().toString()));
                addNoteDialog.dismiss();
            }
        });


        addNoteDialog.show();
    }
}
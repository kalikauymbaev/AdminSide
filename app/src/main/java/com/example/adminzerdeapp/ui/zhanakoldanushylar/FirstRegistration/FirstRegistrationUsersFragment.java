package com.example.adminzerdeapp.ui.zhanakoldanushylar.FirstRegistration;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.adapters.FirstUsersListAdapter;
import com.example.adminzerdeapp.modules.Users;
import com.example.adminzerdeapp.ui.zhanakoldanushylar.ZhanaKoldanushylarFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FirstRegistrationUsersFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Users> usersArrayList;
    FirstUsersListAdapter firstUsersListAdapter;
    ProgressDialog progressDialog;

    DatabaseReference mDatabaseReference;
    Query query;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_registration_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerFirst);
        usersArrayList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        query = mDatabaseReference.orderByChild("ruqsat").equalTo("no");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();

        firstUsersListAdapter = new FirstUsersListAdapter(usersArrayList, getContext(), mDatabaseReference);
        recyclerView.setAdapter(firstUsersListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void getData() {
        progressDialog.show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

//                    Bundle bundle = new Bundle();
//                    bundle.putString("quantity", String.valueOf(snapshot.getChildrenCount()));
//
//                    ZhanaKoldanushylarFragment fragobj = new ZhanaKoldanushylarFragment();
//                    fragobj.setArguments(bundle);

                    usersArrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Users users = snap.getValue(Users.class);
                        usersArrayList.add(users);
                        progressDialog.cancel();
                    }
                    firstUsersListAdapter.notifyDataSetChanged();
                }else {
//                    Toast.makeText(getActivity(), "Мәліметтер жоқ1", Toast.LENGTH_SHORT).show();
                    usersArrayList.clear();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
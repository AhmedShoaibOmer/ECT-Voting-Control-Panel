package com.aso.ectvotingcotrolpanel.data.candidate;

import com.aso.ectvotingcotrolpanel.core.exception.NetworkErrorException;
import com.aso.ectvotingcotrolpanel.data.Result;
import com.aso.ectvotingcotrolpanel.data.ResultCallback;
import com.aso.ectvotingcotrolpanel.data.models.Candidate;
import com.aso.ectvotingcotrolpanel.utils.Logger;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CandidatesDataSource {
    private static final Logger LOGGER = new Logger();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getCandidates(ResultCallback<List<Candidate>> callback) {
        final CollectionReference colRef = db.collection("candidates");
        colRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                callback.onComplete(new Result.Error<>(error));
                return;
            }
            ArrayList<Candidate> list = new ArrayList<>();
            if (value != null && !value.isEmpty()) {
                LOGGER.d("Candidates List Size : " + value.getDocuments().size());
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    if(snapshot.exists()){
                        Map<String, Object> data = new HashMap<>(Objects.requireNonNull(snapshot.getData()));
                        LOGGER.d("Candidate Map : " + data);
                        if(data.containsKey("fullName") && data.containsKey("numOfVoters")) {
                            data.put("id", snapshot.getId());
                            list.add(Candidate.fromMap(data));
                            LOGGER.d("Candidate Map : " + data);
                        } else {
                            callback.onComplete(new Result.Error<>(new IOException()));
                        }
                    }
                }
            }
            callback.onComplete(new Result.Success<>(list));
        });
    }

    public void addCandidate(String fullName, ResultCallback<Candidate> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName);
        data.put("numOfVoters0", 0);
        db.collection("candidates").add(data).addOnSuccessListener(documentReference -> {
            callback.onComplete(new Result.Success<>(new Candidate(documentReference.getId(), fullName, 0)));
        })
        .addOnFailureListener(e -> {
            if(e instanceof FirebaseNetworkException){
                callback.onComplete(new Result.Error<>(new NetworkErrorException()));
            } else {
                Objects.requireNonNull(e).printStackTrace();
                callback.onComplete(new Result.Error<>(new IOException("Error Creating new candidate", e)));
            }
        });
    }

    public void removeCandidate(String id, ResultCallback<Void> callback) {
        db.collection("candidates").document(id).delete().addOnSuccessListener(v -> {
            callback.onComplete(new Result.Success<>(v));
        })
                .addOnFailureListener(e -> {
                    if(e instanceof FirebaseNetworkException){
                        callback.onComplete(new Result.Error<>(new NetworkErrorException()));
                    } else {
                        Objects.requireNonNull(e).printStackTrace();
                        callback.onComplete(new Result.Error<>(new IOException("Error Creating new user", e)));
                    }
                });
    }
}

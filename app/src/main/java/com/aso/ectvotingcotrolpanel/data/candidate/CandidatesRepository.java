package com.aso.ectvotingcotrolpanel.data.candidate;

import com.aso.ectvotingcotrolpanel.data.ResultCallback;
import com.aso.ectvotingcotrolpanel.data.models.Candidate;

import java.util.List;

public class CandidatesRepository {
    private static volatile CandidatesRepository instance;

    private final CandidatesDataSource dataSource;

    // private constructor : singleton access
    private CandidatesRepository(CandidatesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CandidatesRepository getInstance(CandidatesDataSource dataSource) {
        if (instance == null) {
            instance = new CandidatesRepository(dataSource);
        }
        return instance;
    }

    public void getAllCandidates(ResultCallback<List<Candidate>> callback) {
        dataSource.getCandidates(callback);
    }

    public void addCandidate(String fullName, ResultCallback<Candidate> callback) {
        dataSource.addCandidate(fullName, callback);
    }

    public void removeCandidate(String id, ResultCallback<Void> callback) {
        dataSource.removeCandidate(id, callback);
    }

}

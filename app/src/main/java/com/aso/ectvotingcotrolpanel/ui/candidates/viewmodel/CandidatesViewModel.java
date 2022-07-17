package com.aso.ectvotingcotrolpanel.ui.candidates.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aso.ectvotingcotrolpanel.R;
import com.aso.ectvotingcotrolpanel.core.exception.NetworkErrorException;
import com.aso.ectvotingcotrolpanel.data.Result;
import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationRepository;
import com.aso.ectvotingcotrolpanel.data.candidate.CandidatesRepository;
import com.aso.ectvotingcotrolpanel.data.models.Admin;
import com.aso.ectvotingcotrolpanel.data.models.Candidate;
import com.aso.ectvotingcotrolpanel.ui.candidates.AddCandidateResult;
import com.aso.ectvotingcotrolpanel.ui.candidates.CandidatesResult;
import com.aso.ectvotingcotrolpanel.ui.candidates.LogoutResult;
import com.aso.ectvotingcotrolpanel.ui.candidates.RemoveCandidateResult;

import java.util.List;

public class CandidatesViewModel extends ViewModel {

    private final MutableLiveData<CandidatesResult> candidatesResult = new MutableLiveData<>();
    private final MutableLiveData<AddCandidateResult> addCandidateResult = new MutableLiveData<>();
    private final MutableLiveData<RemoveCandidateResult> removeCandidateResult = new MutableLiveData<>();
    private final MutableLiveData<LogoutResult> logoutResult = new MutableLiveData<>();
    private final AuthenticationRepository authenticationRepository;
    private final CandidatesRepository candidatesRepository;
    public boolean isProcessing = false;

    CandidatesViewModel(AuthenticationRepository authenticationRepository, CandidatesRepository candidatesRepository) {
        this.authenticationRepository = authenticationRepository;
        this.candidatesRepository = candidatesRepository;
    }

    public MutableLiveData<CandidatesResult> getCandidatesResult() {
        return candidatesResult;
    }
    public MutableLiveData<AddCandidateResult> getAddCandidateResult() {
        return addCandidateResult;
    }
    public MutableLiveData<RemoveCandidateResult> getRemoveCandidateResult() {
        return removeCandidateResult;
    }
    public LiveData<LogoutResult> getLogoutResult() {
        return logoutResult;
    }

    public Admin getCurrentAdmin() {
        return authenticationRepository.admin;
    }

    public void getCandidates() {
        // can be launched in a separate asynchronous job
        candidatesRepository.getAllCandidates(result -> {
            if (result instanceof Result.Success) {
                List<Candidate> candidates = ((Result.Success<List<Candidate>>) result).getData();
                candidatesResult.setValue(new CandidatesResult(candidates));
            } else {
                if (((Result.Error<List<Candidate>>) result).getError() instanceof NetworkErrorException) {
                    candidatesResult.setValue(new CandidatesResult(R.string.network_error));
                } else {
                    candidatesResult.setValue(new CandidatesResult(R.string.fetching_candidates_failed));
                }
            }
        });
    }

    public void addCandidate(String fullName) {
        // can be launched in a separate asynchronous job
        isProcessing = true;
        candidatesRepository.addCandidate(fullName, result -> {
            if (result instanceof Result.Success) {
                Candidate candidate = ((Result.Success<Candidate>) result).getData();
                addCandidateResult.setValue(new AddCandidateResult(candidate));
                isProcessing = false;
            } else {
                isProcessing = false;
                if (((Result.Error<Candidate>) result).getError() instanceof NetworkErrorException) {
                    candidatesResult.setValue(new CandidatesResult(R.string.network_error));
                } else {
                    candidatesResult.setValue(new CandidatesResult(R.string.add_candidate_failed));
                }
            }
        });
    }

    public void removeCandidate(String id) {
        isProcessing = true;
        // can be launched in a separate asynchronous job
        candidatesRepository.removeCandidate(id, result -> {
            if (result instanceof Result.Success) {
                removeCandidateResult.setValue(new RemoveCandidateResult());
                isProcessing = false;
            } else {
                isProcessing = false;
                if (((Result.Error<Void>) result).getError() instanceof NetworkErrorException) {
                    candidatesResult.setValue(new CandidatesResult(R.string.network_error));
                } else {
                    candidatesResult.setValue(new CandidatesResult(R.string.remove_candidate_failed));
                }
            }
        });
    }

    public void logout() {
        // can be launched in a separate asynchronous job
        authenticationRepository.logout(result -> {
            if (result instanceof Result.Success) {
                logoutResult.setValue(new LogoutResult());
            } else {
                if (((Result.Error<Void>) result).getError() instanceof NetworkErrorException) {
                    logoutResult.setValue(new LogoutResult(R.string.network_error));
                } else {
                    logoutResult.setValue(new LogoutResult(R.string.logout_failed));
                }
            }
        });
    }
}
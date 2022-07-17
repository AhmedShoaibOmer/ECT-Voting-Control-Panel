package com.aso.ectvotingcotrolpanel.ui.candidates.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationDataSource;
import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationRepository;
import com.aso.ectvotingcotrolpanel.data.candidate.CandidatesDataSource;
import com.aso.ectvotingcotrolpanel.data.candidate.CandidatesRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class CandidatesViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CandidatesViewModel.class)) {
            return (T) new CandidatesViewModel(AuthenticationRepository.getInstance(new AuthenticationDataSource()),
                    CandidatesRepository.getInstance(new CandidatesDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
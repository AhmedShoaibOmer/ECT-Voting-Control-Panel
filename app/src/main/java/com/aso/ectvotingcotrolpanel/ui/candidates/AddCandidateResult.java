package com.aso.ectvotingcotrolpanel.ui.candidates;

import androidx.annotation.Nullable;

import com.aso.ectvotingcotrolpanel.data.models.Candidate;


public class AddCandidateResult {
    @Nullable
    private Candidate success;
    @Nullable
    private Integer error;

    public AddCandidateResult(@Nullable Integer error) {
        this.error = error;
    }

    public AddCandidateResult(@Nullable Candidate success) {
        this.success = success;
    }

    @Nullable
    Candidate getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}

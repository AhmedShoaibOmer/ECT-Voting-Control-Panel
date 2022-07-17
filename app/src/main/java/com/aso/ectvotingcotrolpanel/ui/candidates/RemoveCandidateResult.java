package com.aso.ectvotingcotrolpanel.ui.candidates;

import androidx.annotation.Nullable;

public class RemoveCandidateResult {
        private boolean success;
        @Nullable
        private Integer error;

        public RemoveCandidateResult(@Nullable Integer error) {
            this.error = error;
        }

        public RemoveCandidateResult() {
            this.success = true;
        }

        boolean getSuccess() {
            return success;
        }

        @Nullable
        Integer getError() {
            return error;
        }
    }
package com.aso.ectvotingcotrolpanel.ui.candidates;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aso.ectvotingcotrolpanel.R;
import com.aso.ectvotingcotrolpanel.data.models.Admin;
import com.aso.ectvotingcotrolpanel.data.models.Candidate;
import com.aso.ectvotingcotrolpanel.databinding.ActivityCandidatesBinding;
import com.aso.ectvotingcotrolpanel.ui.candidates.viewmodel.CandidatesViewModel;
import com.aso.ectvotingcotrolpanel.ui.candidates.viewmodel.CandidatesViewModelFactory;
import com.aso.ectvotingcotrolpanel.ui.customview.CustomProgressDialog;

import java.util.ArrayList;

public class CandidatesActivity extends AppCompatActivity {

    private Admin currentAdmin;
    private final ArrayList<Candidate> candidates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCandidatesBinding binding = ActivityCandidatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Needed for the loading overlay
        CustomProgressDialog progressDialog = new CustomProgressDialog(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_candidate_detail);
        NavController navController = navHostFragment.getNavController();
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.
                //Builder(navController.getGraph())
                //.build();

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        CandidatesViewModel candidatesViewModel = new ViewModelProvider(this, new CandidatesViewModelFactory())
                .get(CandidatesViewModel.class);

        currentAdmin = candidatesViewModel.getCurrentAdmin();

        String welcome = getString(R.string.welcome) + currentAdmin.getFullName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        candidatesViewModel.getAddCandidateResult().observe(this, addCandidateResult -> {
            if (addCandidateResult == null) {
                return;
            }
            if (addCandidateResult.getError() != null) {
                showOperationFailed(addCandidateResult.getError());
            }
            if (addCandidateResult.getSuccess() != null) {
                showOperationFailed(R.string.add_candidate_success);

                }
                progressDialog.stop();
        });

        candidatesViewModel.getRemoveCandidateResult().observe(this, removeCandidateResult -> {
            if (removeCandidateResult == null) {
                return;
            }
            if (removeCandidateResult.getError() != null) {
                showOperationFailed(removeCandidateResult.getError());
            }
            if (removeCandidateResult.getSuccess()) {
                showOperationFailed(R.string.remove_candidate_success);

                }
                progressDialog.stop();
        });

        candidatesViewModel.getAddCandidateResult().observe(this, addCandidateResult -> {
            if (addCandidateResult == null) {
                return;
            }
            if (addCandidateResult.getError() != null) {
                showOperationFailed(addCandidateResult.getError());
            }
            if (addCandidateResult.getSuccess() != null) {
                showOperationFailed(R.string.remove_candidate_success);

                }
                progressDialog.stop();
        });

        candidatesViewModel.getLogoutResult().observe(this, logoutResult -> {
            if (logoutResult == null) {
                return;
            }
            progressDialog.stop();
            if (logoutResult.getError() != null) {
                showOperationFailed(logoutResult.getError());
            }
            if (logoutResult.getSuccess()) {
                showOperationFailed(R.string.logout_successful);
                finish();
            }
        });


        binding.addCandidateFab.setOnClickListener(view -> {

        });

        binding.logoutBtn.setOnClickListener(view -> {
            progressDialog.start(getString(R.string.logging_out));
            candidatesViewModel.logout();
        });
    }

    private void showOperationFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_candidate_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
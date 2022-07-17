package com.aso.ectvotingcotrolpanel.ui.candidates;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.aso.ectvotingcotrolpanel.R;
import com.aso.ectvotingcotrolpanel.data.models.Candidate;
import com.aso.ectvotingcotrolpanel.databinding.CandidateListContentBinding;
import com.aso.ectvotingcotrolpanel.databinding.FragmentCandidateListBinding;
import com.aso.ectvotingcotrolpanel.ui.candidates.viewmodel.CandidatesViewModel;
import com.aso.ectvotingcotrolpanel.ui.customview.CustomProgressDialog;

import java.util.List;

/**
 * A fragment representing a list of Candidates. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link CandidateDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CandidateListFragment extends Fragment {

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    ViewCompat.OnUnhandledKeyEventListenerCompat unhandledKeyEventListenerCompat = (v, event) -> {
        if (event.getKeyCode() == KeyEvent.KEYCODE_Z && event.isCtrlPressed()) {
            Toast.makeText(
                    v.getContext(),
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
            ).show();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F && event.isCtrlPressed()) {
            Toast.makeText(
                    v.getContext(),
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
            ).show();
            return true;
        }
        return false;
    };

    private FragmentCandidateListBinding binding;
    private CandidatesViewModel candidatesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCandidateListBinding.inflate(inflater, container, false);
        candidatesViewModel = new ViewModelProvider(requireActivity())
                .get(CandidatesViewModel.class);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat);

        RecyclerView recyclerView = binding.candidateList;
        CustomProgressDialog progressDialog = new CustomProgressDialog(requireActivity());

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        View itemDetailFragmentContainer = view.findViewById(R.id.candidate_detail_nav_container);
        progressDialog.start("Loading list of Candidates....");
        candidatesViewModel.getCandidatesResult().observe(getViewLifecycleOwner(), candidatesResult -> {
            if (candidatesResult == null) {
                return;
            }
            if (!candidatesViewModel.isProcessing) {
                progressDialog.stop();
            }
            if (candidatesResult.getError() != null) {
                showOperationFailed(candidatesResult.getError(), view);
            }
            if (candidatesResult.getSuccess() != null) {
                setupRecyclerView(recyclerView, candidatesResult.getSuccess(), itemDetailFragmentContainer);
            }
        });

        candidatesViewModel.getCandidates();
    }

    private void setupRecyclerView(
            RecyclerView recyclerView,
            List<Candidate> candidates,
            View itemDetailFragmentContainer
    ) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
                candidates,
                itemDetailFragmentContainer
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showOperationFailed(@StringRes Integer error, View v) {
        Toast.makeText(v.getContext(), error, Toast.LENGTH_SHORT).show();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Candidate> mValues;
        private final View mItemDetailFragmentContainer;

        SimpleItemRecyclerViewAdapter(List<Candidate> items,
                                      View itemDetailFragmentContainer) {
            mValues = items;
            mItemDetailFragmentContainer = itemDetailFragmentContainer;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            CandidateListContentBinding binding =
                    CandidateListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getFullName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(itemView -> {
                Candidate candidate =
                        (Candidate) itemView.getTag();
                Bundle arguments = new Bundle();
                arguments.putString(CandidateDetailFragment.ARG_ITEM_ID, candidate.getId());
                if (mItemDetailFragmentContainer != null) {
                    Navigation.findNavController(mItemDetailFragmentContainer)
                            .navigate(R.id.fragment_candidate_detail, arguments);
                } else {
                    Navigation.findNavController(itemView).navigate(R.id.show_candidate_detail, arguments);
                }
            });
            /*
             * Context click listener to handle Right click events
             * from mice and trackpad input to provide a more native
             * experience on larger screen devices
             */
            holder.itemView.setOnContextClickListener(v -> {
                Candidate candidate =
                        (Candidate) holder.itemView.getTag();
                Toast.makeText(
                        holder.itemView.getContext(),
                        "Context click of item " + candidate.getId(),
                        Toast.LENGTH_LONG
                ).show();
                return true;
            });
            holder.itemView.setOnLongClickListener(v -> {
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                ClipData.Item clipItem = new ClipData.Item(mValues.get(position).getId());
                ClipData dragData = new ClipData(
                        ((Candidate) v.getTag()).getFullName(),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        clipItem
                );

                if (Build.VERSION.SDK_INT >= 24) {
                    v.startDragAndDrop(
                            dragData,
                            new View.DragShadowBuilder(v),
                            null,
                            0
                    );
                } else {
                    v.startDrag(
                            dragData,
                            new View.DragShadowBuilder(v),
                            null,
                            0
                    );
                }
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(CandidateListContentBinding binding) {
                super(binding.getRoot());
                mIdView = binding.idText;
                mContentView = binding.content;
            }

        }
    }
}
package com.aso.ectvotingcotrolpanel.ui.candidates;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aso.ectvotingcotrolpanel.data.models.Candidate;
import com.aso.ectvotingcotrolpanel.databinding.FragmentCandidateDetailBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.aso.ectvotingcotrolpanel.R;

/**
 * A fragment representing a single Candidate detail screen.
 * This fragment is either contained in a {@link CandidateListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class CandidateDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The placeholder content this fragment is presenting.
     */
    private Candidate candidate;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mTextView;

    private final View.OnDragListener dragListener = (v, event) -> {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
            //candidate = PlaceholderContent.ITEM_MAP.get(clipDataItem.getText().toString());
            updateContent();
        }
        return true;
    };
    private FragmentCandidateDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CandidateDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //candidate = PlaceholderContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCandidateDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        mTextView = binding.candidateDetail;

        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (candidate != null) {
            mTextView.setText(candidate.getFullName());
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(candidate.getFullName());
            }
        }
    }
}
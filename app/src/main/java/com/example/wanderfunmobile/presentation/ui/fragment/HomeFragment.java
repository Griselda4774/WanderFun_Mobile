package com.example.wanderfunmobile.presentation.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wanderfunmobile.core.util.PostViewManager;
import com.example.wanderfunmobile.core.util.SessionManager;
import com.example.wanderfunmobile.databinding.FragmentHomeBinding;
import com.example.wanderfunmobile.domain.model.posts.Post;
import com.example.wanderfunmobile.presentation.ui.activity.post.AddEditPostActivity;
import com.example.wanderfunmobile.presentation.ui.adapter.posts.PostItemAdapter;
import com.example.wanderfunmobile.presentation.viewmodel.posts.PostViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding viewBinding;
    private PostViewModel postViewModel;
    private PostItemAdapter postItemAdapter;
    private final List<Post> postList = new ArrayList<>();
    private boolean isFirstLoad = true;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Inject
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        return viewBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null ) {
                            switch (Objects.requireNonNull(data.getStringExtra("status")))
                            {
                                default:
                                    break;
                            }
                        }
                    }
                });

        viewBinding.createPostInput.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditPostActivity.class);
            startActivity(intent);
        });

        viewBinding.postList.setLayoutManager(new LinearLayoutManager(getContext()));
        postItemAdapter = new PostItemAdapter(postList, activityResultLauncher);
        viewBinding.postList.setAdapter(postItemAdapter);

        // Load posts
        if (!(PostViewManager.getInstance(requireContext()).getCursor() > 1)) {
            postViewModel.findAllPostsByCursor(null, null);
        } else {
            postViewModel.findAllPostsByCursor(PostViewManager.getInstance(requireContext()).getCursor(), 10);
        }

        // Observe Post ViewModel
        postViewModel.getFindAllPostsByCursorLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!result.isError() && result.getData() != null) {
                postList.addAll(result.getData());
                postItemAdapter.notifyDataSetChanged();
                if (postList.isEmpty()) {
                    PostViewManager.getInstance(requireContext().getApplicationContext()).reset();
                    postViewModel.findAllPostsByCursor(null, null);
                }
            } else {
                Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

//        postViewModel.getFindAllPostsNoParamLiveData().observe(getViewLifecycleOwner(), result -> {
//            if (!result.isError() && result.getData() != null) {
//                postList.addAll(result.getData());
//                postItemAdapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
//            }
//        });

//        postViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            if (isLoading) {
//                viewBinding.loading.setVisibility(View.VISIBLE);
//            } else {
//                viewBinding.loading.setVisibility(View.GONE);
//            }
//        });

        // Create post container
        if (SessionManager.getInstance(requireContext().getApplicationContext()).isLoggedIn()) {
            viewBinding.createPostContainer.setVisibility(View.VISIBLE);
        } else {
            viewBinding.createPostContainer.setVisibility(View.GONE);
        }

        // RecyclerView
        viewBinding.postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.setVerticalScrollBarEnabled(recyclerView.computeVerticalScrollOffset() > 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if (!isFirstLoad) {
            if (!(PostViewManager.getInstance(requireContext()).getCursor() > 1)) {
                postViewModel.findAllPostsByCursor(null, null);
            } else {
                postViewModel.findAllPostsByCursor(PostViewManager.getInstance(requireContext()).getCursor(), 10);
            }
        }
        isFirstLoad = false;
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!postList.isEmpty()) {
            PostViewManager.getInstance(requireContext().getApplicationContext()).init(postList.get(postList.size() - 1).getId());
        }
        postList.clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
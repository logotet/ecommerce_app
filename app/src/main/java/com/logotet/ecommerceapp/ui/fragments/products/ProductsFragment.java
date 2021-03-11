package com.logotet.ecommerceapp.ui.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.databinding.FragmentPrductsBinding;

public class ProductsFragment extends Fragment {

    private ProductsViewModel productsViewModel;
    private FragmentPrductsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        productsViewModel =
//                new ViewModelProvider(this).get(ProductsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_prducts, container, false);
        binding = DataBindingUtil.bind(root);
        binding.textHome.setText("Kur Home");
//        productsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
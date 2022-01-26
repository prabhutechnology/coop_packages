package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.searchs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentSearchBinding;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements ProgressTrigger {

    private FragmentSearchBinding binding;
    private static final String TAG = "SearchActivity";
    public static final String SEARCH_DATA_ARRAY = "search_location";
    public static final String SEARCH_IMAGE_ARRAY = "search_images";
    public static final String SEARCH_CODE_ARRAY = "search_codes";
    public static final String TITLE = "title";

    // TODO: 7/12/19 move to contracts
    public static final int SEARCH_INTENT = 100;

    private ArrayList<String> defaultData = new ArrayList<>();
    private ArrayList<String> imageData = new ArrayList<>();
    private ArrayList<String> codeData = new ArrayList<>();
    private String title;

    public static final String ZERO = "0";
    public static final String ONE = "1";

    int getFragmentId = 0;
    String getFragmentIds = " ";

    private final com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection navigationRedirection;

    public SearchFragment() {
        // Required empty public constructor
        navigationRedirection = new com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        String[] names = com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.searchs.SearchFragmentArgs.fromBundle(getArguments()).getNames();
        // TODO: Ask Anish dai before removing
        /*getFragmentId = SearchFragmentArgs.fromBundle(getArguments()).getFragmentID();
        DebugMode.log(TAG, "onCreateView: " + getFragmentId);*/

        for (String name : names) {
            com.prabhutech.prabhupackages.wallet.utils.DebugMode.log(TAG, "onCreateView: " + name);
        }

        binding.recyclerSearch.hasFixedSize();
        SearchListAdapter adapter = new SearchListAdapter(new SearchFragment());
        binding.recyclerSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerSearch.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setBusy(String reason, boolean isBusy) {

    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> /*implements Filterable*/ {

        private SearchFragment context;
        public ArrayList<String> values;
        //        public ArrayList<BankDiff> values2;
        public ArrayList<String> images;
        public ArrayList<String> codeData;
        private ArrayList<String> immutable;
        //        private ArrayList<BankDiff> getImages;
        private String title;

        public SearchListAdapter(SearchFragment context) {
            this.context = context;
        }

        public SearchListAdapter(SearchFragment context, ArrayList<String> values, String title) {
            this.context = context;
            this.values = values;
            this.title = title;
            this.immutable = new ArrayList<>(this.values);

        }

     /*   public SearchListAdapter(SearchFragment context, ArrayList<String> values, ArrayList<String> images, ArrayList<String> codeData, String title, ArrayList<BankDiff> getImages) {
            this.context = context;
            this.values = values;
            this.images = images;
            this.codeData = codeData;
            this.title = title;
            this.immutable = new ArrayList<>(this.values);
            this.values2 = getImages;
            this.getImages = new ArrayList<BankDiff>(this.values2);
        }*/

        @NonNull
        @Override
        public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
            return new SearchListViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull SearchListViewHolder holder, int position) {
          /*  if (title.equals("Choose Bank"))
                holder.value.setText(values2.get(position).name);
            else*/
//            holder.value.setText(values.get(position));

//            if (title.equals("Choose Bank")) {
//                holder.imageView.setVisibility(View.VISIBLE);
//                Glide.with(context).load(images.get(position)).into(holder.imageView);
//            } else {
//                holder.imageView.setVisibility(View.GONE);
//            }
//            if (holder.value.getText().equals("Nothing found")) {
//                holder.itemView.setEnabled(false);
//            } else
            {

//                holder.itemView.setEnabled(true);
                holder.itemView.setOnClickListener(v -> {
                    navigationRedirection.navigateBack(v);
//                    navigationRedirection.navigateToFragmentWithAction(v, getFragmentId);
//                    Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_amountCollectionFragment);
//                    String result = null;
//                    try {
//                        result = values.get(position);
//                    } catch (Exception e) {
//                        result = defaultData.get(position); // ?? why ??
//                    }
//                    Intent data = new Intent();
//                    data.putExtra(Intent.EXTRA_RETURN_RESULT, result);

//                    data.putExtra(Intent.EXTRA_ASSIST_CONTEXT, getIntent().getStringExtra(Intent.EXTRA_ASSIST_CONTEXT));
//                    if (title.equals("Choose Bank")) {
//                        data.putExtra(Intent.EXTRA_REFERRER, images.get(position));
//                        data.putExtra(Intent.EXTRA_SPLIT_NAME, codeData.get(position));
//                    }
//                    setResult(SearchFragment.SEARCH_INTENT, data);

                });
            }
        }

        @Override
        public int getItemCount() {
            return 10;

//            return values.size();
        }

      /*  @Override
        public Filter getFilter() {
            return filterList;
        }
*/
       /* private Filter filterList = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence searchSeq) {
                ArrayList<String> filtered = new ArrayList<>();
                if (searchSeq == null || searchSeq.length() == 0) {
                    filtered.addAll(immutable);
                } else {
                    String filterPattern = searchSeq.toString().toLowerCase().trim();
                    for (String item : immutable)
                        if (item.toLowerCase().startsWith(filterPattern)) {
                            filtered.add(item);

                        }

                    for (String item : immutable)
                        if (item.toLowerCase().contains(filterPattern)) {
                            if (filtered.contains(item)) {
                                //do nothing
                            } else {
                                filtered.add(item);
                            }
                        }

                    if (filtered.isEmpty()) {
                        filtered.add("Nothing found");
                    }

                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                values.clear();
                values.addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };*/

       /* private Filter bankfilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence searchSeq) {
                ArrayList<BankDiff> filtered = new ArrayList<>();
                if (searchSeq == null || searchSeq.length() == 0) {
                    filtered.addAll(getImages);
                } else {
                    String filterPattern = searchSeq.toString().toLowerCase().trim();
                    for (BankDiff item : getImages)
                        if (item.name.toLowerCase().startsWith(filterPattern)) {
                            filtered.add(item);
                        }

                    for (BankDiff item : getImages)
                        if (item.name.toLowerCase().contains(filterPattern)) {
                            if (filtered.contains(item)) {
                                //do nothing
                            } else {
                                filtered.add(item);
                            }
                        }

                    if (filtered.isEmpty()) {
                        BankDiff db = new BankDiff();
                        db.name = "Nothing found";
                        db.logo = String.valueOf(R.drawable.bank);
                        db.code = "NODATATEST";
                        filtered.add(db);
                    }

                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }*/

          /*  @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                values2.clear();
                values2.addAll((List<BankDiff>) results.values);
                notifyDataSetChanged();
            }*/
//        };


        private class SearchListViewHolder extends RecyclerView.ViewHolder {

            TextView value;
            ImageView imageView;

            SearchListViewHolder(@NonNull View itemView) {
                super(itemView);
                value = itemView.findViewById(R.id.value);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}
package asay.asaymobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import asay.asaymobile.BillContract;
import asay.asaymobile.R;
import asay.asaymobile.UserContract;
import asay.asaymobile.activities.BillActivity;
import asay.asaymobile.activities.MainActivity;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.BillDTO.CaseStep;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.BillPresenter;
import asay.asaymobile.presenter.UserPresenter;


public class BillsAllFragment extends Fragment implements AdapterView.OnItemClickListener, BillContract.View, UserContract.View{
    private BillPresenter billPresenter;
    private double userId = 1;
    private ArrayList<BillDTO> bills = new ArrayList<>();
    private ArrayAdapter<BillDTO> adapter;
    private MainActivity activity;
    private Typeface typeface;
    private ListFilter listFilter;
    private boolean isFavorite = false;
    private boolean isEnded = false;

    public BillsAllFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills_all, container, false);
        //ButterKnife.bind(this, view);

        if(getArguments() != null){
            isFavorite = getArguments().getBoolean("isFavorite");
            isEnded = getArguments().getBoolean("isEnded");
        }
        billPresenter = new BillPresenter(this,isEnded);
        UserPresenter userPresenter = new UserPresenter(this);


        if(isFavorite){
            userPresenter.getUser(userId);
            view.findViewById(R.id.loadingBill).setVisibility(View.GONE);

        } else if (isEnded) {
            billPresenter.getEndedBills();
        } else {
            billPresenter.getAllBills();

        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFilter();
        // get reference to the views
        adapter = new ArrayAdapter<BillDTO>(getActivity(), R.layout.list_item_bill,R.id.listeelem_header,bills){
            @SuppressLint("DefaultLocale")
            @Override
            public View getView(int position, View cachedView, ViewGroup parent){
                View view = super.getView(position, cachedView, parent);
                if(!bills.isEmpty()) {
                    BillDTO bill = bills.get(position);
                    TextView titleTextView = view.findViewById(R.id.listeelem_header);
                    String title = bill.number + ": " + bill.getTitleShort();
                    titleTextView.setText(title);
                    TextView date = view.findViewById(R.id.listeelem_date);
                    date.setText(getResources().getQuantityString(R.plurals.days_until_deadline, (int) calcDateFromToday(bill.getDeadline()), calcDateFromToday(bill.getDeadline())));
                    TextView numberOfVotes = view.findViewById(R.id.listeelem_number_of_votes);
                    if (bill.votes.size() > 0)
                        numberOfVotes.setText(getResources().getQuantityString(R.plurals.number_of_votes, bill.votes.size(), bill.votes.size()));
                    else {
                        numberOfVotes.setText("");
                    }
                }
                return view;
            }
        };
        ListView listview = view.findViewById(R.id.allBillsListView);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BillDTO item = bills.get(position);
        Intent switchview = new Intent(getContext(), BillActivity.class);
        if(calcDateFromToday(item.getDeadline()) < 0) {
            switchview.putExtra("isEnded", true);
        }
        else {
            switchview.putExtra("isEnded", false);
        }

        switchview.putExtra("bill", item);
        startActivity(switchview);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    @Override
    public void refreshCurrentBills(final ArrayList<BillDTO> bills) {
        this.bills.clear();
        if (!bills.isEmpty()) {
            for (BillDTO bill : bills) {
                boolean hasSteps = false;
                for (CaseStep step : bill.getCaseSteps()) {
                    if ((step.getTypeid() == 87 || step.getTypeid() == 7 || step.getTypeid() == 23 || step.getTypeid() == 17 ||
                            step.getTypeid() == 12))
                        hasSteps = true;
                }
                if (bill.getResume() != null && !bill.getResume().isEmpty() && hasSteps)
                    this.bills.add(bill);
            }
            adapter.notifyDataSetChanged();
        }
        if (getView() != null)
            getView().findViewById(R.id.loadingBill).setVisibility(View.GONE);
        System.out.println("done");

        }

    public void refreshEndedBills(final ArrayList<BillDTO> bills){
        if (!bills.isEmpty()) {
            this.bills.clear();
            for (BillDTO bill : bills) {
                boolean hasSteps = false;
                for (CaseStep step : bill.getCaseSteps()) {
                    if ((step.getTypeid() == 87 || step.getTypeid() == 7 || step.getTypeid() == 23 || step.getTypeid() == 17 ||
                            step.getTypeid() == 12))
                        hasSteps = true;
                }
                if (bill.getResume() != null && !bill.getResume().isEmpty() && hasSteps && calcDateFromToday(bill.getDeadline()) < 0)
                    this.bills.add(bill);
            }
            adapter.notifyDataSetChanged();
        }
        if (getView() != null)
            getView().findViewById(R.id.loadingBill).setVisibility(View.GONE);
    }

    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    @Override
    public void refreshUser(UserDTO user) {
        ArrayList<Integer> savedbills = user.getbillsSaved();
        billPresenter.getSavedBills(savedbills);
    }

    private long calcDateFromToday(String date) {
        long diffDays = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sdf.parse(date);
            Date today = new Date();
            long diff = d.getTime() - today.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
        } catch (ParseException ex){
            System.out.println(ex.getMessage());
        }

        return diffDays;
    }


    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BillDTO> tempList = new ArrayList<>();

                // search content in friend list
                for (BillDTO bill : bills) {
                    if (bill.getNumber().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            bill.getTitleShort().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(bill);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = bills.size();
                filterResults.values = bills;
            }
            System.out.println(bills.size());
            System.out.println("qq: " + filterResults.values.toString());
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<BillDTO> filteredList = (ArrayList<BillDTO>) results.values;
            adapter.notifyDataSetChanged();
            System.out.println("n: " + filteredList.size());

        }

    }

}

package asay.asaymobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.activities.BillActivity;
import asay.asaymobile.fetch.HttpAsyncTask;


public class BillsAllFragment extends Fragment implements AdapterView.OnItemClickListener{

    EditText etResponse;
    private ArrayList<String> bills = new ArrayList<String>();
    ArrayAdapter adapter;

    public BillsAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills_all, container, false);
        return view;    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        // call AsynTask to perform network operation on separate thread
        String baseUrl = "http://oda.ft.dk/api/Sag?$orderby=id%20desc";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAkt%C3%B8r,Sagstrin";
        String proposalFilter = "&$filter=(typeid%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String urlAsString = new StringBuilder().append(baseUrl).append(proposalExpand).append(proposalFilter).toString();
        System.out.println("my url: " + urlAsString);
        new HttpAsyncTask(getActivity(), new AsyncTaskCompleteListener()).execute(urlAsString);

        // String baseUrl ="http://hmkcode.appspot.com/rest/controller/get.json";

        // get reference to the views
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item_bill,R.id.listeelem_header,bills);

        ListView listview = new ListView(getActivity());
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
        ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.addView(listview);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent switchview = new Intent(getActivity(), BillActivity.class);
        startActivity(switchview);
        Toast.makeText(getActivity() , "Click on" + position,Toast.LENGTH_SHORT).show();

    }

    private class AsyncTaskCompleteListener implements asay.asaymobile.fetch.AsyncTaskCompleteListener<JSONObject> {


        @Override
        public void onTaskComplete(JSONObject result)
        {
            try{
                String str = "";
                if (result == null){
                    etResponse.setText("Result is null");
                }
                Log.d("OnTaskComplete", "onTaskComplete: " + result);
                JSONArray articles = result.getJSONArray("value"); // get articles array
                for (int i = 0; i < articles.length(); i++){
                    bills.add(articles.getJSONObject(i).getString("titelkort"));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception excep){
                Log.d("JSON Exception", "onTaskComplete: " + excep.getMessage());
            }
            // do something with the result
        }
    }
}
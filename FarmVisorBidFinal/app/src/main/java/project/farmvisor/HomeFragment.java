package project.farmvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class HomeFragment extends Fragment {
    static String farm_id,lang;

    public HomeFragment() {
    }


    public static HomeFragment newInstance(String param1, String param2, String language) {
        HomeFragment fragment = new HomeFragment();
        farm_id=param1;
        lang=language;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;

        if (lang.equals("English")) {
            root=inflater.inflate(R.layout.fragment_home, container, false);
        }
        else
        {
            root=inflater.inflate(R.layout.kfragment_home, container, false);
        }


        ImageButton job=(ImageButton)root.findViewById(R.id.btn_my_jobs);
        ImageButton land=(ImageButton)root.findViewById(R.id.btn_my_land);
        ImageButton invest=(ImageButton)root.findViewById(R.id.btn_my_investment);
        ImageButton equipment=(ImageButton)root.findViewById(R.id.btn_my_equipment);
        ImageButton crop=(ImageButton)root.findViewById(R.id.btn_my_crops);
        ImageButton auctions=(ImageButton)root.findViewById(R.id.btn_auctions);
        TextView equipmentText = root.findViewById(R.id.textView5);
        TextView auctionsText = root.findViewById(R.id.auctionsText);


        if(MyApplication.userType.equals("trader")){
            job.setVisibility(View.GONE);
            land.setVisibility((View.GONE));
            invest.setVisibility(View.GONE);
            equipment.setVisibility(View.GONE);
            equipmentText.setVisibility(View.GONE);
        }
        else{
            auctions.setVisibility(View.GONE);
            auctionsText.setVisibility(View.GONE);

        }


        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_former_home,ListFragment.newInstance("job",farm_id))
                        .commit();
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MyBidChooser.class);
                i.putExtra("farmer_id",farm_id);
                startActivity(i);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.content_former_home,ListFragment.newInstance("crop",farm_id))
//                        .commit();
            }
        });

        auctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ShowCropAuctionsActivity.class);
                i.putExtra("farmer_id",farm_id);
                startActivity(i);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.content_former_home,ListFragment.newInstance("auctions",farm_id))
//                        .commit();
            }
        });
        equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_former_home,ListFragment.newInstance("equipment",farm_id))
                        .commit();
            }
        });

        land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_former_home,ListFragment.newInstance("land",farm_id))
                        .commit();
            }
        });

        invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_former_home,ListFragment.newInstance("invest",farm_id))
                        .commit();
            }
        });



        return root;
    }



}

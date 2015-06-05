package com.phonetact.phonetact.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.Country;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by hp on 28/05/2015.
 */
public class ChooseCityActivity extends ActionBarActivity {
    //element layout
    //RecyclerView recyclview;

    //adaptater
    //NewsAdapter messageSnippetAdapter;
    MyCustomAdapter dataAdapter = null;

    //variable
    ArrayList<Country> countryLookupMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecity);

        // intialiser the element layout
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Choose your contry");
        mToolbar.setNavigationIcon(R.drawable.ic_ab_up_compat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
            }
        });

        //the recycler view
        //recyclview = (RecyclerView) findViewById(R.id.my_recycler_view);
       // recyclview.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       // recyclview.setLayoutManager(mLayoutManager);
       // messageSnippetAdapter = new NewsAdapter();
        //recyclview.setAdapter(messageSnippetAdapter);
        displayListView();
    }


    //recycl adapter
    public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private  final String TAG = NewsAdapter.class.getSimpleName();
        private LayoutInflater mLayoutInflater;
        private ActionBarActivity mContext;
        private FragmentManager mFragmentManager;


        public NewsAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //inflate your layout and pass it to view holder
            mContext = (ActionBarActivity) viewGroup.getContext();
            mLayoutInflater = LayoutInflater.from(mContext);
            mFragmentManager = mContext.getSupportFragmentManager();
            View v = mLayoutInflater.inflate(R.layout.elemnt_choose_city,
                    viewGroup, false);
            return new VHHeader(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //cast holder to VHHeader and set data for header.
            final int pos = position;
            ((VHHeader) holder).txtmarque.setText("");

            ((VHHeader) holder).mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SignUpActivity.theCity = "position "+pos;
                    finish();
                    overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
                }
            });

        }

        @Override
        public int getItemCount() {
            return 20;
            //return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_HEADER;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        private String getItem(int position) {
            return "";
        }



        class VHHeader extends RecyclerView.ViewHolder {

            public TextView txtmarque;
            public MaterialRippleLayout mView;

            public VHHeader(View v) {
                super(v);
                txtmarque = (TextView)v.findViewById(R.id.city);
                mView = (MaterialRippleLayout) v.findViewById(R.id.rippler);
            }
        }


    }

    //adaptater with filter
    private void displayListView() {

        //Array list of countries
        /*ArrayList<String> countryList = new ArrayList<String>();
        countryList.add("Afghanistan");
        countryList.add("Albania");
        countryList.add("Algeria");
        countryList.add("American Samoa");
        countryList.add("Andorra");
        countryList.add("Angola");
        countryList.add("Anguilla");*/
        save();
        Collections.sort(countryLookupMap, new Comparator<Country>() {
            @Override
            public int compare(Country lhs, Country rhs) {
                char lhsFirstLetter = TextUtils.isEmpty(lhs.getName()) ? ' ' : lhs.getName().charAt(0);
                char rhsFirstLetter = TextUtils.isEmpty(rhs.getName()) ? ' ' : rhs.getName().charAt(0);
                int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                if (firstLetterComparison == 0)
                    return lhs.getName().compareTo(rhs.getName());
                return firstLetterComparison;
            }
        });

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.elemnt_choose_city, countryLookupMap);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                SignUpActivity.theCity =((Country) parent.getItemAtPosition(position)).getName();
                SignUpActivity.theCode =((Country) parent.getItemAtPosition(position)).getCode();
                finish();
                overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

    }
    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> originalList;
        private ArrayList<Country> countryList;
        private CountryFilter filter;



        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
            this.originalList = new ArrayList<Country>();
            this.originalList.addAll(countryList);
        }

        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new CountryFilter();
            }
            return filter;
        }


        private class ViewHolder {
            TextView name;
            MaterialRippleLayout mView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            ViewHolder holder = null;
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.elemnt_choose_city, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.city);
                holder.mView = (MaterialRippleLayout) convertView.findViewById(R.id.rippler);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            //String country = countryList.get(position);
            holder.name.setText(country.getName());
            /*holder.mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SignUpActivity.theCity = "position "+pos;
                    finish();
                    overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
                }
            });*/

            return convertView;

        }

        private class CountryFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0)
                {
                    ArrayList<Country> filteredItems = new ArrayList<Country>();

                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        Country country = originalList.get(i);
                        if(country.toString().toLowerCase().contains(constraint))
                            filteredItems.add(country);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                }
                else
                {
                    synchronized(this)
                    {
                        result.values = originalList;
                        result.count = originalList.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                countryList = (ArrayList<Country>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = countryList.size(); i < l; i++)
                    add(countryList.get(i));
                notifyDataSetInvalidated();
            }
        }


    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
    }


    //set the arraylist of contry code
    private void  save(){

        countryLookupMap = new ArrayList<Country>();

        countryLookupMap.add(new Country("376","Andorra"));//
        countryLookupMap.add(new Country("971","United Arab Emirates"));//
        countryLookupMap.add(new Country("93","Afghanistan"));
        countryLookupMap.add(new Country("1","Antigua and Barbuda"));//
        countryLookupMap.add(new Country("","Anguilla"));
        countryLookupMap.add(new Country("355","Albania"));
        countryLookupMap.add(new Country("374","Armenia"));
        countryLookupMap.add(new Country("599","Netherlands Antilles"));
        countryLookupMap.add(new Country("244","Angola"));
        countryLookupMap.add(new Country("672","Antarctica"));
        countryLookupMap.add(new Country("54","Argentina"));
        countryLookupMap.add(new Country("1","American Samoa"));//
        countryLookupMap.add(new Country("43","Austria"));
        countryLookupMap.add(new Country("61","Australia"));
        countryLookupMap.add(new Country("297","Aruba"));
        countryLookupMap.add(new Country("994","Azerbaijan"));
        countryLookupMap.add(new Country("387","Bosnia and Herzegovina"));
        countryLookupMap.add(new Country("1","Barbados"));//
        countryLookupMap.add(new Country("880","Bangladesh"));
        countryLookupMap.add(new Country("32","Belgium"));
        countryLookupMap.add(new Country("226","Burkina Faso"));
        countryLookupMap.add(new Country("359","Bulgaria"));
        countryLookupMap.add(new Country("973","Bahrain"));
        countryLookupMap.add(new Country("257","Burundi"));
        countryLookupMap.add(new Country("229","Benin"));
        countryLookupMap.add(new Country("1","Bermuda"));//
        countryLookupMap.add(new Country("673","Brunei"));
        countryLookupMap.add(new Country("591","Bolivia"));
        countryLookupMap.add(new Country("55","Brazil"));
        countryLookupMap.add(new Country("1","Bahamas"));//
        countryLookupMap.add(new Country("975","Bhutan"));
        //countryLookupMap.add("BV",new Country("","Bouvet Island"));//
        countryLookupMap.add(new Country("267","Botswana"));
        countryLookupMap.add(new Country("375","Belarus"));
        countryLookupMap.add(new Country("501","Belize"));
        countryLookupMap.add(new Country("1","Canada"));
        countryLookupMap.add(new Country("61","Cocos (Keeling) Islands"));
        countryLookupMap.add(new Country("243","Congo, The Democratic Republic of the"));
        countryLookupMap.add(new Country("236","Central African Republic"));
        countryLookupMap.add(new Country("242","Congo"));
        countryLookupMap.add(new Country("41","Switzerland"));
        countryLookupMap.add(new Country("225","Côte d?Ivoire"));
        countryLookupMap.add(new Country("682","Cook Islands"));
        countryLookupMap.add(new Country("56","Chile"));
        countryLookupMap.add(new Country("237","Cameroon"));
        countryLookupMap.add(new Country("86","China"));
        countryLookupMap.add(new Country("57","Colombia"));
        countryLookupMap.add(new Country("506","Costa Rica"));
        countryLookupMap.add(new Country("53","Cuba"));
        countryLookupMap.add(new Country("238","Cape Verde"));
        countryLookupMap.add(new Country("61","Christmas Island"));
        countryLookupMap.add(new Country("357","Cyprus"));
        countryLookupMap.add(new Country("420","Czech Republic"));
        countryLookupMap.add(new Country("49","Germany"));
        countryLookupMap.add(new Country("253","Djibouti"));
        countryLookupMap.add(new Country("45","Denmark"));
        countryLookupMap.add(new Country("1","Dominica"));//
        countryLookupMap.add(new Country("1","Dominican Republic"));//
        countryLookupMap.add(new Country("213","Algeria"));
        countryLookupMap.add(new Country("593","Ecuador"));
        countryLookupMap.add(new Country("372","Estonia"));
        countryLookupMap.add(new Country("20","Egypt"));
        countryLookupMap.add(new Country("212","Western Sahara"));//
        countryLookupMap.add(new Country("291","Eritrea"));
        countryLookupMap.add(new Country("34","Spain"));
        countryLookupMap.add(new Country("251","Ethiopia"));
        countryLookupMap.add(new Country("358","Finland"));
        countryLookupMap.add(new Country("679","Fiji Islands"));
        countryLookupMap.add(new Country("500","Falkland Islands"));
        countryLookupMap.add(new Country("691","Micronesia, Federated States of"));
        countryLookupMap.add(new Country("298","Faroe Islands"));
        countryLookupMap.add(new Country("33","France"));
        countryLookupMap.add(new Country("241","Gabon"));
        countryLookupMap.add(new Country("44","United Kingdom"));
        countryLookupMap.add(new Country("1","Grenada"));//
        countryLookupMap.add(new Country("995","Georgia"));
        //countryLookupMap.add("GF",new Country("","French Guiana"));//
        countryLookupMap.add(new Country("233","Ghana"));
        countryLookupMap.add(new Country("350","Gibraltar"));
        countryLookupMap.add(new Country("299","Greenland"));
        countryLookupMap.add(new Country("220","Gambia"));
        countryLookupMap.add(new Country("224","Guinea"));
        //countryLookupMap.add("GP",new Country("","Guadeloupe"));//
        countryLookupMap.add(new Country("240","Equatorial Guinea"));
        countryLookupMap.add(new Country("30","Greece"));
        //countryLookupMap.add("GS",new Country("","South Georgia and the South Sandwich Islands"));//
        countryLookupMap.add(new Country("502","Guatemala"));
        countryLookupMap.add(new Country("1","Guam"));//
        countryLookupMap.add(new Country("245","Guinea-Bissau"));
        countryLookupMap.add(new Country("592","Guyana"));
        countryLookupMap.add(new Country("852","Hong Kong"));
        //countryLookupMap.add("HM",new Country("","Heard Island and McDonald Islands"));//
        countryLookupMap.add(new Country("504","Honduras"));
        countryLookupMap.add(new Country("385","Croatia"));
        countryLookupMap.add(new Country("509","Haiti"));
        countryLookupMap.add(new Country("36","Hungary"));
        countryLookupMap.add(new Country("62","Indonesia"));
        countryLookupMap.add(new Country("353","Ireland"));
        countryLookupMap.add(new Country("972","Israel"));
        countryLookupMap.add(new Country("91","India"));
        countryLookupMap.add(new Country("246","British Indian Ocean Territory"));//
        countryLookupMap.add(new Country("964","Iraq"));
        countryLookupMap.add(new Country("98","Iran"));
        countryLookupMap.add(new Country("354","Iceland"));//
        countryLookupMap.add(new Country("39","Italy"));
        countryLookupMap.add(new Country("1","Jamaica"));//
        countryLookupMap.add(new Country("962","Jordan"));
        countryLookupMap.add(new Country("81","Japan"));
        countryLookupMap.add(new Country("254","Kenya"));
        countryLookupMap.add(new Country("996","Kyrgyzstan"));
        countryLookupMap.add(new Country("855","Cambodia"));
        countryLookupMap.add(new Country("686","Kiribati"));
        countryLookupMap.add(new Country("269","Comoros"));
        countryLookupMap.add(new Country("1","Saint Kitts and Nevis"));//
        countryLookupMap.add(new Country("850","North Korea"));
        countryLookupMap.add(new Country("82","South Korea"));
        countryLookupMap.add(new Country("965","Kuwait"));
        countryLookupMap.add(new Country("1","Cayman Islands"));//
        countryLookupMap.add(new Country("7","Kazakstan"));
        countryLookupMap.add(new Country("856","Laos"));
        countryLookupMap.add(new Country("961","Lebanon"));
        countryLookupMap.add(new Country("1","Saint Lucia"));//
        countryLookupMap.add(new Country("423","Liechtenstein"));
        countryLookupMap.add(new Country("94","Sri Lanka"));
        countryLookupMap.add(new Country("231","Liberia"));
        countryLookupMap.add(new Country("266","Lesotho"));
        countryLookupMap.add(new Country("370","Lithuania"));
        countryLookupMap.add(new Country("352","Luxembourg"));
        countryLookupMap.add(new Country("371","Latvia"));
        countryLookupMap.add(new Country("218","Libyan Arab Jamahiriya"));
        countryLookupMap.add(new Country("212","Morocco"));
        countryLookupMap.add(new Country("377","Monaco"));
        countryLookupMap.add(new Country("373","Moldova"));
        countryLookupMap.add(new Country("261","Madagascar"));
        countryLookupMap.add(new Country("692","Marshall Islands"));
        countryLookupMap.add(new Country("389","Macedonia"));
        countryLookupMap.add(new Country("223","Mali"));
        countryLookupMap.add(new Country("95","Myanmar"));
        countryLookupMap.add(new Country("976","Mongolia"));
        countryLookupMap.add(new Country("853","Macao"));
        countryLookupMap.add(new Country("1","Northern Mariana Islands"));//
        //countryLookupMap.add("MQ",new Country("","Martinique"));//
        countryLookupMap.add(new Country("222","Mauritania"));
        countryLookupMap.add(new Country("1","Montserrat"));//
        countryLookupMap.add(new Country("356","Malta"));
        countryLookupMap.add(new Country("230","Mauritius"));
        countryLookupMap.add(new Country("960","Maldives"));
        countryLookupMap.add(new Country("265","Malawi"));
        countryLookupMap.add(new Country("52","Mexico"));
        countryLookupMap.add(new Country("60","Malaysia"));
        countryLookupMap.add(new Country("258","Mozambique"));
        countryLookupMap.add(new Country("264","Namibia"));
        countryLookupMap.add(new Country("687","New Caledonia"));
        countryLookupMap.add(new Country("227","Niger"));
        //countryLookupMap.add("NF",new Country("","Norfolk Island"));//
        countryLookupMap.add(new Country("234","Nigeria"));
        countryLookupMap.add(new Country("505","Nicaragua"));
        countryLookupMap.add(new Country("31","Netherlands"));
        countryLookupMap.add(new Country("","Norway"));
        countryLookupMap.add(new Country("977","Nepal"));
        countryLookupMap.add(new Country("674","Nauru"));
        countryLookupMap.add(new Country("683","Niue"));
        countryLookupMap.add(new Country("64","New Zealand"));
        countryLookupMap.add(new Country("968","Oman"));
        countryLookupMap.add(new Country("507","Panama"));
        countryLookupMap.add(new Country("51","Peru"));
        countryLookupMap.add(new Country("689","French Polynesia"));
        countryLookupMap.add(new Country("675","Papua New Guinea"));
        countryLookupMap.add(new Country("63","Philippines"));
        countryLookupMap.add(new Country("92","Pakistan"));
        countryLookupMap.add(new Country("48","Poland"));
        countryLookupMap.add(new Country("508","Saint Pierre and Miquelon"));
        countryLookupMap.add(new Country("870","Pitcairn"));
        countryLookupMap.add(new Country("1","Puerto Rico"));
        countryLookupMap.add(new Country("970","Palestine"));//
        countryLookupMap.add(new Country("351","Portugal"));
        countryLookupMap.add(new Country("680","Palau"));
        countryLookupMap.add(new Country("595","Paraguay"));
        countryLookupMap.add(new Country("974","Qatar"));
        //countryLookupMap.add("RE",new Country("","Réunion"));//
        countryLookupMap.add(new Country("40","Romania"));
        countryLookupMap.add(new Country("7","Russian Federation"));
        countryLookupMap.add(new Country("250","Rwanda"));
        countryLookupMap.add(new Country("966","Saudi Arabia"));
        countryLookupMap.add(new Country("677","Solomon Islands"));
        countryLookupMap.add(new Country("248","Seychelles"));
        countryLookupMap.add(new Country("249","Sudan"));
        countryLookupMap.add(new Country("46","Sweden"));
        countryLookupMap.add(new Country("65","Singapore"));
        countryLookupMap.add(new Country("290","Saint Helena"));
        countryLookupMap.add(new Country("386","Slovenia"));
        countryLookupMap.add(new Country("47","Svalbard and Jan Mayen"));//
        countryLookupMap.add(new Country("421","Slovakia"));
        countryLookupMap.add(new Country("232","Sierra Leone"));
        countryLookupMap.add(new Country("378","San Marino"));
        countryLookupMap.add(new Country("221","Senegal"));
        countryLookupMap.add(new Country("252","Somalia"));
        countryLookupMap.add(new Country("597","Suriname"));
        countryLookupMap.add(new Country("239","Sao Tome and Principe"));
        countryLookupMap.add(new Country("503","El Salvador"));
        countryLookupMap.add(new Country("963","Syria"));
        countryLookupMap.add(new Country("268","Swaziland"));
        countryLookupMap.add(new Country("1","Turks and Caicos Islands"));//
        countryLookupMap.add(new Country("235","Chad"));
        // countryLookupMap.add("TF",new Country("","French Southern territories"));//
        countryLookupMap.add(new Country("228","Togo"));
        countryLookupMap.add(new Country("66","Thailand"));
        countryLookupMap.add(new Country("992","Tajikistan"));
        countryLookupMap.add(new Country("690","Tokelau"));
        countryLookupMap.add(new Country("993","Turkmenistan"));
        countryLookupMap.add(new Country("216","Tunisia"));
        countryLookupMap.add(new Country("676","Tonga"));
        countryLookupMap.add(new Country("670","East Timor"));//
        countryLookupMap.add(new Country("90","Turkey"));
        countryLookupMap.add(new Country("1","Trinidad and Tobago"));//
        countryLookupMap.add(new Country("688","Tuvalu"));
        countryLookupMap.add(new Country("886","Taiwan"));
        countryLookupMap.add(new Country("255","Tanzania"));
        countryLookupMap.add(new Country("380","Ukraine"));
        countryLookupMap.add(new Country("256","Uganda"));
        //countryLookupMap.add("UM",new Country("","United States Minor Outlying Islands"));//
        countryLookupMap.add(new Country("1","United States"));
        countryLookupMap.add(new Country("598","Uruguay"));
        countryLookupMap.add(new Country("998","Uzbekistan"));
        countryLookupMap.add(new Country("39","Holy See (Vatican City State)"));
        countryLookupMap.add(new Country("1","Saint Vincent and the Grenadines"));//
        countryLookupMap.add(new Country("58","Venezuela"));
        //countryLookupMap.add("VG",new Country("","Virgin Islands, British"));
        //countryLookupMap.add("VI",new Country("","Virgin Islands, U.S."));//
        countryLookupMap.add(new Country("84","Vietnam"));
        countryLookupMap.add(new Country("678","Vanuatu"));
        countryLookupMap.add(new Country("681","Wallis and Futuna"));
        countryLookupMap.add(new Country("685","Samoa"));
        countryLookupMap.add(new Country("967","Yemen"));
        countryLookupMap.add(new Country("262","Mayotte"));
        //countryLookupMap.add("YU",new Country("","Yugoslavia"));//
        countryLookupMap.add(new Country("27","South Africa"));
        countryLookupMap.add(new Country("260","Zambia"));
        countryLookupMap.add(new Country("263","Zimbabwe"));
    }
}

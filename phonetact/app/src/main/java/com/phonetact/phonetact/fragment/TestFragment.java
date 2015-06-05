package com.phonetact.phonetact.fragment;



import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.phonetact.R;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.CircularContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import lb.library.PinnedHeaderListView;
import lb.library.SearchablePinnedHeaderListViewAdapter;
import lb.library.StringArrayAlphabetIndexer;
import com.phonetact.phonetact.Utils.CircularContactView;
import com.phonetact.phonetact.Utils.ContactImageUtil;
import com.phonetact.phonetact.Utils.ContactsQuery;
import com.phonetact.phonetact.Utils.ImageCache;
import com.phonetact.phonetact.Utils.async_task_thread_pool.AsyncTaskEx;
import com.phonetact.phonetact.Utils.async_task_thread_pool.AsyncTaskThreadPool;
import com.software.shell.fab.ActionButton;

public class TestFragment extends Fragment{

    //element layout
    LayoutInflater inflate;
    //private LayoutInflater mInflater;
    private PinnedHeaderListView mListView;

    //adaptater
    private ContactsAdapter mAdapter;

    //variable
    protected static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private ArrayList<Contact> contacts = new ArrayList<Contact>();

    public static TestFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater;
        View view = inflater.inflate(R.layout.activity_main, container,
                false);
       /* final EditText edit = (EditText) view.findViewById(R.id.myRecherche);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                test(Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode(edit.getText().toString())));
                return false;
            }
        });*/
        mListView=(PinnedHeaderListView)view.findViewById(android.R.id.list);
        mListView.setEnableHeaderTransparencyChanges(false);

        //You can also perform operations on selected item by using :
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        Uri uri=ContactsQuery.CONTENT_URI;
        getPhoneBook(uri);




        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    public static int getResIdFromAttribute(final Activity activity,final int attr)
    {
        if(attr==0)
            return 0;
        final TypedValue typedValue=new TypedValue();
        activity.getTheme().resolveAttribute(attr,typedValue,true);
        return typedValue.resourceId;
    }

    /**
     *
     */
    public void test(){
        Toast.makeText(getActivity(),"icicici ",Toast.LENGTH_SHORT).show();
    }
    /**
     * getPhoneBook
     * @param uri
     */
    public  void getPhoneBook(Uri uri){
        contacts=getContacts(uri);
        Collections.sort(contacts,new Comparator<Contact>()
        {
            @Override
            public int compare(Contact lhs,Contact rhs)
            {
                char lhsFirstLetter= TextUtils.isEmpty(lhs.displayName)?' ':lhs.displayName.charAt(0);
                char rhsFirstLetter=TextUtils.isEmpty(rhs.displayName)?' ':rhs.displayName.charAt(0);
                int firstLetterComparison=Character.toUpperCase(lhsFirstLetter)-Character.toUpperCase(rhsFirstLetter);
                if(firstLetterComparison==0)
                    return lhs.displayName.compareTo(rhs.displayName);
                return firstLetterComparison;
            }
        });
        //Toast.makeText(getActivity(),"size is "+contacts.size(),Toast.LENGTH_SHORT).show();
        mAdapter=new ContactsAdapter(contacts);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
    }

    private ArrayList<Contact> getContacts(Uri uri)
    {
        //if(checkContactsReadPermission())
        ArrayList<Contact> result=new ArrayList<>();
        if(true)
        {
            try{

                String[] proj = new String[] { MediaStore.Images.Media.DATA };
                final String[] PEOPLE_PROJECTION = new String[] {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Email.DATA,

                };
                String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Event.START_DATE,
                        ContactsContract.CommonDataKinds.Email.DATA,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI
                };
                String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC";
                final Cursor cursor = getActivity().getContentResolver().query(uri,ContactsQuery.PROJECTION, ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);
                if(cursor==null)
                    return null;

                while(cursor.moveToNext())
                {
                    Contact contact=new Contact();
                    contact.contactUri= ContactsContract.Contacts.getLookupUri(
                            cursor.getLong(ContactsQuery.ID),
                            cursor.getString(ContactsQuery.LOOKUP_KEY));
                    contact.displayName = cursor.getString(ContactsQuery.DISPLAY_NAME);//cursor.getString(ContactsQuery.DISPLAY_NAME)
                    contact.photoId = cursor.getString(ContactsQuery.PHOTO_THUMBNAIL_DATA);//cursor.getString(ContactsQuery.PHOTO_THUMBNAIL_DATA);
                    result.add(contact);
                }
            }catch(Exception e){
                Log.d("TestFragment","this is the exception  "+e);
            }


            return result;
        }

        return result;
    }



    private static class Contact
    {
        long contactId;
        Uri contactUri;
        String displayName;
        String photoId;
    }



    // ////////////////////////////////////////////////////////////
    // ContactsAdapter //
    // //////////////////
    private class ContactsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact>
    {
        private ArrayList<Contact> mContacts;
        private final int CONTACT_PHOTO_IMAGE_SIZE;
        private final int[] PHOTO_TEXT_BACKGROUND_COLORS;
        private final AsyncTaskThreadPool mAsyncTaskThreadPool=new AsyncTaskThreadPool(1,2,10);

        @Override
        public CharSequence getSectionTitle(int sectionIndex)
        {
            return ((StringArrayAlphabetIndexer.AlphaBetSection)getSections()[sectionIndex]).getName();
        }

        public ContactsAdapter(final ArrayList<Contact> contacts)
        {
            setData(contacts);
            PHOTO_TEXT_BACKGROUND_COLORS=getResources().getIntArray(R.array.contacts_text_background_colors);
            CONTACT_PHOTO_IMAGE_SIZE=getResources().getDimensionPixelSize(
                    R.dimen.list_item__contact_imageview_size);
        }

        public void setData(final ArrayList<Contact> contacts)
        {
            this.mContacts=contacts;
            final String[] generatedContactNames=generateContactNames(contacts);
            setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames,true));
        }

        private String[] generateContactNames(final List<Contact> contacts)
        {
            final ArrayList<String> contactNames=new ArrayList<String>();
            if(contacts!=null)
                for(final Contact contactEntity : contacts)
                    contactNames.add(contactEntity.displayName);
            return contactNames.toArray(new String[contactNames.size()]);
        }

        @Override
        public View getView(final int position,final View convertView,final ViewGroup parent)
        {
            final ViewHolder holder;
            final View rootView;
            if(convertView==null)
            {
                holder=new ViewHolder();
                rootView=inflate.inflate(R.layout.listview_item,parent,false);
                holder.friendProfileCircularContactView=(CircularContactView)rootView
                        .findViewById(R.id.listview_item__friendPhotoImageView);
                holder.friendProfileCircularContactView.getTextView().setTextColor(0xFFffffff);
                holder.friendName=(TextView)rootView
                        .findViewById(R.id.listview_item__friendNameTextView);
                holder.headerView=(TextView)rootView.findViewById(R.id.header_text);
                rootView.setTag(holder);
            }
            else
            {
                rootView=convertView;
                holder=(ViewHolder)rootView.getTag();
            }
            final Contact contact=getItem(position);
            final String displayName=contact.displayName;
            holder.friendName.setText(displayName);
            boolean hasPhoto=!TextUtils.isEmpty(contact.photoId);
            if(holder.updateTask!=null&&!holder.updateTask.isCancelled())
                holder.updateTask.cancel(true);
            final Bitmap cachedBitmap=hasPhoto?ImageCache.INSTANCE.getBitmapFromMemCache(contact.photoId):null;
            if(cachedBitmap!=null)
                holder.friendProfileCircularContactView.setImageBitmap(cachedBitmap);
            else
            {
                final int backgroundColorToUse=PHOTO_TEXT_BACKGROUND_COLORS[position
                        %PHOTO_TEXT_BACKGROUND_COLORS.length];
                if(TextUtils.isEmpty(displayName))
                    holder.friendProfileCircularContactView.setImageResource(R.drawable.ic_person_white_120dp,
                            backgroundColorToUse);
                else
                {
                    final String characterToShow=TextUtils.isEmpty(displayName)?"":displayName.substring(0,1).toUpperCase(Locale.getDefault());
                    holder.friendProfileCircularContactView.setTextAndBackgroundColor(characterToShow,backgroundColorToUse);
                }
                if(hasPhoto)
                {
                    holder.updateTask=new AsyncTaskEx<Void,Void,Bitmap>()
                    {

                        @Override
                        public Bitmap doInBackground(final Void... params)
                        {
                            if(isCancelled())
                                return null;
                            final Bitmap b=ContactImageUtil.loadContactPhotoThumbnail(getActivity(),contact.photoId,CONTACT_PHOTO_IMAGE_SIZE);
                            if(b!=null)
                                return ThumbnailUtils.extractThumbnail(b, CONTACT_PHOTO_IMAGE_SIZE,
                                        CONTACT_PHOTO_IMAGE_SIZE);
                            return null;
                        }

                        @Override
                        public void onPostExecute(final Bitmap result)
                        {
                            super.onPostExecute(result);
                            if(result==null)
                                return;
                            ImageCache.INSTANCE.addBitmapToCache(contact.photoId,result);
                            holder.friendProfileCircularContactView.setImageBitmap(result);
                        }
                    };
                    mAsyncTaskThreadPool.executeAsyncTask(holder.updateTask);
                }
            }
            bindSectionHeader(holder.headerView,null,position);
            return rootView;
        }

        @Override
        public boolean doFilter(final Contact item,final CharSequence constraint)
        {
            if(TextUtils.isEmpty(constraint))
                return true;
            final String displayName=item.displayName;
            return !TextUtils.isEmpty(displayName)&&displayName.toLowerCase(Locale.getDefault())
                    .contains(constraint.toString().toLowerCase(Locale.getDefault()));
        }

        @Override
        public ArrayList<Contact> getOriginalList()
        {
            return mContacts;
        }


    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ViewHolder //
    // /////////////
    private static class ViewHolder
    {
        public CircularContactView friendProfileCircularContactView;
        TextView friendName, headerView;
        public AsyncTaskEx<Void,Void,Bitmap> updateTask;
    }


}


package info.javaknowledge.idbforum.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.javaknowledge.idbforum.R;
import info.javaknowledge.idbforum.model.Question;

/**
 * Created by User on 7/20/2018.
 */

public class QuestionAdapter extends BaseAdapter  implements Filterable{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Question> quesList;
    List<Question> mStringFilterList;
    private String[] bgColors;
    ValueFilter valueFilter;


    public QuestionAdapter(Activity activity, List<Question> quesList) {
        this.activity = activity;
        this.quesList = quesList;
        this.mStringFilterList = quesList;
    }

    @Override
    public int getCount() {
        return quesList.size();
    }

    @Override
    public Object getItem(int location) {
        return quesList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_question, null);

        TextView upvote = (TextView) convertView.findViewById(R.id.tvupvote);
        TextView comment = (TextView) convertView.findViewById(R.id.tvcomment);
        TextView quetitle = (TextView) convertView.findViewById(R.id.tvquestitle);
        //TextView quetag = (TextView) convertView.findViewById(R.id.tvtag);
        TextView time = (TextView) convertView.findViewById(R.id.tvtime);
        TextView user = (TextView) convertView.findViewById(R.id.tvuser);
        LinearLayout tagLayout = (LinearLayout) convertView.findViewById(R.id.tag_layout);

        upvote.setText(String.valueOf(quesList.get(position).getUp_vote()));
        comment.setText(String.valueOf(quesList.get(position).getComment_count()));
        if(quesList.get(position).getQues_title().length()>25){
            String title = quesList.get(position).getQues_title().substring(0,26);
            quetitle.setText(title+"...");
        }
        else{
            quetitle.setText(quesList.get(position).getQues_title());
        }
        //quetitle.setText(quesList.get(position).getQues_title());
        //quetag.setText(quesList.get(position).getEvt_date());
        time.setText(quesList.get(position).getQues_date());
        user.setText(String.valueOf(quesList.get(position).getUser_id()));
        if(quesList.get(position).getTags()!=null){
            //quesList.get(position).getTags().clear();
        if(quesList.get(position).getTags().size()>0){
            tagLayout.removeAllViews();
            //quetag.setText(quesList.get(position).getTags().get(0).getTag_name());
            for (int i = 0; i <quesList.get(position).getTags().size() ; i++) {
                TextView btnTag = new TextView(activity);
                btnTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btnTag.setText(quesList.get(position).getTags().get(i).getTag_name());
                btnTag.setId(i+1);
                //btnTag.setPadding(20,20,20,20);
                //btnTag.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                btnTag.setBackgroundResource(R.drawable.layout_bg);
                btnTag.setTextColor(Color.parseColor(quesList.get(position).getTags().get(i).getTag_color()));
                tagLayout.addView(btnTag);
                int paddingDp = 5;
                float density = activity.getResources().getDisplayMetrics().density;
                int paddingPixel = (int)(paddingDp * density+ 0.5f);
                btnTag.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                setMargins(btnTag, 0, 0, 10, 0);
            }
        }}

        return convertView;
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Question> filterList = new ArrayList();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getQues_title().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        Question em = new Question(mStringFilterList.get(i)
                                .getQues_id(), mStringFilterList.get(i)
                                .getUp_vote(), mStringFilterList.get(i)
                                .getComment_count(),mStringFilterList.get(i)
                                .getUser_id(),mStringFilterList.get(i)
                                .getQues_title(),mStringFilterList.get(i)
                                .getQues_desc(), mStringFilterList.get(i)
                                .getQues_date(),mStringFilterList.get(i)
                                .getImage_url(),mStringFilterList.get(i)
                                .getTags());

                        filterList.add(em);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            quesList = (ArrayList<Question>) results.values;
            notifyDataSetChanged();
        }

    }
}

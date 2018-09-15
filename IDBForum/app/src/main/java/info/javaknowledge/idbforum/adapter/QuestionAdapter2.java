package info.javaknowledge.idbforum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

import info.javaknowledge.idbforum.R;
import info.javaknowledge.idbforum.model.Question;

/**
 * Created by User on 7/30/2018.
 */

public class QuestionAdapter2 extends RecyclerView.Adapter<QuestionAdapter2.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Question> quesList;
    List<Question> mStringFilterList;
    private QuestionsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView upvote, comment, quetitle, time, user;
        public LinearLayout tagLayout;

        public MyViewHolder(View view) {
            super(view);
            upvote = (TextView) view.findViewById(R.id.tvupvote);
            comment = (TextView) view.findViewById(R.id.tvcomment);
            quetitle = (TextView) view.findViewById(R.id.tvquestitle);
            time = (TextView) view.findViewById(R.id.tvtime);
            user = (TextView) view.findViewById(R.id.tvuser);
            tagLayout = (LinearLayout) view.findViewById(R.id.tag_layout);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onQuestionSelected(mStringFilterList.get(getAdapterPosition()));
                }
            });
        }
    }


    public QuestionAdapter2(Context context, List<Question> quesList, QuestionsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.quesList = quesList;
        this.mStringFilterList = quesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_question, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Question question = mStringFilterList.get(position);
        holder.upvote.setText(String.valueOf(question.getUp_vote()));
        holder.comment.setText(String.valueOf(question.getComment_count()));
        if(question.getQues_title().length()>25){
            String title = question.getQues_title().substring(0,26);
            holder.quetitle.setText(title+"...");
        }
        else{
            holder.quetitle.setText(question.getQues_title());
        }
        holder.time.setText(question.getQues_date());
        holder.user.setText(String.valueOf(question.getUser_id()));
        if(question.getTags()!=null){
        if(question.getTags().size()>0){
            holder.tagLayout.removeAllViews();
            //quetag.setText(quesList.get(position).getTags().get(0).getTag_name());
            for (int i = 0; i <question.getTags().size() ; i++) {
                TextView btnTag = new TextView(context);
                btnTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btnTag.setText(question.getTags().get(i).getTag_name());
                btnTag.setId(i+1);
                //btnTag.setPadding(20,20,20,20);
                //btnTag.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                btnTag.setBackgroundResource(R.drawable.layout_bg);
                btnTag.setTextColor(Color.parseColor(question.getTags().get(i).getTag_color()));
                holder.tagLayout.addView(btnTag);
                int paddingDp = 5;
                float density = context.getResources().getDisplayMetrics().density;
                int paddingPixel = (int)(paddingDp * density+ 0.5f);
                btnTag.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                setMargins(btnTag, 0, 0, 10, 0);
            }
        }}

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return mStringFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mStringFilterList = quesList;
                } else {
                    List<Question> filteredList = new ArrayList<>();
                    for (Question row : quesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getQues_title().toLowerCase().contains(charString.toLowerCase()) || row.getQues_desc().toLowerCase().contains(charString.toLowerCase())|| row.getTags().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    mStringFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mStringFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mStringFilterList = (ArrayList<Question>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface QuestionsAdapterListener {
        void onQuestionSelected(Question question);
    }
}
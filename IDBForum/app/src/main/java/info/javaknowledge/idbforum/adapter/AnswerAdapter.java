package info.javaknowledge.idbforum.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.javaknowledge.idbforum.R;
import info.javaknowledge.idbforum.model.Answer;
import info.javaknowledge.idbforum.model.Question;

/**
 * Created by User on 7/23/2018.
 */

public class AnswerAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Answer> ansList;
    List<Answer> mStringFilterList;
    private String[] bgColors;
    //ValueFilter valueFilter;


    public AnswerAdapter(Activity activity, List<Answer> ansList) {
        this.activity = activity;
        this.ansList = ansList;
    }

    @Override
    public int getCount() {
        return ansList.size();
    }

    @Override
    public Object getItem(int location) {
        return ansList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_answer, null);

        TextView upvote = (TextView) convertView.findViewById(R.id.tvupvote);
        TextView answer = (TextView) convertView.findViewById(R.id.tvanswer);
        TextView time = (TextView) convertView.findViewById(R.id.tvtime);
        TextView user = (TextView) convertView.findViewById(R.id.tvuser);
        ImageView imgup = (ImageView) convertView.findViewById(R.id.imgup);
        ImageView imgdown = (ImageView) convertView.findViewById(R.id.imgdown);

        upvote.setText(ansList.get(position).getUp_vote());
        answer.setText(ansList.get(position).getAns_desc());
        time.setText(ansList.get(position).getAns_time());
        user.setText(ansList.get(position).getUser_id());

        imgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,
                        ansList.get(position).getAns_desc(),
                        Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}

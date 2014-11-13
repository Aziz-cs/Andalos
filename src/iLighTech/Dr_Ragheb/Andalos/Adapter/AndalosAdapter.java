package iLighTech.Dr_Ragheb.Andalos.Adapter;



import iLighTech.Dr_Ragheb.Andalos.AndalosList;
import iLighTech.Dr_Ragheb.Andalos.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AndalosAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    int imageResource;
    public AndalosAdapter(Activity a, ArrayList<HashMap<String, String>> d, int imageResource) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageResource = imageResource;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        title.setText(song.get(AndalosList.KEY_TITLE));
//        artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        duration.setText(song.get(AndalosList.KEY_DURATION));
        
        thumb_image.setImageResource(imageResource);
        return vi;
    }
}
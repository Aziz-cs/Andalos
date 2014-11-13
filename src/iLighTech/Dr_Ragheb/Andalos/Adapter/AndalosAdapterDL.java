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

public class AndalosAdapterDL extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    int imageResource;
    //for loading

	
    public AndalosAdapterDL(Activity a, ArrayList<HashMap<String, String>> d, int imageResource) {
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
            vi = inflater.inflate(R.layout.list_row_dl, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
//        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        ImageView playOrDownload = (ImageView)vi.findViewById(R.id.img_arrow); // thumb image

        TextView size = (TextView) vi.findViewById(R.id.tv_size);
        
        HashMap<String, String> song = new HashMap<String, String>();

        song = data.get(position);
        


        // Setting all values in listview
    	playOrDownload.setImageResource(R.drawable.download_ic);
        title.setText(song.get(AndalosList.KEY_TITLE));
//        artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        duration.setText("");
        size.setText(AndalosList.lessonSizes[position]);
        title.setTextColor(AndalosList.notDownloadedYetColor);
        thumb_image.setImageResource(imageResource);
    	TextView downloaded_before = (TextView) vi.findViewById(R.id.tv_downloadedB4);
    	ImageView downloaded_mark = (ImageView) vi.findViewById(R.id.iv_videoDL);
    	downloaded_before.setVisibility(4);
    	downloaded_mark.setVisibility(4);
    	//If lesson was completely downloaded:
        if(AndalosList.isLessonCompletelyDownloaded(position) == true){
        	downloaded_before.setVisibility(0);
        	downloaded_mark.setVisibility(0);
        	playOrDownload.setImageResource(R.drawable.arrow);
            duration.setText(song.get(AndalosList.KEY_DURATION));
            title.setTextColor(AndalosList.downloadedColor);
        	size.setText("");
        }
        
        return vi;
    }
}
package com.example.photoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoeditor.Filters.*;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder>
{
    private List<FilterModel> thumbs = new ArrayList<>();
    private List<FilterModel> processedThumbs = new ArrayList<>();
    private FilterAdapter.OnItemSelected onItemSelected;

    public FilterAdapter(Context context, OnItemSelected onItemSelected, int height, int width, Bitmap bitmap)
    {
        this.onItemSelected = onItemSelected;
        Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, width, height, false);
        thumbs.add(new FilterModel(thumbImage, null, "Original", FilterType.NONE));
        thumbs.add(new FilterModel(thumbImage, AdeleFilter.getFilter(), AdeleFilter.name, FilterType.ADELE));
        thumbs.add(new FilterModel(thumbImage, AmazonFilter.getFilter(), AmazonFilter.name, FilterType.AMAZON));
        thumbs.add(new FilterModel(thumbImage, AprilFilter.getFilter(), AprilFilter.name, FilterType.APRIL));
        thumbs.add(new FilterModel(thumbImage, ArtisticBlackWhiteFilter.getFilter(), ArtisticBlackWhiteFilter.name, FilterType.ARTISTIC_BLACK_WHITE));
        thumbs.add(new FilterModel(thumbImage, AudreyFilter.getFilter(), AudreyFilter.name, FilterType.AUDREY));
        thumbs.add(new FilterModel(thumbImage, AweStruckVibeFilter.getFilter(), AweStruckVibeFilter.name, FilterType.AWE_STRUCK_VIBE));
        thumbs.add(new FilterModel(thumbImage, BlueMessFilter.getFilter(), BlueMessFilter.name, FilterType.BLUE_MESS));
        thumbs.add(new FilterModel(thumbImage, ClarendonFilter.getFilter(), ClarendonFilter.name, FilterType.CLARENDON));
        thumbs.add(new FilterModel(thumbImage, CruzFilter.getFilter(), CruzFilter.name, FilterType.CRUZ));
        thumbs.add(new FilterModel(thumbImage, CutenessFilter.getFilter(), CutenessFilter.name, FilterType.CUTENESS));
        thumbs.add(new FilterModel(thumbImage, HaanFilter.getFilter(), HaanFilter.name, FilterType.HAAN));
        thumbs.add(new FilterModel(thumbImage, LimeStutterFilter.getFilter(), LimeStutterFilter.name, FilterType.LIME_STUTTER));
        thumbs.add(new FilterModel(thumbImage, MarsFilter.getFilter(), MarsFilter.name, FilterType.MARS));
        thumbs.add(new FilterModel(thumbImage, MayFairFilter.getFilter(), MayFairFilter.name, FilterType.MAY_FAIR));
        thumbs.add(new FilterModel(thumbImage, MetropolisFilter.getFilter(), MetropolisFilter.name, FilterType.METROPOLIS));
        thumbs.add(new FilterModel(thumbImage, MonoChromeFilter.getFilter(), MonoChromeFilter.name, FilterType.MONO_CHROME));
        thumbs.add(new FilterModel(thumbImage, NightWhisperFilter.getFilter(), NightWhisperFilter.name, FilterType.NIGHT_WHISPER));
        thumbs.add(new FilterModel(thumbImage, OldManFilter.getFilter(), OldManFilter.name, FilterType.OLD_MAN));
        thumbs.add(new FilterModel(thumbImage, PurplishFilter.getFilter(), PurplishFilter.name, FilterType.PURPLISH));
        thumbs.add(new FilterModel(thumbImage, RiseFilter.getFilter(), RiseFilter.name, FilterType.RISE));
        thumbs.add(new FilterModel(thumbImage, SepiaFilter.getFilter(), SepiaFilter.name, FilterType.SEPIA));
        thumbs.add(new FilterModel(thumbImage, SierraFilter.getFilter(), SierraFilter.name, FilterType.SIERRA));
        thumbs.add(new FilterModel(thumbImage, StarLitFilter.getFilter(), StarLitFilter.name, FilterType.STAR_LIT));
        setupFilters(context);
    }

    public interface OnItemSelected
    {
        void onFilterSelected(Bitmap bitmap, Filter filter);
    }

    class FilterModel
    {
        private String filterName;
        private Bitmap bitmap;
        private Filter filter;
        private FilterType filterType;

        public FilterModel(Bitmap bitmap, Filter filter, String filterName, FilterType filterType)
        {
            this.filterName = filterName;
            this.bitmap = bitmap;
            this.filter = filter;
            this.filterType = filterType;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageFilterView;
        TextView filterName;

        ViewHolder(View itemView)
        {
            super(itemView);
            imageFilterView = itemView.findViewById(R.id.imgFilterView);
            filterName = itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemSelected.onFilterSelected(thumbs.get(getLayoutPosition()).bitmap, thumbs.get(getLayoutPosition()).filter);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        FilterModel filterPair = processedThumbs.get(position);
        holder.imageFilterView.setImageBitmap(filterPair.bitmap);
        holder.filterName.setText(filterPair.filterName);
    }

    @Override
    public int getItemCount()
    {
        return thumbs.size();
    }

    private void setupFilters(Context context)
    {
        for (FilterModel thumb : thumbs)
        {
            int size = (int)context.getResources().getDimension(R.dimen.thumbnail_size);
            thumb.bitmap = Bitmap.createScaledBitmap(thumb.bitmap, size, size, false);
            if( thumb.filter != null )
                thumb.bitmap = thumb.filter.processFilter(thumb.bitmap);
            thumb.bitmap = generateCircularBitmap(thumb.bitmap);
            processedThumbs.add(thumb);
        }
    }

    private Bitmap generateCircularBitmap(Bitmap input)
    {
        final int width = input.getWidth();
        final int height = input.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW
        );

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(input, 0, 0, null);
        return outputBitmap;
    }
}

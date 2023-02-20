package edu.northeastern.nucs5520sp_musiclyicsapp.a6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.northeastern.nucs5520sp_musiclyicsapp.R;

/**
 * Adapter class for the Conversion History RecyclerView.
 */
public class ConversionHistoryAdapter extends RecyclerView.Adapter<ConversionHistoryAdapter.ConversionHistoryViewHolder> {

    private final Context context;
    private final ArrayList<Conversion> conversionList;

    public ConversionHistoryAdapter(Context context, ArrayList<Conversion> conversionList) {
        this.context = context;
        this.conversionList = conversionList;
    }


    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List). Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary View.findViewById(int) calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ConversionHistoryAdapter.ConversionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.conversion_history_row, parent, false);
        return new ConversionHistoryAdapter.ConversionHistoryViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the ViewHolder.itemView to reflect the item at the given
     * position.
     * Credit: https://www.youtube.com/watch?v=Mc0XT58A1Z4
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use ViewHolder.getAdapterPosition() which will
     * have the updated adapter position.
     * <p>
     * Override onBindViewHolder(ViewHolder, int, List) instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ConversionHistoryAdapter.ConversionHistoryViewHolder holder, int position) {
        holder.tvFromAmount.setText(conversionList.get(position).getOriginalAmount());
        holder.tvFromCurrency.setText(conversionList.get(position).getFromCurrency());
        holder.tvToAmount.setText(conversionList.get(position).getConvertedAmount());
        holder.tvToCurrency.setText(conversionList.get(position).getToCurrency());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return conversionList.size();
    }

    public static class ConversionHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvFromAmount, tvFromCurrency, tvToAmount, tvToCurrency;

        public ConversionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFromAmount = itemView.findViewById(R.id.textView_from_amount_row);
            tvFromCurrency = itemView.findViewById(R.id.textView_from_currency_row);
            tvToAmount = itemView.findViewById(R.id.textView_to_amount_row);
            tvToCurrency = itemView.findViewById(R.id.textView_to_currency_row);

        }
    }
}

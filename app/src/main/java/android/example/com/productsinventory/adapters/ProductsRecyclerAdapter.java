package android.example.com.productsinventory.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.example.com.productsinventory.MainActivity;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.data.Product;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ProductViewHolder> {

    List<Product> products;
    Context context;

    public ProductsRecyclerAdapter(Context con, List<Product> products) {
        this.products = products;
        this.context = con;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView productInfo;
        TextView productQuantity;
        Button sellItem;

        public ProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvProduct);
            productInfo = (TextView) itemView.findViewById(R.id.tvProductInfo);
            productQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            sellItem = (Button) itemView.findViewById(R.id.btnSell);
        }
    }

    @Override
    public ProductsRecyclerAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductsRecyclerAdapter.ProductViewHolder holder, int position) {
        holder.productInfo.setText(products.get(position).getName() + " - $" + products.get(position).getPrice());
        holder.productQuantity.setText("Quantity available: " + String.valueOf(products.get(position).getQuantity()));

        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Start new activity

            }

        });

        holder.sellItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Select the value: ");

                NumberPicker np = new NumberPicker(context);
                String[] nums = new String[100];
                for(int i=0; i<nums.length; i++)
                    nums[i] = Integer.toString(i);

                np.setMinValue(1);
                np.setMaxValue(nums.length-1);
                np.setWrapSelectorWheel(false);
                np.setDisplayedValues(nums);
                np.setValue(50);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel.
                    }
                });

                alert.setView(np);
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(products!= null){
            return products.size();
        }
        else
            return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
